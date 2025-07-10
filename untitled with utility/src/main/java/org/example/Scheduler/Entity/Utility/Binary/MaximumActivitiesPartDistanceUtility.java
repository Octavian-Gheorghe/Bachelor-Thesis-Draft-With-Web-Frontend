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
public class MaximumActivitiesPartDistanceUtility implements  BinaryUtility {

    private ActivitySWO a;
    private ActivitySWO b;
    private int maxDistance;

    public double calculateActivityForBestCaseScenario(int durA, int durB)
    {
        List<TemporalIntervalSWO> domA = new ArrayList<>(a.getF());
        List<TemporalIntervalSWO> domB = new ArrayList<>(b.getF());

        double best = 0.0;

        for (int cutA = 0; cutA < domA.size(); ++cutA)
        {
            List<TemporalIntervalSWO> subA = domA.subList(cutA, domA.size());
            for (int cutB = 0; cutB < domB.size(); ++cutB)
            {
                List<TemporalIntervalSWO> subB = domB.subList(cutB, domB.size());

                List<ActivityPartSWO> partsA = buildSchedule(a, subA, durA, 0);
                if (partsA == null)
                    continue;

                int lastEndA = partsA.get(partsA.size() - 1).calculateEndTime();

                TemporalIntervalSWO firstB = subB.isEmpty() ? null : subB.get(0);
                for (int startB = lastEndA; firstB != null && startB + b.getSmini() <= firstB.getBi(); ++startB)
                {
                    List<ActivityPartSWO> partsB = buildSchedule(b, subB, durB, startB);

                    if (partsB == null)
                        continue;
                    if (overlaps(partsA, partsB))
                        continue;


//    / /                    for(ActivityPart ap1 : partsA)
//    / /                        System.out.println(ap1);
//    / /                    System.out.println();
//    / /                    for(ActivityPart ap1 : partsB)
//    / /                        System.out.println(ap1);


                    best = Math.max(best, computeUtility(partsA, partsB));
                    if (best == 1.0)
                        return 1.0;
                }
            }
        }
        return best == 0.0 ? 0.1 : best;
    }
    @Override
    public double computeUtility(List<ActivityPartSWO> pA, List<ActivityPartSWO> pB) {

        int total = pA.size() * pB.size();
        if (total == 0)
            return 0.1;

        int ok = 0;
        for (ActivityPartSWO pa : pA)
            for (ActivityPartSWO pb : pB) {
                int gap = Math.abs(pb.getTij() - pa.calculateEndTime());
                if (gap <= maxDistance)
                    ++ok;
            }
        return (double) ok / total;
    }


    public List<ActivityPartSWO> buildSchedule(ActivitySWO proto, List<TemporalIntervalSWO> domain, int targetDur, int earliestStart)
    {
        int sMin = proto.getSmini(), sMax = proto.getSmaxi();
        int dMin = proto.getDmini(), dMax = proto.getDmaxi();

        List<ActivityPartSWO> parts = new ArrayList<>();
        int remaining = targetDur, id = 1;
        Integer prevEnd = null;

        for (TemporalIntervalSWO ti : domain) {

            // --- changed line ---
            int cursor = Math.max(
                    prevEnd == null ? ti.getAi()
                            : prevEnd + dMin,
                    Math.max(ti.getAi(), earliestStart));

            while (remaining > 0 && cursor + sMin <= ti.getBi()) {

                int dur = Math.min(Math.min(sMax, ti.getBi() - cursor), remaining);

                if (remaining - dur > 0 && remaining - dur < sMin)
                    dur -= (sMin - (remaining - dur));
                if (dur < sMin) break;

                if (prevEnd != null && cursor - prevEnd > dMax) return null;

                parts.add(new ActivityPartSWO(id++, cursor, dur));
                remaining -= dur;
                prevEnd = cursor + dur;
                cursor  = prevEnd + dMin;
            }
            if (remaining == 0) break;
        }
        return remaining == 0 ? parts : null;
    }

    private boolean overlaps(List<ActivityPartSWO> aParts, List<ActivityPartSWO> bParts) {
        for (ActivityPartSWO pa : aParts) {
            int aStart = pa.getTij(), aEnd = pa.calculateEndTime();
            for (ActivityPartSWO pb : bParts) {
                int bStart = pb.getTij(), bEnd = pb.calculateEndTime();
                if (aStart < bEnd && bStart < aEnd) return true;
            }
        }
        return false;
    }
}
