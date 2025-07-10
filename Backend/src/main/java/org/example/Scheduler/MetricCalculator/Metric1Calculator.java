package org.example.Scheduler.MetricCalculator;

import lombok.NoArgsConstructor;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.List;

@NoArgsConstructor
public class Metric1Calculator
{
    public static double m1(ActivitySWO act)
    {
        if (act == null)
            throw new IllegalArgumentException("activity is null");

        int durMin = verify(act.getDurimin());

        List<TemporalIntervalSWO> domain = act.getF();
        if (domain == null)
            throw new IllegalArgumentException("Activity has no temporal domain (F)");

        if(domain.isEmpty())
            return 1.1;

        return (double) durMin/getNetsize(domain);
    }

    private static int getNetsize(List<TemporalIntervalSWO> F)
    {
        int sum = 0;
        if(!F.isEmpty())
        {
            for(TemporalIntervalSWO t : F)
            {
                sum += t.getWeight();
            }
            return sum;
        }
        else
            return 1;
    }

    private static int verify(Integer value)
    {
        if (value == null)
            throw new IllegalArgumentException("Activity." + "durimin" + " is null");
        if (value < 0)
            throw new IllegalArgumentException("Activity." + "durimin" + " must be ≥ 0");
        return value;
    }
}
