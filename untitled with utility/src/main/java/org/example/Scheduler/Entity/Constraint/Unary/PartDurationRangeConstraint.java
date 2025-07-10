package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class PartDurationRangeConstraint implements Constraint
{
    ActivitySWO activityToBeTested;

    @Override
    public boolean eval()
    {
        List<ActivityPartSWO> activityPartList = activityToBeTested.getParts();
        Integer minimum = activityToBeTested.getSmini();
        Integer maximum = activityToBeTested.getSmaxi();
        for (ActivityPartSWO a : activityPartList)
        {
            if(a.getDurij() > maximum || a.getDurij() < minimum)
                return false;
        }
        return true;
    }
}
