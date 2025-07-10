package org.example.Scheduler.Entity.Constraint.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.DistanceMatrix;

import java.util.List;

@Getter
@AllArgsConstructor
public class MinimumTemporalActivityDistanceConstraint implements Constraint {

    ActivitySWO a1;
    ActivitySWO a2;
    Integer dmin;
    DistanceMatrix dm;

    @Override
    public boolean eval()
    {
        if (dmin == null)
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

                // C11 – must satisfy at least one of the two disjuncts
                boolean ok = (end1 + Math.max(dmin, dm.Dist(part1.getLij(), part2.getLij())) <= start2) || (end2 + Math.max(dmin, dm.Dist(part1.getLij(), part2.getLij())) <= start1);
                if (!ok) return false;
            }
        }
        return true;
    }
}