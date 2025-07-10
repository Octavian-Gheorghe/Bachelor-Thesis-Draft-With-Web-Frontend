package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class PartDurationRangeConstraint implements Constraint
{
    Activity activityToBeTested;

    @Override
    public boolean eval()
    {
        List<ActivityPart> activityPartList = activityToBeTested.getParts();
        Integer minimum = activityToBeTested.getSmini();
        Integer maximum = activityToBeTested.getSmaxi();
        for (ActivityPart a : activityPartList)
        {
            if(a.getDurij() > maximum || a.getDurij() < minimum)
                return false;
        }
        return true;
    }
}
