package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class ActivityPartDurationConstraint implements Constraint
{
    ActivitySWO activityToBeTested;

    @Override
    public boolean eval()
    {
        List<ActivityPartSWO> activityPartList = activityToBeTested.getParts();
        Integer durationOfParts = 0;
        for (ActivityPartSWO a : activityPartList)
        {
            durationOfParts+=a.getDurij();
        }
        return durationOfParts.equals(activityToBeTested.getDuri());
    }
}
