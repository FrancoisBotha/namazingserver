package io.francoisbotha.namazingserver.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log
@Controller
public class VendorsController {

    @RequestMapping({"/admin/vendors"})
    @GetMapping
    //@PostMapping
    public String ShowVendorPage(Model model) {
        return "Vendors";
    }

    @RequestMapping({"/admin/vendors/new"})
    @GetMapping
    //@PostMapping
    public String ShowVendorNEwPage(Model model) {
        return "Vendor_new";
    }

}

