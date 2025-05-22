package org.example.Entity.Constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.TemporalInterval;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ActivityMinimumTemporalDistanceConstraint implements Constraint //C3
{
    Activity activity;

    @Override
    public boolean eval()
    {
        List<ActivityPart> listOfActivityParts = activity.getParts();
        for(int i = 0; i < listOfActivityParts.size()-1 ; i++)
        {
            ActivityPart activityPart1 = listOfActivityParts.get(i);
            ActivityPart activityPart2 = listOfActivityParts.get(i+1);
            long minutesBetween =
                    Math.abs(Duration.between(
                            activityPart2.getStartTime(),
                            activityPart1.calculateEndTime()
                    ).toMinutes());
            if(minutesBetween < activity.getMinGapBetweenParts())
                return false;
        }
        return true;
    }

    @Override
    public List<TemporalInterval> propagate()
    {
        return List.of();
    }

    @Override
    public boolean involves(Activity a) {
        return this.activity.getId().equals(a.getId());
    }
}
