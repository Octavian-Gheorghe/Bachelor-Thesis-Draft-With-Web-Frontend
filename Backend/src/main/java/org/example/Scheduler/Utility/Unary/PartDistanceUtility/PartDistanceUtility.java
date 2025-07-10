package org.example.Scheduler.Utility.Unary.PartDistanceUtility;

import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Utility.Unary.UnaryUtility;

import java.util.List;

public interface PartDistanceUtility extends UnaryUtility
{
    double computeUtility(List<ActivityPartSWO> parts);
}
