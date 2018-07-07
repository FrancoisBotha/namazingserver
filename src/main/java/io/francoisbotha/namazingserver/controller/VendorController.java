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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import io.francoisbotha.namazingserver.domain.dto.VendorDto;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    VendorRepository repository;

    private static final String VENDOR_VIEW_NAME = "Vendors";
    private static final String NEW_VENDOR_VIEW_NAME = "Vendor_new";

    /* Key which identifies vendor payload in Model */
    public static final String VENDOR_MODEL_KEY = "vendor";

    @RequestMapping(value = "/admin/vendor", method = RequestMethod.GET)
    public String ShowVendorPage(Model model) {

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
                             @ModelAttribute(VENDOR_MODEL_KEY) @Valid VendorDto vendorDto,
                             ModelMap model) {

        log.debug(vendorDto.getVendorCde());

        Vendor vendor = new Vendor();
        vendor.setVendorCde(vendorDto.getVendorCde());
        vendor.setVendorName(vendorDto.getVendorName());
        repository.save(vendor);

        List<Vendor> result = (List<Vendor>) repository.findAll();

        log.debug(result.toString());

        if (file != null && !file.isEmpty()) {
            log.debug("In file block");
            String vendorImageUrl = s3Service.storeProfileImage(file, vendorDto.getVendorCde());
            if (vendorImageUrl != null) {
                log.debug("update image");
            } else {
                log.debug("Could not upload file to S3");
            }
        }
        return VendorController.VENDOR_VIEW_NAME;
    }

}
