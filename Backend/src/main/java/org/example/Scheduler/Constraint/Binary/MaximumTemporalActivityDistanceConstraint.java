package org.example.Scheduler.Constraint.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class MaximumTemporalActivityDistanceConstraint implements Constraint
{
    ActivitySWO a1;
    ActivitySWO a2;
    Integer dmax;

    @Override
    public boolean eval() {
        if (dmax == null)
            return true;

        List<ActivityPartSWO> p1 = a1.getParts();
        List<ActivityPartSWO> p2 = a2.getParts();

        for (ActivityPartSWO part1 : p1)
        {
            for (ActivityPartSWO part2 : p2)
            {
                int start1 = part1.getTij();
                int end1   = part1.calculateEndTime();
                int start2 = part2.getTij();
                int end2   = part2.calculateEndTime();

                boolean ok = (start1 + dmax <= end2) && (start2 + dmax <= end1);
                if (!ok)
                    return false;
            }
        }
        return true;
    }
}
