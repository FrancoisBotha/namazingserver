package io.francoisbotha.namazingserver.utility;

import io.francoisbotha.namazingserver.domain.dto.MenuDto;
import io.francoisbotha.namazingserver.domain.dto.SpecialDto;
import io.francoisbotha.namazingserver.domain.dto.VendorDto;
import io.francoisbotha.namazingserver.domain.model.Menu;
import io.francoisbotha.namazingserver.domain.model.Special;
import io.francoisbotha.namazingserver.domain.model.Vendor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Utility {

    public static void displayText(InputStream input) throws IOException{
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            System.out.println("    " + line);
        }
    }

    public static List getSortedVendors(Iterable<Vendor> vendorsIt) {
        Iterator<Vendor> iter = vendorsIt.iterator();
        List vendors = new ArrayList();
        while (iter.hasNext()) {
            vendors.add(iter.next());
        }
        Collections.sort(vendors, new SortVendorByNum());
        return vendors;
    }

    public static List getSortedMenus(Iterable<Menu> menusIt) {
        Iterator<Menu> iter = menusIt.iterator();
        List menus = new ArrayList();
        while (iter.hasNext()) {
            menus.add(iter.next());
        }
        Collections.sort(menus, new SortMenuByNo());
        return menus;
    }

    public static List getSortedSpecials(Iterable<Special> specialsIt) {
        Iterator<Special> iter = specialsIt.iterator();
        List specials = new ArrayList();
        while (iter.hasNext()) {
            specials.add(iter.next());
        }
        Collections.sort(specials, new SortSpecialByNo());
        return specials;
    }

    public static VendorDto getVendorDto(Vendor vendor) {
        VendorDto vendorDto = new VendorDto();

        vendorDto.setId(vendor.getId());
        vendorDto.setNum(vendor.getNum());
        vendorDto.setVendorCde(vendor.getVendorCde());
        vendorDto.setVendorName(vendor.getVendorName());
        vendorDto.setVendorLogoUrl(vendor.getVendorLogoUrl());

        return vendorDto;

    }

    public static MenuDto getMenuDto(Menu menu) {
        MenuDto menuDto = new MenuDto();

        menuDto.setId(menu.getId());
        menuDto.setMenuNo(menu.getMenuNo());
        menuDto.setMenuName(menu.getMenuName());
        menuDto.setMenuImgUrl(menu.getMenuImgUrl());
        menuDto.setMenuDesc(menu.getMenuDesc());

        return menuDto;

    }

    public static SpecialDto getSpecialDto(Special special) {
        SpecialDto specialDto = new SpecialDto();

        specialDto.setId(special.getId());
        specialDto.setSpecialNo(special.getSpecialNo());
        specialDto.setSpecialName(special.getSpecialName());
        specialDto.setSpecialImgUrl(special.getSpecialImgUrl());
        specialDto.setSpecialDesc(special.getSpecialDesc());

        return specialDto;

    }

}





