package org.example.Scheduler.Entity.MetricCalculator;

import lombok.NoArgsConstructor;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.List;

@NoArgsConstructor

public class Metric2Calculator
{
    public static double m2(ActivitySWO act)
    {
        if (act == null)
            throw new IllegalArgumentException("activity is null");
        if(!act.isInterruptible)
            return 0;
        int sMax  = nvl(act.getSmaxi(), "smaxi");
        int dMin  = nvl(act.getDmini(), "dmini");
        int durMin = nvl(act.getDurimin(), "durimin");
        if(act.getF() == null)
            throw new IllegalArgumentException("time interval domain is null");

        int min_parts = durMin/sMax + (durMin%sMax != 0 ? 1 : 0);
        int lower_bound_makespan = durMin + (min_parts-1) * dMin;
        return (double) lower_bound_makespan/getWidth(act.getF());
    }

    private static int nvl(Integer value, String field)
    {
        if (value == null)
            throw new IllegalArgumentException("Activity." + field + " is null");
        if (value < 0)
            throw new IllegalArgumentException("Activity." + field + " must be ≥ 0");
        return value;
    }

    private static int getWidth(List<TemporalIntervalSWO> F)
    {
        if(!F.isEmpty())
        {
            int amin = F.get(0).getAi();
            int bmax = F.get(F.size()-1).getBi();
            return bmax - amin;
        }
        else
            return 1;
    }
}
