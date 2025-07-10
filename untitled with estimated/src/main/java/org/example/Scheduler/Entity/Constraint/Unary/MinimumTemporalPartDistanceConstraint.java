package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.DistanceMatrix;

import java.util.List;

@AllArgsConstructor
@Getter
public class MinimumTemporalPartDistanceConstraint implements Constraint
{
    Activity activityToBeTested;
    DistanceMatrix dm;
    int dmin;

    @Override
    public boolean eval()
    {
        List<ActivityPart> activityPartList = activityToBeTested.getParts();
        int minimum = dmin;
        if(activityPartList.size() == 1)
            return true;

        for(int i = 1; i < activityPartList.size() ; i++)
        {
            ActivityPart a1 = activityPartList.get(i-1);
            ActivityPart a2 = activityPartList.get(i);
            if(a2.getTij() - a1.calculateEndTime() < Math.max(minimum, dm.Dist(a1.getLij(), a2.getLij())))
                return false;
        }
        return true;
    }
}
