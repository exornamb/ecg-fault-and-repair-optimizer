package algorithms.greedy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Greedy activity-selection algorithm for ECG Smart Dispatch.
 *
 * In the ECG dispatch context, an Activity represents a repair job
 * assigned to a crew. The start and finish values represent the
 * scheduled start time and expected completion time of that job.
 * The algorithm selects the maximum number of compatible repair jobs
 * by repeatedly choosing the job with the earliest finish time.
 *
 * This leaves the crew available as early as possible for subsequent
 * jobs while preserving the maximum number of sequential repairs.
 */

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
 * ECG dispatch greedy rule:
 * Each activity represents a repair/dispatch job assigned to a crew.
 * The start time represents when the crew can begin the job, while
 * the finish time represents the expected completion time.
 *
 * At each step, select the compatible repair job that finishes earliest.
 * Finishing the current job as early as possible leaves the crew
 * available sooner for subsequent repair jobs.
 *
 * This greedy strategy maximizes the number of non-overlapping
 * repair jobs that a crew can complete sequentially.
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