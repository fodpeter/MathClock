package peter.mathclock;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.common.collect.ListMultimap;

import org.joda.time.LocalTime;

import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

public class Clock {
    private final ImageView hoursView;
    private final ImageView minutesView;
    private final ImageView secondsView;
    private final ListMultimap<Integer, Integer> mapping;
    private Random random = new Random();

    public Clock(ImageView hoursView, ImageView minutesView, ImageView secondsView, ListMultimap<Integer, Integer> mapping) {
        this.hoursView = checkNotNull(hoursView);
        this.minutesView = checkNotNull(minutesView);
        this.secondsView = checkNotNull(secondsView);
        this.mapping = checkNotNull(mapping);
    }

    public void show(@NonNull LocalTime date) {
        show(hoursView, date.getHourOfDay());
        show(minutesView, date.getMinuteOfHour());
        show(secondsView, date.getSecondOfMinute());
    }

    private void show(ImageView view, int n) {
        List<Integer> closestList = ClosestSelector.getClosestList(mapping, n);
        int id;
        if (!closestList.isEmpty()) {
            id = closestList.get(random.nextInt(closestList.size()));
        } else {
            id = 0;
        }
        view.setImageResource(id);
    }

}
