package org.example.SWO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Entity.Activity.Activity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Analyzer
{
    public List<Activity> activities;

    public List<Activity> identifyTroublemakers()
    {
        List<Activity> troublemakers = new ArrayList<>();

        for (Activity activity : activities)
        {
            if (activity.isTroublemaker())
            {
                troublemakers.add(activity);
            }
        }

        return troublemakers;
    }
}