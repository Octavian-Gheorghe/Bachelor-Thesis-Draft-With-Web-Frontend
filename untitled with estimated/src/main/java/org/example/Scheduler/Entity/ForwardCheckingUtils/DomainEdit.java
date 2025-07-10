package org.example.Scheduler.Entity.ForwardCheckingUtils;

import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.TemporalInterval;

sealed interface DomainEdit permits Add, Remove {
    Activity target();
    TemporalInterval original();
}

record Add(Activity target, TemporalInterval original) implements DomainEdit {}

record Remove(Activity target, TemporalInterval original) implements DomainEdit {}

