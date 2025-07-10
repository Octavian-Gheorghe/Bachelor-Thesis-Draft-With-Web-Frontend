package org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StepwiseActivityUtility implements DomainActivityUtility
{
    private ActivitySWO a;
    private int stepPoint;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts)
    {

        double utilSum = 0.0;
        int slots = 0;

        for (ActivityPartSWO p : parts)
        {
            for (int t = 0; t < p.getDurij(); t++)
            {
                int time = p.getTij() + t;
                utilSum += (time >= stepPoint ? 1.0 : 0.0);
                slots++;
            }
        }
        return slots == 0 ? 0.0 : utilSum / slots;
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
        if (totalDur < a.getDurimin() || totalDur > a.getDurimax())
            return 0.1;

        int netSize = getNetsize(a.getF());
        for (int dur = totalDur; dur >= a.getDurimin(); dur--)
        {
            if(dur > netSize)
                continue;
            List<ActivityPartSWO> candidate = trySchedule(dur);
            if (candidate != null)
            {
                candidate.sort(Comparator.comparingInt(ActivityPartSWO::getTij));
                for(ActivityPartSWO a : candidate)
                    System.out.println(a);
                return computeUtility(candidate);
            }
            else System.out.println("Invalid for dur = " + dur);
        }
        return 0.1;
    }

    private List<ActivityPartSWO> trySchedule(int totalDur)
    {
        List<ActivityPartSWO> parts = new ArrayList<>();
        int remaining = totalDur;
        int nextId = 1;

        remaining = fillForward(parts, anchorIndex(), stepPoint,
                remaining, nextId);
        nextId += parts.size();

        if (remaining > 0)
        {
            remaining = fillForward(parts,
                    0,
                    Integer.MIN_VALUE,
                    remaining,
                    nextId,
                    anchorIndex() + 1);
        }

        return remaining == 0 ? parts : null;
    }

    private int anchorIndex() {

        for (int i = 0; i < a.getF().size(); i++)
            if (a.getF().get(i).getAi() <= stepPoint
                    && stepPoint < a.getF().get(i).getBi())
                return i;

        for (int i = 0; i < a.getF().size(); i++)
            if (a.getF().get(i).getAi() > stepPoint)
                return i;

        return 0;
    }

    private int fillForward(List<ActivityPartSWO> out, int startIdx, int cursor, int remaining, int idStart) {
        return fillForward(out, startIdx, cursor, remaining, idStart, a.getF().size());
    }

    private int fillForward(List<ActivityPartSWO> out, int startIdx, int cursor, int remaining, int nextId, int stopExclusive)
    {

        for (int i = startIdx; i < stopExclusive && remaining > 0; i++) {

            TemporalIntervalSWO ti = a.getF().get(i);
            cursor = Math.max(cursor, ti.getAi());

            while (remaining > 0 && cursor + a.getSmini() <= ti.getBi())
            {
                int room   = ti.getBi() - cursor;
                int size   = Math.min(Math.min(a.getSmaxi(), room), remaining);

                if (remaining - size > 0 && remaining - size < a.getSmini())
                    size -= (a.getSmini() - (remaining - size));

                if (size < a.getSmini())
                    break;

                out.add(new ActivityPartSWO(nextId++, cursor, size));
                remaining -= size;
                cursor += size + a.getDmini();
            }
            cursor = Integer.MIN_VALUE;
        }
        out.sort(Comparator.comparingInt(ActivityPartSWO::getTij));
        return remaining;
    }


//    private List<ActivityPart> trySchedule(List<TemporalInterval> domain, int totalDur)
//    {
//
//        List<ActivityPart> parts = new ArrayList<>();
//        int remaining   = totalDur;
//        int nextPartId  = 1;
//
//        int anchor = -1;
//        for (int i = 0; i < domain.size(); i++)
//            if (domain.get(i).getAi() <= stepPoint && stepPoint < domain.get(i).getBi()) {
//                anchor = i; break;
//            }
//        if (anchor == -1)
//            for (int i = 0; i < domain.size() && anchor == -1; i++)
//                if (domain.get(i).getAi() > stepPoint) anchor = i;
//        if (anchor == -1)
//            anchor = 0;
//        remaining = fillForward(parts, domain, anchor, stepPoint, remaining, nextPartId);
//        nextPartId += parts.size();
//
//        if (remaining > 0)
//            remaining = fillForward(parts, domain, 0, Integer.MIN_VALUE, remaining, nextPartId, anchor);
//
//        return remaining == 0 ? parts : null;
//    }
//
//    private int fillForward(List<ActivityPart> out, List<TemporalInterval> dom, int startIdx, int firstStart, int remaining, int idStart) {
//        return fillForward(out, dom, startIdx, firstStart, remaining, idStart, dom.size());
//    }
//
//    private int fillForward(List<ActivityPart> out, List<TemporalInterval> dom, int startIdx, int firstStart, int remaining, int nextId, int stopExclusive) {
//
//        final int sMin = a.getSmini();
//        final int sMax = a.getSmaxi();
//        final int dMin = a.getDmini();
//
//        for (int i = startIdx; i < stopExclusive && remaining > 0; i++)
//        {
//
//            TemporalInterval ti = dom.get(i);
//            int cursor = Math.max(firstStart, ti.getAi());
//
//            while (remaining > 0 && cursor + sMin <= ti.getBi()) {
//
//                int room = ti.getBi() - cursor;
//                int partSize = Math.min(Math.min(sMax, room), remaining);
//
//                if (partSize < sMin) break;
//
//                out.add(new ActivityPart(nextId++, cursor, partSize));
//                remaining -= partSize;
//                cursor    += partSize + dMin;
//            }
//            firstStart = Integer.MIN_VALUE;
//        }
//        return remaining;
//    }
}
