package org.example.Scheduler.Entity.Utility.Unary.PartDistanceUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.TemporalInterval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MinimumPartDistanceUtility implements PartDistanceUtility {

    private Activity activity;
    private int pdmin;

    @Override
    public double computeUtility(List<ActivityPart> parts) {

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
                //System.out.println(parts.get(i) + " " + parts.get(j));
                total++;
                int gap = parts.get(j).getTij() - parts.get(i).calculateEndTime();
                if (gap >= pdmin)
                    satisfied++;
            }
        return (double) satisfied / total;
    }

    @Override
    public double calculateActivityForBestCaseScenario(int totalDur)
    {
        if (totalDur < activity.getDurimin() || totalDur > activity.getDurimax())
            return 0.1;

        List<TemporalInterval> domain = new ArrayList<>(activity.getF());
        int sMin = activity.getSmini();
        int sMax = activity.getSmaxi();
        int dMin = activity.getDmini();
        int dMax = activity.getDmaxi();

        for (int gap = pdmin; gap >= dMin; --gap) {
            if (gap > dMax)
                continue;

            for (int dur = totalDur; dur >= activity.getDurimin(); --dur)
            {
                List<ActivityPart> candidate = trySchedule(domain, dur, gap, sMin, sMax, dMin, dMax);
                if (candidate != null)
                {
                    System.out.println("For Minimum Distance Utility...");
                    for(ActivityPart a : candidate)
                        System.out.println(a);
                    return computeUtility(candidate);
                }

            }
        }
        return 0.1;
    }

    private List<ActivityPart> trySchedule(List<TemporalInterval> domain, int totalDur, int gap, int sMin, int sMax, int dMin, int dMax)
    {
        List<ActivityPart> parts = new ArrayList<>();
        int remaining = totalDur;
        int nextId = 1;
        Integer prevEnd = null;

        int idx = 0;
        int cursor = domain.get(0).getAi();

        while (remaining > 0 && idx < domain.size()) {
            if (prevEnd != null) {
                cursor = Math.max(cursor, prevEnd + gap);
                if (cursor - prevEnd > dMax) return null;
            }

            TemporalInterval ti = domain.get(idx);
            if (cursor < ti.getAi()) cursor = ti.getAi();
            if (cursor + sMin > ti.getBi()) {
                idx++;
                continue;
            }

            int maxFit = ti.getBi() - cursor;
            int partSize = Math.min(Math.min(sMax, maxFit), remaining);

            if (remaining - partSize > 0 && remaining - partSize < sMin)
                partSize -= sMin - (remaining - partSize);

            if (partSize < sMin) {
                idx++;
                continue;
            }

            parts.add(new ActivityPart(nextId++, cursor, partSize));
            remaining -= partSize;
            prevEnd = cursor + partSize;
            cursor = prevEnd + gap;
        }

        if (remaining > 0) return null;
        parts.sort(Comparator.comparingInt(ActivityPart::getTij));
        return parts;
    }
}