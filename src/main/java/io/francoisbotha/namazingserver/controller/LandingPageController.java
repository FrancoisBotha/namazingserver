package io.francoisbotha.namazingserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/")
public class LandingPageController {

    @GetMapping
    public String ShowLandingPage(ModelMap model) {

        log.info("In ShowLandingPage Controller");
        return "landingpage";

    }
}