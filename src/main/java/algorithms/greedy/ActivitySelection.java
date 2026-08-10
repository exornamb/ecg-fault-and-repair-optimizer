package algorithms.greedy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ActivitySelection {

    public static class Activity {

        private final String name;
        private final int start;
        private final int finish;

        public Activity(
                String name,
                int start,
                int finish) {

            if (start > finish) {
                throw new IllegalArgumentException(
                        "Start time cannot be after finish time"
                );
            }

            this.name = name;
            this.start = start;
            this.finish = finish;
        }

        public String getName() {
            return name;
        }

        public int getStart() {
            return start;
        }

        public int getFinish() {
            return finish;
        }

        @Override
        public String toString() {
            return name +
                    " (" +
                    start +
                    "-" +
                    finish +
                    ")";
        }
    }

    public static List<Activity> selectActivities(
            List<Activity> activities) {

        if (activities == null) {
            throw new IllegalArgumentException(
                    "Activities cannot be null"
            );
        }

        List<Activity> sorted =
                new ArrayList<>(activities);

        /*
         * Greedy rule:
         * Always choose the activity
         * that finishes earliest.
         */
        sorted.sort(
                Comparator.comparingInt(
                        Activity::getFinish
                )
        );

        List<Activity> selected =
                new ArrayList<>();

        int lastFinish =
                Integer.MIN_VALUE;

        for (Activity activity :
                sorted) {

            if (activity.getStart()
                    >= lastFinish) {

                selected.add(activity);

                lastFinish =
                        activity.getFinish();
            }
        }

        return selected;
    }
}