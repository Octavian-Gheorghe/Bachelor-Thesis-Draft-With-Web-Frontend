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
@Getter @Setter
public class LinearActivityUtility implements DomainActivityUtility {

    private ActivitySWO a;
    private int earlyThreshold;

    @Override
    public double computeUtility(List<ActivityPartSWO> parts)
    {
        if (parts == null || parts.isEmpty())
            return 0.0;

        int last = a.getF().get(a.getF().size() - 1).getBi();
        double utilSum = 0.0;
        int slots = 0;

        for (ActivityPartSWO p : parts)
            for (int t = 0; t < p.getDurij(); t++)
            {
                int time = p.getTij() + t;

                double u = (time <= earlyThreshold) ? 1.0 : 1.0 - Math.min(1.0, (double)(time - earlyThreshold) / (last - earlyThreshold));

                utilSum += u;
                slots++;
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
    public double calculateActivityForBestCaseScenario(int totalDur) {

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
                for(ActivityPartSWO a : candidate)
                    System.out.println(a);
                //System.out.println();
                return computeUtility(candidate);
            }
        }
        return 0.1;
    }

    private List<ActivityPartSWO> trySchedule(int totalDur) {

        List<ActivityPartSWO> parts = new ArrayList<>();
        int remaining = totalDur;
        int nextId = 1;

        remaining = fillForward(parts, anchorIndex(), earlyThreshold,
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
            if (a.getF().get(i).getAi() <= earlyThreshold
                    && earlyThreshold < a.getF().get(i).getBi())
                return i;

        for (int i = 0; i < a.getF().size(); i++)
            if (a.getF().get(i).getAi() > earlyThreshold)
                return i;

        return 0;
    }

    /* greedy forward fill */
    private int fillForward(List<ActivityPartSWO> out,
                            int startIdx, int cursor,
                            int remaining, int idStart) {
        return fillForward(out, startIdx, cursor,
                remaining, idStart, a.getF().size());
    }

    private int fillForward(List<ActivityPartSWO> out,
                            int startIdx, int cursor,
                            int remaining, int nextId,
                            int stopExclusive) {

        for (int i = startIdx; i < stopExclusive && remaining > 0; i++) {

            TemporalIntervalSWO ti = a.getF().get(i);
            cursor = Math.max(cursor, ti.getAi());

            while (remaining > 0 && cursor + a.getSmini() <= ti.getBi()) {

                int room   = ti.getBi() - cursor;
                int size   = Math.min(Math.min(a.getSmaxi(), room), remaining);

                if (remaining - size > 0 && remaining - size < a.getSmini())
                    size -= (a.getSmini() - (remaining - size));

                if (size < a.getSmini()) break;

                out.add(new ActivityPartSWO(nextId++, cursor, size));
                remaining -= size;
                cursor    += size + a.getDmini();
            }
            cursor = Integer.MIN_VALUE;   // only anchor honoured
        }
        out.sort(Comparator.comparingInt(ActivityPartSWO::getTij));
        return remaining;
    }
}
