package org.example.Scheduler.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;

import java.util.List;
import java.util.Objects;

/*Pseudo Repository*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSWO
{
    private List<ActivitySWO> activities;

    public void addAnotherActivity(ActivitySWO activity)
    {
        activities.add(activity);
    }

    public void removeActivity(Integer id)
    {
        activities.removeIf(activityThatHasId -> Objects.equals(activityThatHasId.getId(), id));
    }

    public boolean searchForActivity(Integer id)
    {
        for(ActivitySWO activityThatHasId : activities)
        {
            if(activityThatHasId.getId().equals(id))
                return true;
        }
        return false;
    }

    public void removeUnusableIntervals()
    {
        for(ActivitySWO activityToBeCleaned : activities)
        {
            activityToBeCleaned.cleanUselessIntervals();
        }
    }

    public ActivitySWO getViaId(int id)
    {
        for(ActivitySWO activity : activities)
        {
            if(activity.getId() == id)
                return activity;
        }
        return null;
    }

    public void determineDifficultiesOfActivities()
    {
        for(ActivitySWO activity : activities)
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
        for (ActivitySWO a : activities)
        {
            a.setTroublemaker(false);
        }
    }
}
