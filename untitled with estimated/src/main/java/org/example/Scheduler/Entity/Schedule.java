package org.example.Scheduler.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.Activity;

import java.util.List;
import java.util.Objects;

/*Pseudo Repository*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public Activity getViaId(int id)
    {
        for(Activity activity : activities)
        {
            if(activity.getId() == id)
                return activity;
        }
        return null;
    }

    public void determineDifficultiesOfActivities()
    {
        for(Activity activity : activities)
        {
            activity.calculateDifficulty();
        }
    }

    public void sortActivitiesByDifficulty()
    {
        activities.sort((a1, a2) -> {
            double diff1 = a1.getDifficulty();
            double diff2 = a2.getDifficulty();
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
