package org.example.Scheduler.Entity.Utility.Binary;

import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.List;

public interface BinaryUtility
{
    double computeUtility(List<ActivityPartSWO> pA, List<ActivityPartSWO> pB);
}
