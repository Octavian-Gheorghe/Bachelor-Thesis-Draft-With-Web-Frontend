package org.example.Scheduler.Entity.Constraint.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Constraint.Constraint;

@AllArgsConstructor
@Getter
public class DurationRangeConstraint implements Constraint
{
    Activity activityToBeTested;

    @Override
    public boolean eval()
    {
        return activityToBeTested.getDuri() >= activityToBeTested.getDurimin() && activityToBeTested.getDuri() <= activityToBeTested.getDurimax();
    }
}
