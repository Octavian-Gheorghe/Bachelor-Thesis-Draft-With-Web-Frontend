import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.Constraint.ActivityPartDurationConstraint;
import org.example.Entity.Constraint.ActivityMinimumTemporalDistanceConstraint;
import org.example.Entity.Constraint.Constraint;
import org.example.Entity.TemporalInterval;
import org.example.SWO.Constructor;
import org.example.SWO.Analyzer;
import org.example.SWO.Prioritizer;
import org.example.Entity.Schedule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        // Create a sample activity
        Activity a1 = new Activity(1, "name", 30, 10, 20, 10, 40, null, null);

        ActivityPart p1 = new ActivityPart(1, LocalTime.of(9, 0), 15);
        ActivityPart p2 = new ActivityPart(2, LocalTime.of(9, 30), 15);
        a1.setParts(List.of(p1, p2));

        // Constraints
        Constraint c1 = new ActivityPartDurationConstraint(a1);
        Constraint c2 = new ActivityMinimumTemporalDistanceConstraint(a1);

        // Check eval logic
        System.out.println("C1 (Duration Equal): " + c1.eval());
        System.out.println("C2 (Min Gap OK): " + c2.eval());
    }
}