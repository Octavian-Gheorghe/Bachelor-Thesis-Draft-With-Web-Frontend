package org.example.Scheduler.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class MaximumTemporalPartDistanceConstraint implements Constraint
{
    ActivitySWO activityToBeTested;
    int dmax;

    @Override
    public boolean eval()
    {
        List<ActivityPartSWO> activityPartList = activityToBeTested.getParts();
        int maximum = dmax;
        if(activityPartList.size() == 1)
            return true;

        for(int i = 1; i < activityPartList.size() ; i++)
        {
            ActivityPartSWO a1 = activityPartList.get(i-1);
            ActivityPartSWO a2 = activityPartList.get(i);
            if(a2.getTij() - a1.calculateEndTime() > maximum)
                return false;
        }
        return true;
    }
}