package org.example.Scheduler.Utility.Unary.PartDistanceUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MinimumPartDistanceUtility implements PartDistanceUtility
{
    private ActivitySWO activity;
    private int pdmin;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts) {

        if (parts == null)
            return 0.1;
        if(parts.isEmpty())
            return 0.1;
        if(parts.size() == 1)
            return 1.0;

        int satisfied = 0, total = 0;

        for (int i = 0; i < parts.size(); i++)
            for (int j = i + 1; j < parts.size(); j++)
            {
                total++;
                int gap = parts.get(j).getTij() - parts.get(i).calculateEndTime();
                if (gap >= pdmin)
                    satisfied++;
            }
        return (double) satisfied / total;
    }

}