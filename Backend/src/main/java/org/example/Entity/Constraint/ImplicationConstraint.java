//package org.example.Entity.Constraint;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.example.Entity.Activity.Activity;
//import org.example.Entity.Schedule;
//
//@Getter
//@Setter
//@AllArgsConstructor
//public class ImplicationConstraint implements Constraint
//{
//    Activity activity1;
//    Activity activity2;
//    Schedule scheduleThatIfContainsOneHasTheOther;
//
//    public boolean isSatisfied()
//    {
//        if(scheduleThatIfContainsOneHasTheOther.searchForActivity(activity2.getId()))
//        {
//            return scheduleThatIfContainsOneHasTheOther.searchForActivity(activity1.getId());
//        }
//        return false;
//    }
//}
