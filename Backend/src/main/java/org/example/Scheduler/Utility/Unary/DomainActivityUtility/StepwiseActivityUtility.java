package org.example.Scheduler.Utility.Unary.DomainActivityUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StepwiseActivityUtility implements DomainActivityUtility
{
    private ActivitySWO a;
    private int stepPoint;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts)
    {

        double utilSum = 0.0;
        int slots = 0;

        for (ActivityPartSWO p : parts)
        {
            for (int t = 0; t < p.getDurij(); t++)
            {
                int time = p.getTij() + t;
                utilSum += (time >= stepPoint ? 1.0 : 0.0);
                slots++;
            }
        }
        return slots == 0 ? 0.0 : utilSum / slots;
    }
}
