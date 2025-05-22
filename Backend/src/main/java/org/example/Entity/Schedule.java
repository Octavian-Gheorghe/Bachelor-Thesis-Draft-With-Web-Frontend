package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Entity.Activity.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*Pseudo Repository*/
@Getter
@Setter
@AllArgsConstructor
public class Schedule
{
    private List<Activity> activities;

    public void addAnotherActivity(Activity activity)
    {
        activities.add(activity);
    }

    public void removeActivity(Integer id)
    {
        activities.removeIf(activityThatHasId -> Objects.equals(activityThatHasId.getId(), id));
    }

    public boolean searchForActivity(Integer id)
    {
        for(Activity activityThatHasId : activities)
        {
            if(activityThatHasId.getId().equals(id))
                return true;
        }
        return false;
    }

    public void removeUnusableIntervals()
    {
        for(Activity activityToBeCleaned : activities)
        {
            activityToBeCleaned.cleanUselessIntervals();
        }
    }

    public List<Double> determineDifficultiesOfActivities()
    {
        List<Double> difficulties = new ArrayList<>();
        for(Activity activity : activities)
        {
            double difficulty = activity.calculateInitialDifficulty();
            if(difficulty > 1)
                return null;
            difficulties.add(difficulty);
        }
        return difficulties;
    }

    public void sortActivitiesByDifficulty()
    {
        for (Activity activity : activities) {
            if (activity.calculateInitialDifficulty() > 1) {
                activities.clear();
                return;
            }
        }

        activities.sort((a1, a2) -> {
            double diff1 = a1.calculateInitialDifficulty();
            double diff2 = a2.calculateInitialDifficulty();
            return Double.compare(diff1, diff2);
        });
    }

    public void setActivitiesAsNonTroublemakers()
    {
        for (Activity a : activities)
        {
            a.setTroublemaker(false);
        }
    }
}
