package org.example.Entity.Constraint;

import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.TemporalInterval;

import java.util.List;

public class AllowedPartDurationConstraint implements Constraint //C2
{
    Activity activity;

    @Override
    public boolean eval()
    {
        for(ActivityPart activityPart : activity.getParts())
        {
            if(activityPart.getDuration() < activity.getMinPartDuration() || activityPart.getDuration() > activity.getMaxPartDuration())
                return false;
        }
        return true;
    }

    @Override
    public List<TemporalInterval> propagate() {
        return List.of();
    }

    @Override
    public boolean involves(Activity a) {
        return this.activity.getId().equals(a.getId());
    }
}
