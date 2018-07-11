package io.francoisbotha.namazingserver.controller;

import io.francoisbotha.namazingserver.domain.dao.MenuRepository;
import io.francoisbotha.namazingserver.domain.dao.SpecialRepository;
import io.francoisbotha.namazingserver.domain.dao.VendorRepository;
import io.francoisbotha.namazingserver.domain.model.Menu;
import io.francoisbotha.namazingserver.domain.model.Special;
import io.francoisbotha.namazingserver.domain.model.Vendor;
import io.francoisbotha.namazingserver.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ApiController {

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    MenuRepository menuRepository;


    @Autowired
    SpecialRepository specialRepository;


    @RequestMapping(value = "/api/vendor", method = RequestMethod.GET)
    public List getVendors (Model model) {

        Iterable<Vendor> vendorsIt = vendorRepository.findAll();

        List vendors = Utility.getSortedVendors(vendorsIt);

        return vendors;

    }

    @RequestMapping(value = "/api/vendor/menu/{id}", method = RequestMethod.GET)
    public List getVendorMenu (Model model, @PathVariable("id") String id ) {

        Iterable<Menu> menusIt = menuRepository.findAllByVendorId(id);

        List menus = Utility.getSortedMenus(menusIt);

        return menus;

    }

    @RequestMapping(value = "/api/vendor/special/{id}", method = RequestMethod.GET)
    public List getVendorSpecials (Model model, @PathVariable("id") String id ) {

        Iterable<Special> specialsIt = specialRepository.findAllByVendorId(id);

        List special = Utility.getSortedSpecials(specialsIt);

        return special;

    }

}
