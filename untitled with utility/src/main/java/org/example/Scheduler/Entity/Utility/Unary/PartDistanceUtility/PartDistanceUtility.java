package org.example.Scheduler.Entity.Utility.Unary.PartDistanceUtility;

import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Utility.Unary.UnaryUtility;

import java.util.List;

public interface PartDistanceUtility extends UnaryUtility
{
    double computeUtility(List<ActivityPartSWO> parts);
    double calculateActivityForBestCaseScenario(int totalDur);
}
