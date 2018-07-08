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
public class VendorMenuController {

    @Autowired
    VendorRepository vendorRepository;

    private static final String MENU_VIEW_NAME = "VendorMenu";

    @RequestMapping(value = "/admin/menu/{id}", method = RequestMethod.GET)
    public String ViewMenu(Model model,
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

            return this.MENU_VIEW_NAME;

        }

        return this.MENU_VIEW_NAME;
    }

}


