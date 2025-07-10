package org.example.Scheduler.Entity.ForwardCheckingUtils;

import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.TemporalInterval;

import java.util.ArrayDeque;
import java.util.Deque;

public class DomainContext implements AutoCloseable {

    private final Deque<DomainEdit> log = new ArrayDeque<>();

    public  void recordRemove(Activity act, TemporalInterval original) {
        log.push(new Remove(act, original));
    }

    public void recordAdd(Activity a, TemporalInterval t)    { log.push(new Add(a,t)); }

    /** Undo in LIFO order */
    @Override public void close() {
        while (!log.isEmpty()) {
            DomainEdit e = log.pop();
            if (e instanceof Add a) {
                a.target().getF().remove(a.original());
            } else if (e instanceof Remove r) {
                r.target().getF().add(r.original());
            }
        }
    }
}