package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class ActivityPartDurationConstraint implements Constraint
{
    Activity activityToBeTested;

    @Override
    public boolean eval()
    {
        List<ActivityPart> activityPartList = activityToBeTested.getParts();
        Integer durationOfParts = 0;
        for (ActivityPart a : activityPartList)
        {
            durationOfParts+=a.getDurij();
        }
        return durationOfParts.equals(activityToBeTested.getDuri());
    }
}
