package io.francoisbotha.namazingserver.controller;

import io.francoisbotha.namazingserver.domain.dao.SpecialRepository;
import io.francoisbotha.namazingserver.domain.dao.VendorRepository;
import io.francoisbotha.namazingserver.domain.dto.SpecialDto;
import io.francoisbotha.namazingserver.domain.model.Special;
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
public class VendorSpecialsController {

    @Autowired
    S3Service s3Service;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    SpecialRepository specialRepository;

    private static final String SPECIAL_VIEW_NAME = "VendorSpecial";
    private static final String NEW_SPECIAL_VIEW_NAME = "VendorSpecial_new";
    private static final String VIEW_SPECIAL_VIEW_NAME = "VendorSpecial_view";
    private static final String MOD_SPECIAL_VIEW_NAME = "VendorSpecial_mod";

    /* Key which identifies data models */
    public static final String VENDOR_MODEL_KEY = "vendor";
    public static final String SPECIAL_MODEL_KEY = "special";
    private static final String SPECIALLIST_MODEL_KEY = "specials";

    @RequestMapping(value = "/admin/special/{id}", method = RequestMethod.GET)
    public String ViewSpecial(Model model,
                           @PathVariable("id") String id) {

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()){

            Iterable<Special> specialsIt = specialRepository.findAll();

            List specials = Utility.getSortedSpecials(specialsIt);

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));
            model.addAttribute(this.SPECIALLIST_MODEL_KEY, specials);

            return this.SPECIAL_VIEW_NAME;

        }

        return this.SPECIAL_VIEW_NAME;

    }

    @RequestMapping(value = "/admin/special/{id}/view/{specialId}", method = RequestMethod.GET)
    public String ViewSpecial(Model model,
                           @PathVariable("id") String id,
                           @PathVariable("specialId") String specialId){

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        Optional<Special> specialOptional = specialRepository.findById(specialId);
        if (vendorOptional.isPresent()
                && specialOptional.isPresent()){

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));
            model.addAttribute(this.SPECIAL_MODEL_KEY , Utility.getSpecialDto(specialOptional.get()));

            return this.VIEW_SPECIAL_VIEW_NAME;

        }

        return this.VIEW_SPECIAL_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/special/{id}/new", method = RequestMethod.GET)
    public String ShowAdminNewPage(ModelMap model, @PathVariable("id") String id) {

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()){

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));

            SpecialDto specialDto = new SpecialDto();
            model.addAttribute(this.SPECIAL_MODEL_KEY , specialDto);
            return this.NEW_SPECIAL_VIEW_NAME;

        }

        return this.NEW_SPECIAL_VIEW_NAME;

    }

    @RequestMapping(value = "/admin/special/{id}/new", method = RequestMethod.POST)
    public String AdminModPost(@RequestParam(name = "file", required = false) MultipartFile file,
                               @ModelAttribute(SPECIAL_MODEL_KEY) @Valid SpecialDto specialDto
            , BindingResult bindingResult, ModelMap model, @PathVariable("id") String id) {


        //Get Vendor
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);

        if (vendorOptional.isPresent() ){

            Vendor vendor = vendorOptional.get();

            if (bindingResult.hasErrors()) {
                model.addAttribute(this.VENDOR_MODEL_KEY , vendor);
                return this.NEW_SPECIAL_VIEW_NAME;
            }

            Special special = new Special();

            special.setVendorId(vendor.getId());
            special.setSpecialName(specialDto.getSpecialName());
            special.setSpecialNo(specialDto.getSpecialNo());
            special.setSpecialDesc(specialDto.getSpecialDesc());

            if (file != null && !file.isEmpty()) {
                log.debug("In file block");
                String specialImageUrl = s3Service.storeImage(file, vendor.getVendorCde(), "specialLogo");
                if (specialImageUrl != null) {
                    log.debug("update image");
                    special.setSpecialImgUrl(specialImageUrl);
                } else {
                    log.debug("Could not upload file to S3");
                }
            }

            specialRepository.save(special);

        }

        return "redirect:/admin/special/" + id;
    }

    @RequestMapping(value = "/admin/special/{id}/mod/{specialId}", method = RequestMethod.GET)
    public String modSpecial(Model model,
                          @PathVariable("id") String id,
                          @PathVariable("specialId") String specialId){

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        Optional<Special> specialOptional = specialRepository.findById(specialId);
        if (vendorOptional.isPresent()
                && specialOptional.isPresent()){

            model.addAttribute(this.VENDOR_MODEL_KEY , Utility.getVendorDto(vendorOptional.get()));
            model.addAttribute(this.SPECIAL_MODEL_KEY , Utility.getSpecialDto(specialOptional.get()));

            return this.MOD_SPECIAL_VIEW_NAME;

        }

        return this.MOD_SPECIAL_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/special/{id}/mod/{specialId}", method = RequestMethod.POST)
    public String VendorModPost(@RequestParam(name = "file", required = false) MultipartFile file,
                                @ModelAttribute(SPECIAL_MODEL_KEY) @Valid SpecialDto specialDto,
                                BindingResult bindingResult, ModelMap model,
                                @PathVariable("id") String id, @PathVariable("specialId") String specialId) {

        if (bindingResult.hasErrors()) {
            log.debug("ERROR");
            return this.MOD_SPECIAL_VIEW_NAME;
        }

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        Optional<Special> specialOptional = specialRepository.findById(specialId);

        if (vendorOptional.isPresent()
                && specialOptional.isPresent()){

            Vendor vendor = vendorOptional.get();
            Special special = specialOptional.get();

            special.setSpecialName(specialDto.getSpecialName());
            special.setSpecialNo(specialDto.getSpecialNo());
            special.setSpecialDesc(specialDto.getSpecialDesc());

            if (file != null && !file.isEmpty()) {
                log.debug("In file block");
                String vendorImageUrl = s3Service.storeImage(file, vendor.getVendorCde(), special.getSpecialName());
                if (vendorImageUrl != null) {
                    log.debug("update image");
                    vendor.setVendorLogoUrl(vendorImageUrl);
                } else {
                    log.debug("Could not upload file to S3");
                }
            }

            specialRepository.save(special);

        }

        return "redirect:/admin/special/" + id + "/view/" + specialId;
    }

    @RequestMapping(value = "/admin/special/{id}/delete/{specialId}", method = RequestMethod.DELETE)
    public String DeleteSpecial(Model model,
                             @PathVariable("id") String id,
                             @PathVariable("specialId") String specialId) {

        Optional<Special> specialOptional = specialRepository.findById(specialId);

        if (specialOptional.isPresent()){

            Special special = specialOptional.get();
            specialRepository.delete(special);

        }

        return "redirect:/admin/special/" + id;
    }

}


