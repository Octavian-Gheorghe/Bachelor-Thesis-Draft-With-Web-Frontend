package org.example.Entity.Constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.TemporalInterval;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderingConstraint implements Constraint
{
    private Activity activityThatHappensBefore;
    private Activity activityThatHappensAfter;

    @Override
    public boolean eval()
    {
        //going strictly by definition
        for (ActivityPart partOfActivityThatHappensAfter : activityThatHappensAfter.getParts())
        {
            for (ActivityPart partOfActivityThatHappensBefore : activityThatHappensBefore.getParts())
            {
                if(partOfActivityThatHappensAfter.getStartTime().isBefore(partOfActivityThatHappensBefore.calculateEndTime()))
                    return false;
            }
        }
        return true;
    }

    @Override
    public List<TemporalInterval> propagate() {
        return List.of();
    }

    @Override
    public boolean involves(Activity a) {
        return activityThatHappensBefore.getId().equals(a.getId()) ||
                activityThatHappensAfter.getId().equals(a.getId());
    }
}
