package io.francoisbotha.namazingserver.controller;

import io.francoisbotha.namazingserver.domain.dao.VendorRepository;
import io.francoisbotha.namazingserver.domain.dto.VendorDto;
import io.francoisbotha.namazingserver.domain.model.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Slf4j
@Controller
public class VendorSpecialsController {

    @Autowired
    VendorRepository vendorRepository;

    private static final String SPECIAL_VIEW_NAME = "VendorSpecial";

    @RequestMapping(value = "/admin/special/{id}", method = RequestMethod.GET)
    public String ViewSpecials(Model model,
                           @PathVariable("id") String id) {

        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()){
            Vendor vendor = vendorOptional.get();
            VendorDto vendorDto = new VendorDto();

            vendorDto.setId(vendor.getId());
            vendorDto.setVendorNo(vendor.getVendorNo());
            vendorDto.setVendorCde(vendor.getVendorCde());
            vendorDto.setVendorName(vendor.getVendorName());
            vendorDto.setVendorLogoUrl(vendor.getVendorLogoUrl());

            model.addAttribute(VendorController.VENDOR_MODEL_KEY , vendorDto);

            return this.SPECIAL_VIEW_NAME ;

        }

        return this.SPECIAL_VIEW_NAME ;
    }

}


