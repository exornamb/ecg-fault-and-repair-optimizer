package com.g15.dsa.algorithms.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GreedyAlgorithmsTest {

    @Test
    void activitySelectionChoosesMaximumCompatibleActivities() {

        List<ActivitySelection.Activity> activities =
                Arrays.asList(
                        new ActivitySelection.Activity("A", 1, 4),
                        new ActivitySelection.Activity("B", 3, 5),
                        new ActivitySelection.Activity("C", 0, 6),
                        new ActivitySelection.Activity("D", 5, 7),
                        new ActivitySelection.Activity("E", 3, 9),
                        new ActivitySelection.Activity("F", 5, 9),
                        new ActivitySelection.Activity("G", 6, 10),
                        new ActivitySelection.Activity("H", 8, 11)
                );

        List<ActivitySelection.Activity> selected =
                ActivitySelection.selectActivities(
                        activities
                );

        assertEquals(3, selected.size());

        assertEquals(
                "A",
                selected.get(0).getName()
        );

        assertEquals(
                "D",
                selected.get(1).getName()
        );

        assertEquals(
                "H",
                selected.get(2).getName()
        );
    }

    @Test
    void activitySelectionHandlesEmptyList() {

        List<ActivitySelection.Activity> activities =
                Collections.emptyList();

        List<ActivitySelection.Activity> selected =
                ActivitySelection.selectActivities(
                        activities
                );

        assertTrue(selected.isEmpty());
    }

    @Test
    void activitySelectionHandlesSingleActivity() {

        List<ActivitySelection.Activity> activities =
                Collections.singletonList(
                        new ActivitySelection.Activity(
                                "A",
                                1,
                                5
                        )
                );

        List<ActivitySelection.Activity> selected =
                ActivitySelection.selectActivities(
                        activities
                );

        assertEquals(1, selected.size());
        assertEquals(
                "A",
                selected.get(0).getName()
        );
    }

    @Test
    void activitySelectionRejectsInvalidTimeRange() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ActivitySelection.Activity(
                        "Invalid",
                        10,
                        5
                )
        );
    }

    @Test
    void activitySelectionAcceptsBackToBackActivities() {

        List<ActivitySelection.Activity> activities =
                Arrays.asList(
                        new ActivitySelection.Activity(
                                "A", 1, 3
                        ),
                        new ActivitySelection.Activity(
                                "B", 3, 5
                        ),
                        new ActivitySelection.Activity(
                                "C", 5, 7
                        )
                );

        List<ActivitySelection.Activity> selected =
                ActivitySelection.selectActivities(
                        activities
                );

        assertEquals(3, selected.size());
    }
}