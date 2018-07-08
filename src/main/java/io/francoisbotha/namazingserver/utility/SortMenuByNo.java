package io.francoisbotha.namazingserver.utility;

import io.francoisbotha.namazingserver.domain.model.Menu;
import java.util.Comparator;

public class SortMenuByNo implements Comparator<Menu> {
    public int compare(Menu a, Menu b)
    {
        return a.getMenuNo() - b.getMenuNo();
    }
}
