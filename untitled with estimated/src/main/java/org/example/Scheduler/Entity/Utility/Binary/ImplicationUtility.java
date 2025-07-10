package org.example.Scheduler.Entity.Utility.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ImplicationUtility implements BinaryUtility
{

    Activity a1;
    Activity a2;

    @Override
    public double computeUtility(List<ActivityPart> ap1, List<ActivityPart> ap2)
    {
        if(a1.getDuri() > 0 && !ap1.isEmpty())
        {
            if(a2.getDuri() > 0 && !ap2.isEmpty())
                return 1.0;
        }
        return 0.0;
    }
}
