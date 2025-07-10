package org.example.Scheduler.Entity.Utility.Binary;

import org.example.Scheduler.Entity.Activity.ActivityPart;

import java.util.List;

public interface BinaryUtility
{
    double computeUtility(List<ActivityPart> pA, List<ActivityPart> pB);
}
