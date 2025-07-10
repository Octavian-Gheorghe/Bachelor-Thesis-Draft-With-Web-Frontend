package org.example.Scheduler.Entity.Utility.Binary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.TemporalInterval;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class MaximumActivitiesPartDistanceUtility implements  BinaryUtility {

    private Activity a;
    private Activity b;
    private int maxDistance;

    public double calculateActivityForBestCaseScenario(int durA, int durB)
    {
        List<TemporalInterval> domA = new ArrayList<>(a.getF());
        List<TemporalInterval> domB = new ArrayList<>(b.getF());

        double best = 0.0;

        for (int cutA = 0; cutA < domA.size(); ++cutA)
        {
            List<TemporalInterval> subA = domA.subList(cutA, domA.size());
            for (int cutB = 0; cutB < domB.size(); ++cutB)
            {
                List<TemporalInterval> subB = domB.subList(cutB, domB.size());

                List<ActivityPart> partsA = buildSchedule(a, subA, durA, 0);
                if (partsA == null)
                    continue;

                int lastEndA = partsA.get(partsA.size() - 1).calculateEndTime();

                TemporalInterval firstB = subB.isEmpty() ? null : subB.get(0);
                for (int startB = lastEndA; firstB != null && startB + b.getSmini() <= firstB.getBi(); ++startB)
                {
                    List<ActivityPart> partsB = buildSchedule(b, subB, durB, startB);

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
    public double computeUtility(List<ActivityPart> pA, List<ActivityPart> pB) {

        int total = pA.size() * pB.size();
        if (total == 0)
            return 0.1;

        int ok = 0;
        for (ActivityPart pa : pA)
            for (ActivityPart pb : pB) {
                int gap = Math.abs(pb.getTij() - pa.calculateEndTime());
                if (gap <= maxDistance)
                    ++ok;
            }
        return (double) ok / total;
    }


    public List<ActivityPart> buildSchedule(Activity proto, List<TemporalInterval> domain, int targetDur, int earliestStart)
    {
        int sMin = proto.getSmini(), sMax = proto.getSmaxi();
        int dMin = proto.getDmini(), dMax = proto.getDmaxi();

        List<ActivityPart> parts = new ArrayList<>();
        int remaining = targetDur, id = 1;
        Integer prevEnd = null;

        for (TemporalInterval ti : domain) {

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

                parts.add(new ActivityPart(id++, cursor, dur));
                remaining -= dur;
                prevEnd = cursor + dur;
                cursor  = prevEnd + dMin;
            }
            if (remaining == 0) break;
        }
        return remaining == 0 ? parts : null;
    }

    private boolean overlaps(List<ActivityPart> aParts, List<ActivityPart> bParts) {
        for (ActivityPart pa : aParts) {
            int aStart = pa.getTij(), aEnd = pa.calculateEndTime();
            for (ActivityPart pb : bParts) {
                int bStart = pb.getTij(), bEnd = pb.calculateEndTime();
                if (aStart < bEnd && bStart < aEnd) return true;
            }
        }
        return false;
    }
}
