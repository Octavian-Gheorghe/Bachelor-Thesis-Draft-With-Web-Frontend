package org.example.SWO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Entity.Activity.Activity;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prioritizer
{

    public List<Activity> activities;

    public List<Activity> promoteTroublemakers(List<Activity> troublemakers)
    {
        for (Activity troublemaker : troublemakers)
        {
            if (activities.remove(troublemaker))
            {
                activities.add(0, troublemaker);
            }
        }
        return activities;
    }
}