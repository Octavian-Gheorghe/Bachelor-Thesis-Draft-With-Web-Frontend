//package org.example.Entity.Constraint;
//
//import org.example.Entity.Activity.Activity;
//import org.example.Entity.Activity.ActivityPart;
//
//import java.time.Duration;
//import java.util.List;
//
//public class ActivityMaximumTemporalDistanceConstraint implements Constraint
//{
//    Activity activity;
//
//    @Override
//    public boolean isSatisfied()
//    {
//        List<ActivityPart> listOfActivityParts = activity.getPartsOfTheActivity();
//        for(int i = 0; i < listOfActivityParts.size()-1 ; i++)
//        {
//            ActivityPart activityPart1 = listOfActivityParts.get(i);
//            ActivityPart activityPart2 = listOfActivityParts.get(i+1);
//            long minutesBetween =
//                    Math.abs(Duration.between(
//                            activityPart2.getStartTime(),
//                            activityPart1.calculateEndTime()
//                    ).toMinutes());
//            if(minutesBetween > activity.getMaximumTemporalDistanceBetweenParts())
//                return false;
//        }
//        return true;
//    }
//}
