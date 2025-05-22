package org.example.Entity.Constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.TemporalInterval;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ActivityPartDurationConstraint implements Constraint //C1
{
    private final Activity activity;

    @Override
    public boolean eval()
    {
        int sum = activity.getParts()
                .stream()
                .mapToInt(ActivityPart::getDuration)
                .sum();
        return sum == activity.getDuration();
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