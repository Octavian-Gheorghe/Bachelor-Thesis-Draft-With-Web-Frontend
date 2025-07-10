package org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility;

import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Utility.Unary.UnaryUtility;

import java.util.List;

public interface DomainActivityUtility extends UnaryUtility
{
    double computeUtility(List<ActivityPart> parts);
    double calculateActivityForBestCaseScenario(int totalDur);
}
