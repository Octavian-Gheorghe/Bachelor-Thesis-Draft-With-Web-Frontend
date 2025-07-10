package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class MaximumTemporalPartDistanceConstraint implements Constraint
{
    Activity activityToBeTested;
    int dmax;

    @Override
    public boolean eval()
    {
        List<ActivityPart> activityPartList = activityToBeTested.getParts();
        Integer maximum = dmax;
        if(activityPartList.size() == 1)
            return true;

        for(int i = 1; i < activityPartList.size() ; i++)
        {
            ActivityPart a1 = activityPartList.get(i-1);
            ActivityPart a2 = activityPartList.get(i);
            if(a2.getTij() - a1.calculateEndTime() > maximum)
                return false;
        }
        return true;
    }
}