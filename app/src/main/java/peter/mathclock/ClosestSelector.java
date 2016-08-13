package peter.mathclock;


import com.google.common.collect.ListMultimap;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ClosestSelector {

    public static List<Integer> getClosestList(ListMultimap<Integer, Integer> mapping, int n) {
        List<Integer> list = mapping.get(getClosest(mapping.keySet(), n));
        if (list!=null) {
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    public static int getClosest(Set<Integer> set, int n) {
        for (int i=0;i<60;i++) {
            int key = (n + i)%60;
            if (set.contains(key)) {
                return key;
            }
        }
        return 0;
    }

}
