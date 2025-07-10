package org.example.Scheduler.MetricCalculator;

import lombok.NoArgsConstructor;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.List;

@NoArgsConstructor
public class Metric3Calculator
{
    public static double m3(ActivitySWO act)
    {
        if (act == null)
            throw new IllegalArgumentException("activity is null");

        if(!act.getIsInterruptible())
            return 0;

        int sMin = verify(act.getSmini(), "smini");
        int sMax = verify(act.getSmaxi(), "smaxi");
        int dMin = verify(act.getDmini(), "dmini");
        int durMin = verify(act.getDurimin(), "durimin");

        List<TemporalIntervalSWO> domain = act.getF();
        if (domain == null)
            throw new IllegalArgumentException("Activity has no temporal domain (F)");
        if(domain.isEmpty())
            return 1.1;

        long scheduled = 0;

        for (TemporalIntervalSWO ti : domain)
        {
            int a = ti.getAi();
            int b = ti.getBi();
            if (a >= b)
                continue;
            scheduled += maxFillLength(b - a, sMin, sMax, dMin);
        }

        if (scheduled == 0L)
            return Double.POSITIVE_INFINITY;
        return durMin / (double) scheduled;
    }

    private static int verify(Integer value, String field)
    {
        if (value == null)
            throw new IllegalArgumentException("Activity." + field + " is null");
        if (value < 0)
            throw new IllegalArgumentException("Activity." + field + " must be ≥ 0");
        return value;
    }

    private static int maxFillLength(int len, int minDur, int maxDur, int dist)
    {
        int[] dp = new int[len + 1];

        for (int i = len; i >= 0; --i)
        {
            int best = 0;
            for (int d = minDur; d <= maxDur && i + d <= len; ++d)
            {
                int next = i + d + dist;
                int candidate = d + (next <= len ? dp[next] : 0);
                if (candidate > best)
                    best = candidate;
            }
            dp[i] = best;
        }
        return dp[0];
    }

}
