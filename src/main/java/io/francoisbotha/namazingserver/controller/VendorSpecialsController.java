package io.francoisbotha.namazingserver.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log
@Controller
@RequestMapping({"/admin/vendorspecial"})
public class VendorSpecialsController {
    @GetMapping
    //@PostMapping
    public String ShowVendorSpecialPage(Model model) {
        return "VendorSpecial";
    }
}
