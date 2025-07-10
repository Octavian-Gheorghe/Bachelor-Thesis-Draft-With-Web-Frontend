package org.example.Scheduler.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Constraint.Constraint;

@AllArgsConstructor
@Getter
public class DurationRangeConstraint implements Constraint
{
    ActivitySWO activityToBeTested;

    @Override
    public boolean eval()
    {
        return activityToBeTested.getDuri() >= activityToBeTested.getDurimin() && activityToBeTested.getDuri() <= activityToBeTested.getDurimax();
    }
}
