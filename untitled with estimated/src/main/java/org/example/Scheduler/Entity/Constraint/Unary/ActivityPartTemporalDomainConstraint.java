package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.TemporalInterval;

import java.util.List;

@AllArgsConstructor
@Getter
public class ActivityPartTemporalDomainConstraint implements Constraint
{
    Activity activityToBeTested;

    @Override
    public boolean eval() {
        List<ActivityPart> activityPartList = activityToBeTested.getParts();
        List<TemporalInterval> temporalIntervals = activityToBeTested.getF();
        for (ActivityPart a : activityPartList)
        {
            boolean exists = false;
            for (TemporalInterval t : temporalIntervals)
            {
                if (t.getAi() <= a.getTij() && a.calculateEndTime() <= t.getBi())
                    exists = true;
            }
            if (!exists)
                return false;
        }
        return true;
    }
}
