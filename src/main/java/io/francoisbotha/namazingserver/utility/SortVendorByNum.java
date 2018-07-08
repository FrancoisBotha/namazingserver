package io.francoisbotha.namazingserver.utility;

import io.francoisbotha.namazingserver.domain.model.Vendor;

import java.util.Comparator;

public class SortVendorByNum implements Comparator<Vendor> {
    public int compare(Vendor a, Vendor b)
    {
        return a.getNum() - b.getNum();
    }
}
