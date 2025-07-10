package org.example.Scheduler.ForwardCheckingUtils;

import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

sealed interface DomainEdit permits Add, Remove
{
    ActivitySWO target();
    TemporalIntervalSWO original();
}

record Add(ActivitySWO target, TemporalIntervalSWO original) implements DomainEdit
{

}

record Remove(ActivitySWO target, TemporalIntervalSWO original) implements DomainEdit
{

}

