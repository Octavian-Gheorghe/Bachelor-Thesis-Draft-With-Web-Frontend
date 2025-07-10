package org.example.Scheduler.Utility.Binary;

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
public class MaximumActivitiesPartDistanceUtility implements  BinaryUtility
{
    private ActivitySWO a;
    private ActivitySWO b;
    private int maxDistance;

    @Override
    public double computeUtility(List<ActivityPartSWO> pA, List<ActivityPartSWO> pB) {

        int total = pA.size() * pB.size();
        if (total == 0)
            return 0.1;

        int ok = 0;
        for (ActivityPartSWO pa : pA)
            for (ActivityPartSWO pb : pB)
            {
                int gap = Math.abs(pb.getTij() - pa.calculateEndTime());
                if (gap <= maxDistance)
                    ++ok;
            }
        return (double) ok / total;
    }
}
