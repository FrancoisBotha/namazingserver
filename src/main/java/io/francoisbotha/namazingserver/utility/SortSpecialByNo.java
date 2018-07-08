package io.francoisbotha.namazingserver.utility;

import io.francoisbotha.namazingserver.domain.model.Special;

import java.util.Comparator;

public class SortSpecialByNo implements Comparator<Special> {

    public int compare(Special a, Special b)
    {
        return a.getSpecialNo() - b.getSpecialNo();
    }

}
