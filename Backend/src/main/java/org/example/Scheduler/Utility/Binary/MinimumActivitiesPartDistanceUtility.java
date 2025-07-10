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
public class MinimumActivitiesPartDistanceUtility implements BinaryUtility
{
    private ActivitySWO a;
    private ActivitySWO b;
    private int minDistance;

    @Override
    public double computeUtility(List<ActivityPartSWO> partsA, List<ActivityPartSWO> partsB)
    {
        int total = partsA.size() * partsB.size();
        if (total == 0)
            return 0.0;

        int satisfied = 0;
        for (ActivityPartSWO pa : partsA)
            for (ActivityPartSWO pb : partsB)
            {
                int start1 = pa.getTij();
                int end1 = pa.calculateEndTime();
                int start2 = pb.getTij();
                int end2 = pb.calculateEndTime();

                boolean ok = (end1 + minDistance <= start2) || (end2 + minDistance <= start1);
                if(ok)
                    ++satisfied;
            }
        return (double) satisfied / total;
    }
}