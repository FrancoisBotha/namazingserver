package io.francoisbotha.namazingserver.controller;

import io.francoisbotha.namazingserver.domain.dao.MenuRepository;
import io.francoisbotha.namazingserver.domain.dao.VendorRepository;
import io.francoisbotha.namazingserver.domain.dto.MenuDto;
import io.francoisbotha.namazingserver.domain.model.Menu;
import io.francoisbotha.namazingserver.domain.model.Vendor;
import io.francoisbotha.namazingserver.services.S3Service;
import io.francoisbotha.namazingserver.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class VendorMenuController {

    @Autowired
    S3Service s3Service;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    MenuRepository menuRepository;

    private static final String MENU_VIEW_NAME = "VendorMenu";
    private static final String NEW_MENU_VIEW_NAME = "VendorMenu_new";
    private static final String VIEW_MENU_VIEW_NAME = "VendorMenu_view";
    private static final String MOD_MENU_VIEW_NAME = "VendorMenu_mod";

    /* Key which identifies data models */
    public static final String VENDOR_MODEL_KEY = "vendor";
    public static final String MENU_MODEL_KEY = "menu";
    private static final String MENULIST_MODEL_KEY = "menus";

    @RequestMapping(value = "/admin/menu/{id}", method = RequestMethod.GET)
    public String ViewMenu(Model model,
                             @PathVariable("id") String id) {

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()){

            Iterable<Menu> menusIt = menuRepository.findAllByVendorId(id);

            List menus = Utility.getSortedMenus(menusIt);

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));
            model.addAttribute(this.MENULIST_MODEL_KEY, menus);

            return this.MENU_VIEW_NAME;

        }

        return this.MENU_VIEW_NAME;

    }

    @RequestMapping(value = "/admin/menu/{id}/view/{menuId}", method = RequestMethod.GET)
    public String ViewMenu(Model model,
                            @PathVariable("id") String id,
                            @PathVariable("menuId") String menuId){

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        Optional<Menu> menuOptional = menuRepository.findById(menuId);
        if (vendorOptional.isPresent()
            && menuOptional.isPresent()){

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));
            model.addAttribute(this.MENU_MODEL_KEY , Utility.getMenuDto(menuOptional.get()));

            return this.VIEW_MENU_VIEW_NAME;

        }

        return this.VIEW_MENU_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/menu/{id}/new", method = RequestMethod.GET)
    public String ShowAdminNewPage(ModelMap model, @PathVariable("id") String id) {

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()){

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));

            MenuDto menuDto = new MenuDto();
            model.addAttribute(this.MENU_MODEL_KEY , menuDto);
            return this.NEW_MENU_VIEW_NAME;

        }

        return this.NEW_MENU_VIEW_NAME;

    }

    @RequestMapping(value = "/admin/menu/{id}/new", method = RequestMethod.POST)
    public String AdminModPost(@RequestParam(name = "file", required = false) MultipartFile file,
                                @ModelAttribute(MENU_MODEL_KEY) @Valid MenuDto menuDto
            , BindingResult bindingResult, ModelMap model, @PathVariable("id") String id) {

        //Get Vendor
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);

        if (vendorOptional.isPresent() ) {
            Vendor vendor = vendorOptional.get();

            if (bindingResult.hasErrors()) {
                model.addAttribute(this.VENDOR_MODEL_KEY , vendor);
                return this.NEW_MENU_VIEW_NAME;
            }

            Menu menu = new Menu();

            menu.setVendorId(vendor.getId());
            menu.setMenuName(menuDto.getMenuName());
            menu.setMenuNo(menuDto.getMenuNo());
            menu.setMenuDesc(menuDto.getMenuDesc());

            if (file != null && !file.isEmpty()) {
                log.debug("In file block");
                String menuImageUrl = s3Service.storeImage(file, vendor.getVendorCde(), "menuLogo");
                if (menuImageUrl != null) {
                    log.debug("update image");
                    menu.setMenuImgUrl(menuImageUrl);
                } else {
                    log.debug("Could not upload file to S3");
                }
            }

            menuRepository.save(menu);

            return "redirect:/admin/menu/" + id;

        }

        return "redirect:/admin/menu/" + id;

    }

    @RequestMapping(value = "/admin/menu/{id}/mod/{menuId}", method = RequestMethod.GET)
    public String modMenu(Model model,
                          @PathVariable("id") String id,
                          @PathVariable("menuId") String menuId){

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        Optional<Menu> menuOptional = menuRepository.findById(menuId);
        if (vendorOptional.isPresent()
                && menuOptional.isPresent()){

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));
            model.addAttribute(this.MENU_MODEL_KEY , Utility.getMenuDto(menuOptional.get()));

            return this.MOD_MENU_VIEW_NAME;

        }

        return this.MOD_MENU_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/menu/{id}/mod/{menuId}", method = RequestMethod.POST)
    public String VendorModPost(@RequestParam(name = "file", required = false) MultipartFile file,
                                @ModelAttribute(MENU_MODEL_KEY) @Valid MenuDto menuDto,
                                BindingResult bindingResult, ModelMap model,
                                @PathVariable("id") String id, @PathVariable("menuId") String menuId) {

        if (bindingResult.hasErrors()) {
            log.debug("ERROR");
            return this.MOD_MENU_VIEW_NAME;
        }

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        Optional<Menu> menuOptional = menuRepository.findById(menuId);

        if (vendorOptional.isPresent()
                && menuOptional.isPresent()){

            Vendor vendor = vendorOptional.get();
            Menu menu = menuOptional.get();

            menu.setMenuName(menuDto.getMenuName());
            menu.setMenuNo(menuDto.getMenuNo());
            menu.setMenuDesc(menuDto.getMenuDesc());

            if (file != null && !file.isEmpty()) {
                log.debug("In file block");
                String vendorImageUrl = s3Service.storeImage(file, vendor.getVendorCde(), menu.getMenuName());
                if (vendorImageUrl != null) {
                    log.debug("update image");
                    vendor.setVendorLogoUrl(vendorImageUrl);
                } else {
                    log.debug("Could not upload file to S3");
                }
            }

            menuRepository.save(menu);

        }

        return "redirect:/admin/menu/" + id + "/view/" + menuId;
    }

    @RequestMapping(value = "/admin/menu/{id}/delete/{menuId}", method = RequestMethod.DELETE)
    public String DeleteMenu(Model model,
                               @PathVariable("id") String id,
                               @PathVariable("menuId") String menuId) {

        Optional<Menu> menuOptional = menuRepository.findById(menuId);

        if (menuOptional.isPresent()){

            Menu menu = menuOptional.get();
            menuRepository.delete(menu);

        }

        return "redirect:/admin/menu/" + id;
    }

}


