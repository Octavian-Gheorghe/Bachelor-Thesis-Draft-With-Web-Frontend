package org.example.Scheduler.Entity.Utility.Unary.PartDistanceUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class MaximumPartDistanceUtility implements PartDistanceUtility {

    private ActivitySWO activity;
    private int pdmax;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts)
    {
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
                ++total;
                int gap = parts.get(j).getTij() - parts.get(i).calculateEndTime();
                if (gap <= pdmax)
                    ++satisfied;
            }
        return (double) satisfied / total;
    }

    private static int getNetsize(List<TemporalIntervalSWO> F)
    {
        int sum = 0;
        if(!F.isEmpty())
        {
            for(TemporalIntervalSWO t : F)
            {
                sum += t.getWeight();
            }
            return sum;
        }
        else
            return 1;
    }

    @Override
    public double calculateActivityForBestCaseScenario(int totalDur)
    {
        if (totalDur < activity.getDurimin() || totalDur > activity.getDurimax())
            return 0.1;
        if(getNetsize(activity.getF()) < totalDur)
            return 0.1;

        int sMin = activity.getSmini(), sMax = activity.getSmaxi();
        int dMin = activity.getDmini(), dMax = activity.getDmaxi();

        List<TemporalIntervalSWO> domain = new ArrayList<>(activity.getF());

        for (int maxGap = pdmax; maxGap <= dMax; ++maxGap)
        {
            List<ActivityPartSWO> parts = trySchedule(domain, totalDur, sMin, sMax, dMin, dMax, maxGap);

            if (parts == null)
                continue;

            int worstGap = 0;
            for (int i = 1; i < parts.size(); i++)
            {
                System.out.println("For Maximum Distance Utility for activity " + activity.getName());
                System.out.println(parts.get(i-1));
                int gap = parts.get(i).getTij() - parts.get(i - 1).calculateEndTime();
                worstGap = Math.max(worstGap, gap);
            }
            System.out.println(parts.get(parts.size()-1));
            if (worstGap > maxGap)
                continue;

            return computeUtility(parts);
        }
        return 0.1;
    }

    private List<ActivityPartSWO> trySchedule(List<TemporalIntervalSWO> domain, int dur, int sMin, int sMax, int dMin, int dMax, int curMaxGap) {

        List<ActivityPartSWO> parts = new ArrayList<>();
        int remaining = dur, nextId = 1;

        Integer prevEnd = null;
        int idx = 0, cursor = domain.get(0).getAi();

        while (remaining > 0 && idx < domain.size())
        {
            if (prevEnd != null)
            {
                cursor = Math.max(cursor, prevEnd + dMin);
                if (cursor - prevEnd > curMaxGap)
                    return null;
                if (cursor - prevEnd > dMax)
                    return null;
            }

            TemporalIntervalSWO ti = domain.get(idx);
            if (cursor < ti.getAi())
                cursor = ti.getAi();

            if (cursor + sMin > ti.getBi())
            {
                idx++;
                continue;
            }

            int room = ti.getBi() - cursor;
            int partDur  = Math.min(sMax, Math.min(room, remaining));

            if (remaining - partDur > 0 && remaining - partDur < sMin)
                partDur -= (sMin - (remaining - partDur));
            if (partDur < sMin)
            {
                idx++;
                continue;
            }
            parts.add(new ActivityPartSWO(nextId++, cursor, partDur));
            remaining -= partDur;
            prevEnd = cursor + partDur;
            cursor = prevEnd + dMin;
        }
        return remaining == 0 ? parts : null;
    }
}