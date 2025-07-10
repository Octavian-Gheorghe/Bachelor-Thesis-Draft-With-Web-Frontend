package org.example.Scheduler.Utility.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityDurationUtility implements UnaryUtility
{
    ActivitySWO a;
    Integer durMin;
    Integer durMax;
    Double uLow;
    Double uHigh;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts)
    {
        Integer dur = 0;
        for(ActivityPartSWO a : parts)
        {
            dur += a.getDurij();
        }
        if (dur < durMin)
        {
            return 0.0;
        }
        else if (dur <= durMax)
        {
            if(durMin.equals(durMax))
                return uLow + (uHigh - uLow);
            double fraction = (double) (dur - durMin) / (durMax - durMin);
            return uLow + fraction * (uHigh - uLow);
        }
        else
        {
            return 0.0;
        }
    }
}
