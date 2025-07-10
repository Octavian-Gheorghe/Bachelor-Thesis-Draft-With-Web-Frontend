package org.example.Scheduler.Utility.Unary;

import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.List;

public interface UnaryUtility
{
    double computeUtility(List<ActivityPartSWO> parts);
}
