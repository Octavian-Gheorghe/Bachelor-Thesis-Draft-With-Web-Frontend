package org.example.Scheduler.Entity.Utility.Unary;

import org.example.Scheduler.Entity.Activity.ActivityPart;

import java.util.List;

public interface UnaryUtility
{
    double computeUtility(List<ActivityPart> parts);
}
