package org.example.Scheduler.Entity.Constraint.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Constraint;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderingConstraint implements Constraint
{
    ActivitySWO activityToBeTested1; //before
    ActivitySWO activityToBeTested2; //after

    @Override
    public boolean eval()
    {
        List<ActivityPartSWO> listOfActivityParts1 = activityToBeTested1.getParts();
        List<ActivityPartSWO> listOfActivityParts2 = activityToBeTested2.getParts();
        for(ActivityPartSWO a1 : listOfActivityParts1)
        {
            for(ActivityPartSWO a2 : listOfActivityParts2)
            {
                if(a1.calculateEndTime() > a2.getTij())
                    return false;
            }
        }
        return true;
    }
}
