package org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility;

import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Utility.Unary.UnaryUtility;

import java.util.List;

public interface DomainActivityUtility extends UnaryUtility
{
    double computeUtility(List<ActivityPartSWO> parts);
    double calculateActivityForBestCaseScenario(int totalDur);
}
