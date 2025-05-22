//package org.example.Entity.Preference;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.example.Entity.Activity.Activity;
//import org.example.Entity.Activity.ActivityPart;
//
//import java.time.Duration;
//
//@AllArgsConstructor
//@Getter
//@Setter
//public class MaximumTemporalProximityReference implements Preference
//{
//    private Activity activity1;
//    private Activity activity2;
//    private Integer timeTakenBetweenAny2PartsOfActivities;
//    private double utility; //Udmax
//
//    //TODO - Degree of Satisfaction!!????
//    public double getUtilityIfValid()
//    {
//        if(isSatisfied())
//            return utility;
//        return 0;
//    }
//
//    @Override
//    public boolean isSatisfied()
//    {
//        for (ActivityPart activityPart2 : activity2.getPartsOfTheActivity())
//        {
//            for (ActivityPart activityPart1 : activity1.getPartsOfTheActivity())
//            {
//                long minutesBetween =
//                        Math.abs(Duration.between(
//                                activityPart2.getStartTime(),
//                                activityPart1.calculateEndTime()
//                        ).toMinutes());
//
//                if (minutesBetween > timeTakenBetweenAny2PartsOfActivities)
//                    return false;
//
//                minutesBetween =
//                        Math.abs(Duration.between(
//                                activityPart1.getStartTime(),
//                                activityPart2.calculateEndTime()
//                        ).toMinutes());
//
//                if (minutesBetween > timeTakenBetweenAny2PartsOfActivities)
//                    return false;
//            }
//        }
//        return true;
//    }
//}
