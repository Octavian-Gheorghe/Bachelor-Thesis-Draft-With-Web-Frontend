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
public class OrderingPreferenceUtility implements BinaryUtility
{
    private ActivitySWO a;
    private ActivitySWO b;

    @Override
    public double computeUtility(List<ActivityPartSWO> aParts, List<ActivityPartSWO> bParts) {

        int total = aParts.size() * bParts.size();
        if (total == 0)
            return 0.0;

        int satisfied = 0;
        for (ActivityPartSWO pa : aParts)
            for (ActivityPartSWO pb : bParts)
                if (pa.getTij() < pb.getTij()) ++satisfied;

        return (double) satisfied / total;
    }
}