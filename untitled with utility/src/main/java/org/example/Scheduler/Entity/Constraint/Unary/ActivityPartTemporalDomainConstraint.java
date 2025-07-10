package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.List;

@AllArgsConstructor
@Getter
public class ActivityPartTemporalDomainConstraint implements Constraint
{
    ActivitySWO activityToBeTested;

    @Override
    public boolean eval() {
        List<ActivityPartSWO> activityPartList = activityToBeTested.getParts();
        List<TemporalIntervalSWO> temporalIntervals = activityToBeTested.getF();
        for (ActivityPartSWO a : activityPartList)
        {
            boolean exists = false;
            for (TemporalIntervalSWO t : temporalIntervals)
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
