package org.example.Entity.Constraint;

import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.TemporalInterval;

import java.time.LocalTime;
import java.util.List;

public class ActivityPartsFitInTemporalIntervalsConstraint implements Constraint //C4
{
    Activity activity;

    @Override
    public boolean eval()
    {
        for(ActivityPart activityPart : activity.getParts())
        {
            LocalTime startTime = activityPart.getStartTime();
            LocalTime endTime = activityPart.calculateEndTime();
            boolean itFits = false;

            for(TemporalInterval temporalInterval : activity.getIntervals())
            {
                if(temporalInterval.getStartTime().isBefore(startTime) && temporalInterval.getEndTime().isAfter(endTime))
                    itFits = true;
            }

            if(!itFits)
                return false;
        }
        return true;
    }

    @Override
    public List<TemporalInterval> propagate() {
        return List.of();
    }

    @Override
    public boolean involves(Activity a)
    {
        return this.activity.getId().equals(a.getId());
    }
}
