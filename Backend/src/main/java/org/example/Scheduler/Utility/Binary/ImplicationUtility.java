package org.example.Scheduler.Utility.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ImplicationUtility implements BinaryUtility
{
    ActivitySWO a1;
    ActivitySWO a2;

    @Override
    public double computeUtility(List<ActivityPartSWO> ap1, List<ActivityPartSWO> ap2)
    {
        if(a1.getDuri() > 0 && !ap1.isEmpty())
        {
            if(a2.getDuri() > 0 && !ap2.isEmpty())
                return 1.0;
        }
        return 0.0;
    }
}
