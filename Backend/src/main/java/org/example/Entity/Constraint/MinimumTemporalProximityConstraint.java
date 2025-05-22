//package org.example.Entity.Constraint;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.example.Entity.Activity.Activity;
//import org.example.Entity.Activity.ActivityPart;
//
//import java.time.Duration;
//
//@Getter
//@Setter
//@AllArgsConstructor
//public class MinimumTemporalProximityConstraint implements Constraint
//{
//    private Activity activity1;
//    private Activity activity2;
//    private Integer timeTakenBetweenAny2PartsOfActivities;
//
//
//    @Override
//    public boolean isSatisfied()
//    {
//        for (ActivityPart activityPart2 : activity2.getPartsOfTheActivity())
//        {
//            for (ActivityPart activityPart1 : activity1.getPartsOfTheActivity())
//            {
//                long minutesBetween =
//                    Math.abs(Duration.between(
//                        activityPart2.getStartTime(),
//                        activityPart1.calculateEndTime()
//                    ).toMinutes());
//
//                if (minutesBetween < timeTakenBetweenAny2PartsOfActivities)
//                    return false;
//
//                minutesBetween =
//                        Math.abs(Duration.between(
//                                activityPart1.getStartTime(),
//                                activityPart2.calculateEndTime()
//                        ).toMinutes());
//
//                if (minutesBetween < timeTakenBetweenAny2PartsOfActivities)
//                    return false;
//            }
//        }
//        return true;
//    }
//}
