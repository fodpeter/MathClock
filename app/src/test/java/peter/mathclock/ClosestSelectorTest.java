package peter.mathclock;

import com.google.common.collect.Sets;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ClosestSelectorTest {

    @Test
    public void fallbackToZeroForEmptySet() throws Exception {
        assertEquals(0, ClosestSelector.getClosest(Collections.<Integer>emptySet(), 1));
    }

    @Test
    public void selectsTheExisting() throws Exception {
        assertEquals(1, ClosestSelector.getClosest(Collections.singleton(1), 1));
    }

    @Test
    public void selectsTheExistingFromMore() throws Exception {
        assertEquals(2, ClosestSelector.getClosest(Sets.newHashSet(1,2,3), 2));
    }

    @Test
    public void selectsTheNextIfMissing() throws Exception {
        assertEquals(3, ClosestSelector.getClosest(Sets.newHashSet(1,3), 2));
        assertEquals(1, ClosestSelector.getClosest(Sets.newHashSet(1,3), 6));
    }
}