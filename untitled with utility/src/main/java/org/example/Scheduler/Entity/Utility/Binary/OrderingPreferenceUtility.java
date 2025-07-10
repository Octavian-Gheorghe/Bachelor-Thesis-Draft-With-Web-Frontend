package org.example.Scheduler.Entity.Utility.Binary;


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
public class OrderingPreferenceUtility implements BinaryUtility {

    private ActivitySWO a;
    private ActivitySWO b;

    public double calculateActivityForBestCaseScenario(int durA, int durB)
    {
        List<ActivityPartSWO> partsA = buildForward (a, durA);
        if (partsA == null)
            return 0.1;

        int lastEndA = partsA.get(partsA.size() - 1).calculateEndTime();

        List<ActivityPartSWO> partsB = buildBackward(b, durB, lastEndA + a.getDmini());
        if (partsB == null)
            return 0.1;

        return computeUtility(partsA, partsB);
    }


    @Override
    public double computeUtility(List<ActivityPartSWO> aParts, List<ActivityPartSWO> bParts) {

        int total = aParts.size() * bParts.size();
        if (total == 0)
            return 0.0;

        int satisfied = 0;
        for (ActivityPartSWO pa : aParts)
            for (ActivityPartSWO pb : bParts)
                if (pa.getTij() < pb.getTij()) ++satisfied;

        return (double) satisfied / total;
    }


    private static List<ActivityPartSWO> buildForward(ActivitySWO act, int targetDur) {

        List<TemporalIntervalSWO> domain = new ArrayList<>(act.getF());
        int sMin = act.getSmini(), sMax = act.getSmaxi(), dMin = act.getDmini();

        List<ActivityPartSWO> parts = new ArrayList<>();
        int remaining = targetDur, id = 1;

        for (TemporalIntervalSWO ti : domain) {

            int cursor = ti.getAi();

            while (remaining > 0 && cursor + sMin <= ti.getBi()) {

                int partDur = Math.min(Math.min(sMax, ti.getBi() - cursor), remaining);

                if (remaining - partDur > 0 && remaining - partDur < sMin)
                    partDur -= (sMin - (remaining - partDur));
                if (partDur < sMin) break;

                parts.add(new ActivityPartSWO(id++, cursor, partDur));
                remaining -= partDur;
                cursor    += partDur + dMin;
            }
            if (remaining == 0) break;
        }
        return remaining == 0 ? parts : null;
    }

    private static List<ActivityPartSWO> buildBackward(ActivitySWO act, int targetDur, int notBefore)
    {

        List<TemporalIntervalSWO> domain = new ArrayList<>(act.getF());
        int sMin = act.getSmini(), sMax = act.getSmaxi(), dMin = act.getDmini();

        List<ActivityPartSWO> parts = new ArrayList<>();
        int remaining = targetDur, id = 1;

        for (int idx = domain.size() - 1; idx >= 0 && remaining > 0; --idx) {

            TemporalIntervalSWO ti = domain.get(idx);
            int cursorEnd = ti.getBi();

            if (cursorEnd < notBefore) break;

            while (remaining > 0 && cursorEnd - sMin >= Math.max(ti.getAi(), notBefore)) {

                int maxDur  = Math.min(Math.min(sMax, cursorEnd - ti.getAi()), remaining);
                int partDur = maxDur;

                if (remaining - partDur > 0 && remaining - partDur < sMin)
                    partDur -= (sMin - (remaining - partDur));
                if (partDur < sMin) break;

                int start = cursorEnd - partDur;
                parts.add(0, new ActivityPartSWO(id++, start, partDur));

                remaining -= partDur;
                cursorEnd  = start - dMin;
            }
        }
        return remaining == 0 ? parts : null;
    }
}