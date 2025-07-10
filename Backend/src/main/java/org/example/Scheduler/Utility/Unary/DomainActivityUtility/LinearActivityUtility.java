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
@Getter @Setter
public class LinearActivityUtility implements DomainActivityUtility
{
    private ActivitySWO a;
    private int earlyThreshold;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts)
    {
        if (parts == null || parts.isEmpty())
            return 0.0;

        int last = a.getF().get(a.getF().size() - 1).getBi();
        double utilSum = 0.0;
        int slots = 0;

        for (ActivityPartSWO p : parts)
            for (int t = 0; t < p.getDurij(); t++)
            {
                int time = p.getTij() + t;

                double u = (time < earlyThreshold) ? 0.0 : 1.0 - Math.min(1.0, (double)(time - earlyThreshold) / (last - earlyThreshold));

                utilSum += u;
                slots++;
            }
        return slots == 0 ? 0.0 : utilSum / slots;
    }
}
