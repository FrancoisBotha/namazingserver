package io.francoisbotha.namazingserver.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.francoisbotha.namazingserver.domain.dao.VendorRepository;
import io.francoisbotha.namazingserver.domain.model.Vendor;
import io.francoisbotha.namazingserver.services.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.francoisbotha.namazingserver.domain.dto.VendorDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class VendorController {

    @Autowired
    S3Service s3Service;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    VendorRepository vendorRepository;

    private static final String VENDOR_VIEW_NAME = "Vendors";
    private static final String NEW_VENDOR_VIEW_NAME = "Vendor_new";
    private static final String VIEW_VENDOR_VIEW_NAME = "Vendor_view";

    /* Key which identifies vendor payload in Model */
    public static final String VENDOR_MODEL_KEY = "vendor";
    private static final String VENDORLIST_MODEL_KEY = "vendors";

    @RequestMapping(value = "/admin/vendor/delete/{id}", method = RequestMethod.GET)
    public String DeleteVendor(Model model,
                               @PathVariable("id") String id) {

        log.debug(id);

        return "redirect:/admin/vendor";
    }

    @RequestMapping(value = "/admin/vendor/view/{id}", method = RequestMethod.GET)
    public String ViewVendor(Model model,
                               @PathVariable("id") String id) {

        VendorDto vendorDto = new VendorDto();
        vendorDto.setVendorName("Some Vendor");
        model.addAttribute(VendorController.VENDOR_MODEL_KEY , vendorDto);

        return VendorController.VIEW_VENDOR_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/vendor", method = RequestMethod.GET)
    public String ShowVendorPage(Model model) {

        List<Vendor> vendorList = (List<Vendor>) vendorRepository.findAll();

        for (Vendor vendor : vendorRepository.findAll()) {
            log.info(vendor.toString());
        }

        log.debug(vendorList.toArray().toString());

        model.addAttribute(VendorController.VENDORLIST_MODEL_KEY, vendorRepository.findAll());
        return VendorController.VENDOR_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/vendor/new", method = RequestMethod.GET)
    public String ShowVendorNEwPage(ModelMap model) {
        VendorDto vendorDto = new VendorDto();
        model.addAttribute(VendorController.VENDOR_MODEL_KEY , vendorDto);

        return VendorController.NEW_VENDOR_VIEW_NAME;
    }

    @RequestMapping(value = "/admin/vendor/new", method = RequestMethod.POST)
    public String VendorPost(@RequestParam(name = "file", required = false) MultipartFile file,
                             @ModelAttribute(VENDOR_MODEL_KEY) @Valid VendorDto vendorDto
                             , BindingResult bindingResult, ModelMap model) {

        if (bindingResult.hasErrors()) {
            return VendorController.NEW_VENDOR_VIEW_NAME;
        }

        Vendor vendor = new Vendor();
        vendor.setVendorCde(vendorDto.getVendorCde());
        vendor.setVendorName(vendorDto.getVendorName());

        if (file != null && !file.isEmpty()) {
            log.debug("In file block");
            String vendorImageUrl = s3Service.storeProfileImage(file, vendorDto.getVendorCde());
            if (vendorImageUrl != null) {
                log.debug("update image");
                vendor.setVendorLogoUrl(vendorImageUrl);
            } else {
                log.debug("Could not upload file to S3");
            }
        }

        vendorRepository.save(vendor);

        return VendorController.VENDOR_VIEW_NAME;
    }

}
