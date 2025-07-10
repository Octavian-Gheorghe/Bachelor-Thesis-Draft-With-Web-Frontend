package org.example.Scheduler.Entity.Utility.Unary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityDurationUtility implements UnaryUtility
{
    Activity a;
    Integer durMin;
    Integer durMax;
    Double uLow;
    Double uHigh;

    @Override
    public double computeUtility(List<ActivityPart> parts)
    {
        Integer dur = 0;
        for(ActivityPart a : parts)
        {
            dur += a.getDurij();
        }
        //System.out.println(dur);
        if (dur < durMin)
        {
            return 0.0;
        }
        else if (dur <= durMax)
        {
            if(durMin == durMax && dur == durMin)
                return uLow + (uHigh - uLow);
            double fraction = (double) (dur - durMin) / (durMax - durMin);
            return uLow + fraction * (uHigh - uLow);
        }
        else
        {
            return (double) 0.0;
        }
    }
}
