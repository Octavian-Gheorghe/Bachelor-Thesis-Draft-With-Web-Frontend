package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.DistanceMatrix;

import java.util.List;

@AllArgsConstructor
@Getter
public class MinimumTemporalPartDistanceConstraint implements Constraint
{
    ActivitySWO activityToBeTested;
    DistanceMatrix dm;
    int dmin;

    @Override
    public boolean eval()
    {
        List<ActivityPartSWO> activityPartList = activityToBeTested.getParts();
        int minimum = dmin;
        if(activityPartList.size() == 1)
            return true;

        for(int i = 1; i < activityPartList.size() ; i++)
        {
            ActivityPartSWO a1 = activityPartList.get(i-1);
            ActivityPartSWO a2 = activityPartList.get(i);
            if(a2.getTij() - a1.calculateEndTime() < Math.max(minimum, dm.Dist(a1.getLij(), a2.getLij())))
                return false;
        }
        return true;
    }
}
