package org.example.Scheduler.ForwardCheckingUtils;

import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.ArrayDeque;
import java.util.Deque;

public class DomainContext implements AutoCloseable
{
    private final Deque<DomainEdit> log = new ArrayDeque<>();

    public void recordRemove(ActivitySWO act, TemporalIntervalSWO original)
    {
        log.push(new Remove(act, original));
    }

    public void recordAdd(ActivitySWO a, TemporalIntervalSWO t)
    {
        log.push(new Add(a,t));
    }

    @Override
    public void close()
    {
        while (!log.isEmpty())
        {
            DomainEdit e = log.pop();
            if (e instanceof Add a)
            {
                a.target().getF().remove(a.original());
            }
            else if (e instanceof Remove r)
            {
                r.target().getF().add(r.original());
            }
        }
    }
}