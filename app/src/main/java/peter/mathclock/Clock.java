package peter.mathclock;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.common.collect.ListMultimap;

import org.joda.time.LocalTime;

import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

class Clock {
    private final ViewItem hoursView;
    private final ViewItem minutesView;
    private final ViewItem secondsView;
    private final ListMultimap<Integer, Integer> mapping;
    private final Random random = new Random();

    public Clock(ImageView hoursView, ImageView minutesView, ImageView secondsView, ListMultimap<Integer, Integer> mapping) {
        this.hoursView = new ViewItem(hoursView);
        this.minutesView = new ViewItem(minutesView);
        this.secondsView = new ViewItem(secondsView);
        this.mapping = checkNotNull(mapping);
    }

    public void show(@NonNull LocalTime date) {
        show(hoursView, date.getHourOfDay());
        show(minutesView, date.getMinuteOfHour());
        show(secondsView, date.getSecondOfMinute());
    }

    private void show(ViewItem view, int n) {
        if (n != view.getLastNumber()) {
            List<Integer> closestList = ClosestSelector.getClosestList(mapping, n);
            int id;
            if (!closestList.isEmpty()) {
                id = closestList.get(random.nextInt(closestList.size()));
            } else {
                id = 0;
            }
            view.setLastNumber(n);
            view.setImageResource(id);
        }
    }

    private static class ViewItem {
        private final ImageView view;
        private int lastNumber = -1;

        public ViewItem(ImageView view) {
            this.view = checkNotNull(view);
        }

        public int getLastNumber() {
            return lastNumber;
        }

        public void setLastNumber(int lastNumber) {
            this.lastNumber = lastNumber;
        }

        public void setImageResource(int id) {
            view.setImageResource(id);
        }
    }
}
