package org.example.Scheduler.Entity.Constraint.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class MaximumTemporalActivityDistanceConstraint implements Constraint {

    Activity a1;
    Activity a2;
    Integer dmax;

    @Override
    public boolean eval() {
        if (dmax == null)
            return true;

        List<ActivityPart> p1 = a1.getParts();
        List<ActivityPart> p2 = a2.getParts();

        for (ActivityPart part1 : p1)
        {
            for (ActivityPart part2 : p2)
            {
                int start1 = part1.getTij();
                int end1   = part1.calculateEndTime();
                int start2 = part2.getTij();
                int end2   = part2.calculateEndTime();

                // C12 – both conjuncts must hold
                boolean ok = (start1 + dmax <= end2) && (start2 + dmax <= end1);
                if (!ok)
                    return false;
            }
        }
        return true;
    }
}
