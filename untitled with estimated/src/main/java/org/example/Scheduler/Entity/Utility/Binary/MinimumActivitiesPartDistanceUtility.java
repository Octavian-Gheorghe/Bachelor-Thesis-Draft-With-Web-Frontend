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
public class MinimumActivitiesPartDistanceUtility implements BinaryUtility
{

    private Activity a;
    private Activity b;
    private int minDistance;

    public double calculateActivityForBestCaseScenario(int durA, int durB)
    {
        List<ActivityPart> aEarly = buildForward (a, durA);
        List<ActivityPart> bLate = buildBackward(b, durB, null);

        double best = 0.0;
        if (aEarly != null && bLate != null)
        {
//            for(ActivityPart ap1 : aEarly)
//                System.out.println(ap1);
//            System.out.println();
//            for(ActivityPart ap1 : bLate)
//                System.out.println(ap1);
            best = computeUtility(aEarly, bLate);
        }

        if(best == 1.0)
            return 1.0;

        List<ActivityPart> aLate  = buildBackward(a, durA, null);
        List<ActivityPart> bEarly = buildForward (b, durB);

        if (aLate != null && bEarly != null)
        {
            for(ActivityPart ap1 : aLate)
                System.out.println(ap1);
            System.out.println();
            for(ActivityPart ap1 : bEarly)
                System.out.println(ap1);
            best = Math.max(best, computeUtility(aLate, bEarly));
        }
        return best == 0.0 ? 0.1 : best;
    }

    @Override
    public double computeUtility(List<ActivityPart> partsA, List<ActivityPart> partsB)
    {
        int total = partsA.size() * partsB.size();
        if (total == 0)
            return 0.0;

        int satisfied = 0;
        for (ActivityPart pa : partsA)
            for (ActivityPart pb : partsB)
            {
                int start1 = pa.getTij();
                int end1 = pa.calculateEndTime();
                int start2 = pb.getTij();
                int end2 = pb.calculateEndTime();
//                System.out.println("A has " + start1 + " - " + end1);
//                System.out.println("B has " + start2 + " - " + end2);
//                System.out.println("Min distance is : " + minDistance);

                boolean ok = (end1 + minDistance <= start2) || (end2 + minDistance <= start1);
                if(ok)
                    ++satisfied;
            }
//        System.out.println("Total valid pairs: "  + satisfied);
//        System.out.println("Total pairs: "  + total);
        return (double) satisfied / total;
    }

    private List<ActivityPart> buildForward(Activity act, int targetDur) {

        List<TemporalInterval> domain = new ArrayList<>(act.getF());
        int sMin = act.getSmini(), sMax = act.getSmaxi(), dMin = act.getDmini();

        List<ActivityPart> out = new ArrayList<>();
        int remain = targetDur, id = 1;

        for (TemporalInterval t : domain) {
            int cursor = t.getAi();

            while (remain > 0 && cursor + sMin <= t.getBi()) {

                int dur = Math.min(Math.min(sMax, t.getBi() - cursor), remain);

                if (remain - dur > 0 && remain - dur < sMin)
                    dur -= (sMin - (remain - dur));
                if (dur < sMin) break;

                out.add(new ActivityPart(id++, cursor, dur));
                remain -= dur;
                cursor += dur + dMin;
            }
            if (remain == 0) break;
        }
        return remain == 0 ? out : null;
    }

    private List<ActivityPart> buildBackward(Activity act, int targetDur, Integer notBefore)
    {

        List<TemporalInterval> domain = new ArrayList<>(act.getF());
        int sMin = act.getSmini(), sMax = act.getSmaxi(), dMin = act.getDmini();

        List<ActivityPart> out = new ArrayList<>();
        int remain = targetDur, id = 1;

        for (int idx = domain.size() - 1; idx >= 0 && remain > 0; --idx) {

            TemporalInterval t = domain.get(idx);
            int cursorEnd = t.getBi();

            if (notBefore != null && cursorEnd < notBefore) break;

            while (remain > 0 && cursorEnd - sMin >= t.getAi()) {

                int dur = Math.min(Math.min(sMax, cursorEnd - t.getAi()), remain);

                if (remain - dur > 0 && remain - dur < sMin)
                    dur -= (sMin - (remain - dur));
                if (dur < sMin) break;

                int start = cursorEnd - dur;
                if (notBefore != null && start < notBefore) break;

                out.add(0, new ActivityPart(id++, start, dur));
                remain   -= dur;
                cursorEnd = start - dMin;
            }
        }
        return remain == 0 ? out : null;
    }
}