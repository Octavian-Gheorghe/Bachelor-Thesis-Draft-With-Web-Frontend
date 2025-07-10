package org.example.Scheduler.Entity.Constraint.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderingConstraint implements Constraint
{
    Activity activityToBeTested1; //before
    Activity activityToBeTested2; //after

    @Override
    public boolean eval()
    {
        List<ActivityPart> listOfActivityParts1 = activityToBeTested1.getParts();
        List<ActivityPart> listOfActivityParts2 = activityToBeTested2.getParts();
        for(ActivityPart a1 : listOfActivityParts1)
        {
            for(ActivityPart a2 : listOfActivityParts2)
            {
                if(a1.calculateEndTime() > a2.getTij())
                    return false;
            }
        }
        return true;
    }
}
