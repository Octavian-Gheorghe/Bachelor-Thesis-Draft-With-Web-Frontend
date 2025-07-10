package org.example;

import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Constraint.Binary.MinimumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Entity.Constraint.Binary.OrderingConstraint;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.DistanceMatrix;
import org.example.Scheduler.Entity.LocationSWO;
import org.example.Scheduler.Entity.ScheduleSWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;
//import org.example.Entity.Utility.Binary.MaximumActivitiesPartDistanceUtility;
//import org.example.Entity.Utility.Binary.MinimumActivitiesPartDistanceUtility;
//import org.example.Entity.Utility.Binary.OrderingPreferenceUtility;
//import org.example.Entity.Utility.Unary.DomainActivityUtility.LinearActivityUtility;
//import org.example.Entity.Utility.Unary.DomainActivityUtility.StepwiseActivityUtility;
//import org.example.Entity.Utility.Unary.PartDistanceUtility.MaximumPartDistanceUtility;
//import org.example.Entity.Utility.Unary.PartDistanceUtility.MinimumPartDistanceUtility;
import org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility.LinearActivityUtility;
import org.example.Scheduler.Entity.Utility.Unary.UnaryUtility;
import org.example.Scheduler.SWO;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) {

//        //ACTIVITY A
//        LocationSWO l11 = new LocationSWO(1, "X");
//        LocationSWO l12 = new LocationSWO(2, "Y");
//        List<LocationSWO> l1 = new ArrayList<>();
//        l1.add(l11);
//        l1.add(l12);
//        TemporalIntervalSWO ta1 = new TemporalIntervalSWO(0, 25);
//        TemporalIntervalSWO ta2 = new TemporalIntervalSWO(30, 60);
//        List<TemporalIntervalSWO> t1 = new ArrayList<>();
//        t1.add(ta1);
//        t1.add(ta2);
//        ActivitySWO A = new ActivitySWO(1, "A", 4, 3, 2, 1, 999999999, 2, t1, l1);
////
////
//        //ACTIVITY B
//        List<LocationSWO> l2 = new ArrayList<>();
//        l2.add(l11);
//        TemporalIntervalSWO tb1 = new TemporalIntervalSWO(2, 11);
//        TemporalIntervalSWO tb2 = new TemporalIntervalSWO(14, 19);
//        List<TemporalIntervalSWO> t2 = new ArrayList<>();
//        t2.add(tb1);
//        t2.add(tb2);
//        ActivitySWO B = new ActivitySWO(2, "B", 4, 2, t2, l2);
//
////
//        //ACTIVITY D
//        TemporalIntervalSWO td1 = new TemporalIntervalSWO(11, 14);
//        List<TemporalIntervalSWO> t4 = new ArrayList<>();
//        t4.add(td1);
//        ActivitySWO D = new ActivitySWO(4, "D", 3, 3, t4, l1);
////
////
//        //ACTIVITY E
//        TemporalIntervalSWO te1 = new TemporalIntervalSWO(2, 3);
//        List<TemporalIntervalSWO> t5 = new ArrayList<>();
//        t5.add(te1);
//        ActivitySWO E = new ActivitySWO(5, "E", 1, 1, t5, l1);
////
////
//        //ACTIVITY F
//        TemporalIntervalSWO tf1 = new TemporalIntervalSWO(17, 19);
//        List<TemporalIntervalSWO> t6 = new ArrayList<>();
//        t6.add(tf1);
//        ActivitySWO F = new ActivitySWO(6, "F", 2, 2, t6, l1);
////
////        //ACTIVITY C
//        List<LocationSWO> l3 = new ArrayList<>();
//        l3.add(l12);
//        TemporalIntervalSWO tc1 = new TemporalIntervalSWO(3, 19);
//        List<TemporalIntervalSWO> t3 = new ArrayList<>();
//        t3.add(tc1);
//        ActivitySWO C = new ActivitySWO(3, "C", 6, 3, 2, 1, 0, 0, t3, l3);
//
//
//        Location l13 = new Location(3, "Z");
//
//
//        // ACTIVITY G
//        List<TemporalInterval> t7 = new ArrayList<>();
//        t7.add(new TemporalInterval(6, 9));
//        Activity G = new Activity(7, "G", 2, 2, t7, List.of(l11));
//
//// ACTIVITY H
//        List<TemporalInterval> t8 = new ArrayList<>();
//        t8.add(new TemporalInterval(10, 13));
//        Activity H = new Activity(8, "H", 3, 3, t8, List.of(l12));
//
//// ACTIVITY I
//        List<TemporalInterval> t9 = new ArrayList<>();
//        t9.add(new TemporalInterval(14, 16));
//        Activity I = new Activity(9, "I", 2, 2, t9, List.of(l13));
//
//// ACTIVITY J
//        List<TemporalInterval> t10 = new ArrayList<>();
//        t10.add(new TemporalInterval(17, 19));
//        Activity J = new Activity(10, "J", 2, 2, t10, List.of(l11));
//
//// ACTIVITY K
//        List<TemporalInterval> t11 = new ArrayList<>();
//        t11.add(new TemporalInterval(19, 21));
//        Activity K = new Activity(11, "K", 2, 2, t11, List.of(l12, l13));
//
//// ACTIVITY L
//        List<TemporalInterval> t12 = new ArrayList<>();
//        t12.add(new TemporalInterval(21, 23));
//        Activity L = new Activity(12, "L", 2, 1, t12, List.of(l11));
//
//// ACTIVITY M
//        List<TemporalInterval> t13 = new ArrayList<>();
//        t13.add(new TemporalInterval(0, 5));
//        Activity M = new Activity(13, "M", 3, 3, t13, List.of(l12));
//
//// ACTIVITY N
//        List<TemporalInterval> t14 = new ArrayList<>();
//        t14.add(new TemporalInterval(5, 7));
//        Activity N = new Activity(14, "N", 1, 2, t14, List.of(l13));
//
//// ACTIVITY O
//        List<TemporalInterval> t15 = new ArrayList<>();
//        t15.add(new TemporalInterval(7, 10));
//        Activity O = new Activity(15, "O", 3, 3, t15, List.of(l11, l12));
//
//// ACTIVITY P
//        List<TemporalInterval> t16 = new ArrayList<>();
//        t16.add(new TemporalInterval(11, 14));
//        Activity P = new Activity(16, "P", 2, 2, t16, List.of(l13));
//
//// ACTIVITY Q
//        List<TemporalInterval> t17 = new ArrayList<>();
//        t17.add(new TemporalInterval(15, 18));
//        Activity Q = new Activity(17, "Q", 3, 3, t17, List.of(l11));
//
//// ACTIVITY R
//        List<TemporalInterval> t18 = new ArrayList<>();
//        t18.add(new TemporalInterval(18, 21));
//        Activity R = new Activity(18, "R", 2, 1, t18, List.of(l12));
//
//// ACTIVITY S
//        List<TemporalInterval> t19 = new ArrayList<>();
//        t19.add(new TemporalInterval(22, 25));
//        Activity S = new Activity(19, "S", 3, 3, t19, List.of(l13));
//
//// ACTIVITY T
//        List<TemporalInterval> t20 = new ArrayList<>();
//        t20.add(new TemporalInterval(0, 4));
//        Activity T = new Activity(20, "T", 2, 2, t20, List.of(l11));
//
//// ACTIVITY U
//        List<TemporalInterval> t21 = new ArrayList<>();
//        t21.add(new TemporalInterval(5, 9));
//        Activity U = new Activity(21, "U", 3, 3, t21, List.of(l12, l13));
//
//// ACTIVITY V
//        List<TemporalInterval> t22 = new ArrayList<>();
//        t22.add(new TemporalInterval(10, 12));
//        Activity V = new Activity(22, "V", 2, 2, t22, List.of(l13));
//
//// ACTIVITY W
//        List<TemporalInterval> t23 = new ArrayList<>();
//        t23.add(new TemporalInterval(12, 14));
//        Activity W = new Activity(23, "W", 1, 1, t23, List.of(l11));
//
//// ACTIVITY X
//        List<TemporalInterval> t24 = new ArrayList<>();
//        t24.add(new TemporalInterval(14, 17));
//        Activity X = new Activity(24, "X", 2, 2, t24, List.of(l12));
//
//// ACTIVITY Y
//        List<TemporalInterval> t25 = new ArrayList<>();
//        t25.add(new TemporalInterval(17, 20));
//        Activity Y = new Activity(25, "Y", 2, 3, t25, List.of(l13));
//
//// ACTIVITY Z
//        List<TemporalInterval> t26 = new ArrayList<>();
//        t26.add(new TemporalInterval(20, 24));
//        Activity Z = new Activity(26, "Z", 3, 3, t26, List.of(l11, l13));
//
//



//        Location l11 = new Location(1, "X");
//        Location l12 = new Location(2, "Y");
        LocationSWO l13 = new LocationSWO(3, "Z");

// ACTIVITY A
//        List<Location> l1 = List.of(l11, l12);
//        List<TemporalInterval> t1 = List.of(new TemporalInterval(0, 30), new TemporalInterval(60, 90));
//        Activity A = new Activity(1, "A", 4, 3, 3, 1, 999999999, 2, t1, l1);
//
//// ACTIVITY B
//        List<Location> l2 = List.of(l11);
//        List<TemporalInterval> t2 = List.of(new TemporalInterval(10, 40), new TemporalInterval(50, 80));
//        Activity B = new Activity(2, "B", 4, 2, t2, l2);
//
//// ACTIVITY C
//        List<Location> l3 = List.of(l12);
//        List<TemporalInterval> t3 = List.of(new TemporalInterval(20, 85));
//        Activity C = new Activity(3, "C", 6, 3, 2, 1, 10, 0, t3, l3);
//
//// ACTIVITY D
//        List<TemporalInterval> t4 = List.of(new TemporalInterval(40, 70));
//        Activity D = new Activity(4, "D", 3, 3, t4, l1);
//
//// ACTIVITY E
//        List<TemporalInterval> t5 = List.of(new TemporalInterval(5, 25));
//        Activity E = new Activity(5, "E", 1, 1, t5, l1);
//
//// ACTIVITY F
//        List<TemporalInterval> t6 = List.of(new TemporalInterval(60, 70));
//        Activity F = new Activity(6, "F", 2, 2, t6, l1);
//
//// ACTIVITY G
//        List<TemporalInterval> t7 = List.of(new TemporalInterval(15, 50));
//        Activity G = new Activity(7, "G", 2, 2, t7, List.of(l11));
//
//// ACTIVITY H
//        List<TemporalInterval> t8 = List.of(new TemporalInterval(30, 60));
//        Activity H = new Activity(8, "H", 3, 3, t8, List.of(l12));
//
//// ACTIVITY I
//        List<TemporalInterval> t9 = List.of(new TemporalInterval(55, 85));
//        Activity I = new Activity(9, "I", 2, 2, t9, List.of(l13));
//
//// ACTIVITY J
//        List<TemporalInterval> t10 = List.of(new TemporalInterval(70, 90));
//        Activity J = new Activity(10, "J", 2, 2, t10, List.of(l11));
//
//// ACTIVITY K
//        List<TemporalInterval> t11 = List.of(new TemporalInterval(80, 100));
//        Activity K = new Activity(11, "K", 2, 2, t11, List.of(l12, l13));
//
//// ACTIVITY L
//        List<TemporalInterval> t12 = List.of(new TemporalInterval(10, 35));
//        Activity L = new Activity(12, "L", 2, 1, t12, List.of(l11));
//
//// ACTIVITY M
//        List<TemporalInterval> t13 = List.of(new TemporalInterval(0, 30));
//        Activity M = new Activity(13, "M", 3, 3, t13, List.of(l12));
//
//// ACTIVITY N
//        List<TemporalInterval> t14 = List.of(new TemporalInterval(25, 45));
//        Activity N = new Activity(14, "N", 1, 2, t14, List.of(l13));
//
//// ACTIVITY O
//        List<TemporalInterval> t15 = List.of(new TemporalInterval(35, 60));
//        Activity O = new Activity(15, "O", 3, 3, t15, List.of(l11, l12));
//
//// ACTIVITY P
//        List<TemporalInterval> t16 = List.of(new TemporalInterval(50, 80));
//        Activity P = new Activity(16, "P", 2, 2, t16, List.of(l13));
//
//// ACTIVITY Q
//        List<TemporalInterval> t17 = List.of(new TemporalInterval(65, 85));
//        Activity Q = new Activity(17, "Q", 3, 3, t17, List.of(l11));
//
//// ACTIVITY R
//        List<TemporalInterval> t18 = List.of(new TemporalInterval(75, 95));
//        Activity R = new Activity(18, "R", 2, 1, t18, List.of(l12));
//
//// ACTIVITY S
//        List<TemporalInterval> t19 = List.of(new TemporalInterval(85, 100));
//        Activity S = new Activity(19, "S", 3, 3, t19, List.of(l13));
//
//// ACTIVITY T
//        List<TemporalInterval> t20 = List.of(new TemporalInterval(0, 20));
//        Activity T = new Activity(20, "T", 2, 2, t20, List.of(l11));
//
//// ACTIVITY U
//        List<TemporalInterval> t21 = List.of(new TemporalInterval(25, 55));
//        Activity U = new Activity(21, "U", 3, 3, t21, List.of(l12, l13));
//
//// ACTIVITY V
//        List<TemporalInterval> t22 = List.of(new TemporalInterval(40, 65));
//        Activity V = new Activity(22, "V", 2, 2, t22, List.of(l13));
//
//// ACTIVITY W
//        List<TemporalInterval> t23 = List.of(new TemporalInterval(60, 75));
//        Activity W = new Activity(23, "W", 1, 1, t23, List.of(l11));
//
//// ACTIVITY X
//        List<TemporalInterval> t24 = List.of(new TemporalInterval(70, 90));
//        Activity X = new Activity(24, "X", 2, 2, t24, List.of(l12));
//
//// ACTIVITY Y
//        List<TemporalInterval> t25 = List.of(new TemporalInterval(85, 100));
//        Activity Y = new Activity(25, "Y", 2, 3, t25, List.of(l13));
//
//// ACTIVITY Z
//        List<TemporalInterval> t26 = List.of(new TemporalInterval(50, 95));
//        Activity Z = new Activity(26, "Z", 3, 3, t26, List.of(l11, l13));
//
//
//        Activity A1 = new Activity(27, "A1", 2, 2, List.of(new TemporalInterval(0, 20)), List.of(l11));
//        Activity B1 = new Activity(28, "B1", 3, 3, List.of(new TemporalInterval(10, 35)), List.of(l12));
//        Activity C1 = new Activity(29, "C1", 1, 1, List.of(new TemporalInterval(20, 25)), List.of(l13));
//        Activity D1 = new Activity(30, "D1", 2, 2, List.of(new TemporalInterval(25, 45)), List.of(l11));
//        Activity E1 = new Activity(31, "E1", 3, 3, List.of(new TemporalInterval(30, 55)), List.of(l11, l12));
//        Activity F1 = new Activity(32, "F1", 2, 2, List.of(new TemporalInterval(35, 60)), List.of(l13));
//        Activity G1 = new Activity(33, "G1", 1, 1, List.of(new TemporalInterval(40, 50)), List.of(l12));
//        Activity H1 = new Activity(34, "H1", 3, 2, List.of(new TemporalInterval(45, 65)), List.of(l11));
//        Activity I1 = new Activity(35, "I1", 2, 3, List.of(new TemporalInterval(50, 75)), List.of(l12, l13));
//        Activity J1 = new Activity(36, "J1", 3, 2, List.of(new TemporalInterval(55, 80)), List.of(l13));
//        Activity K1 = new Activity(37, "K1", 1, 1, List.of(new TemporalInterval(60, 70)), List.of(l11));
//        Activity L1 = new Activity(38, "L1", 2, 2, List.of(new TemporalInterval(65, 85)), List.of(l13));
//        Activity M1 = new Activity(39, "M1", 3, 3, List.of(new TemporalInterval(70, 90)), List.of(l11, l12));
//        Activity N1 = new Activity(40, "N1", 2, 1, List.of(new TemporalInterval(75, 95)), List.of(l12));
//        Activity O1 = new Activity(41, "O1", 1, 2, List.of(new TemporalInterval(80, 100)), List.of(l13));
//        Activity P1 = new Activity(42, "P1", 2, 2, List.of(new TemporalInterval(0, 18)), List.of(l11));
//        Activity Q1 = new Activity(43, "Q1", 3, 3, List.of(new TemporalInterval(18, 40)), List.of(l12));
//        Activity R1 = new Activity(44, "R1", 2, 1, List.of(new TemporalInterval(40, 60)), List.of(l13));
//        Activity S1 = new Activity(45, "S1", 3, 3, List.of(new TemporalInterval(60, 88)), List.of(l12));
//        Activity T1 = new Activity(46, "T1", 1, 1, List.of(new TemporalInterval(5, 15)), List.of(l11));
//        Activity U1 = new Activity(47, "U1", 2, 2, List.of(new TemporalInterval(25, 50)), List.of(l13));
//        Activity V1 = new Activity(48, "V1", 3, 3, List.of(new TemporalInterval(50, 77)), List.of(l11));
//        Activity W1 = new Activity(49, "W1", 1, 2, List.of(new TemporalInterval(67, 83)), List.of(l12));
//        Activity X1 = new Activity(50, "X1", 2, 2, List.of(new TemporalInterval(10, 30)), List.of(l13));
//        Activity Y1 = new Activity(51, "Y1", 3, 3, List.of(new TemporalInterval(33, 66)), List.of(l11, l13));
//        Activity Z1 = new Activity(52, "Z1", 2, 2, List.of(new TemporalInterval(70, 98)), List.of(l12));
//        Activity AA1 = new Activity(53, "AA1", 3, 1, List.of(new TemporalInterval(28, 52)), List.of(l13));
//        Activity AB1 = new Activity(54, "AB1", 1, 1, List.of(new TemporalInterval(30, 49)), List.of(l12));
//        Activity AC1 = new Activity(55, "AC1", 1, 2, List.of(new TemporalInterval(39, 44)), List.of(l11));
//        Activity AD1 = new Activity(56, "AD1", 2, 2, List.of(new TemporalInterval(14, 42)), List.of(l13));
//        Activity AE1 = new Activity(57, "AE1", 3, 3, List.of(new TemporalInterval(1, 7)), List.of(l13, l11));
//        Activity AF1 = new Activity(58, "AF1", 1, 1, List.of(new TemporalInterval(62, 79)), List.of(l11, l12));
//        Activity AG1 = new Activity(59, "AG1", 2, 2, List.of(new TemporalInterval(55, 73)), List.of(l11, l12));
//        Activity AH1 = new Activity(60, "AH1", 2, 1, List.of(new TemporalInterval(11, 22)), List.of(l12, l13));
//        Activity AI1 = new Activity(61, "AI1", 2, 1, List.of(new TemporalInterval(45, 70)), List.of(l12));
//        Activity AJ1 = new Activity(62, "AJ1", 2, 1, List.of(new TemporalInterval(82, 99)), List.of(l13, l11));
//        Activity AK1 = new Activity(63, "AK1", 1, 2, List.of(new TemporalInterval(69, 85)), List.of(l13, l11));
//        Activity AL1 = new Activity(64, "AL1", 3, 3, List.of(new TemporalInterval(28, 39)), List.of(l13));
//        Activity AM1 = new Activity(65, "AM1", 3, 3, List.of(new TemporalInterval(82, 94)), List.of(l11));
//        Activity AN1 = new Activity(66, "AN1", 1, 2, List.of(new TemporalInterval(50, 56)), List.of(l12, l13));
//        Activity AO1 = new Activity(67, "AO1", 2, 1, List.of(new TemporalInterval(2, 10)), List.of(l11, l12));
//        Activity AP1 = new Activity(68, "AP1", 2, 3, List.of(new TemporalInterval(0, 9)), List.of(l13));
//        Activity AQ1 = new Activity(69, "AQ1", 1, 1, List.of(new TemporalInterval(36, 49)), List.of(l11));
//        Activity AR1 = new Activity(70, "AR1", 2, 1, List.of(new TemporalInterval(12, 23)), List.of(l11));
//        Activity AS1 = new Activity(71, "AS1", 1, 2, List.of(new TemporalInterval(38, 63)), List.of(l12));
//        Activity AT1 = new Activity(72, "AT1", 3, 3, List.of(new TemporalInterval(70, 88)), List.of(l13, l12));
//        Activity AU1 = new Activity(73, "AU1", 2, 2, List.of(new TemporalInterval(67, 79)), List.of(l11, l13));
//        Activity AV1 = new Activity(74, "AV1", 3, 1, List.of(new TemporalInterval(69, 77)), List.of(l13));
//        Activity AW1 = new Activity(75, "AW1", 3, 1, List.of(new TemporalInterval(26, 47)), List.of(l11, l13));

//        DistanceMatrix dm = new DistanceMatrix();
//        dm.addLocationsFromActivity(A);
//        dm.addLocationsFromActivity(B);
//        dm.addLocationsFromActivity(C);
//        dm.addLocationsWithDistance(l11, l12, 2);
//        //dm.addLocationsWithDistance(l12, l13, 3);
//        //dm.addLocationsWithDistance(l11, l13, 1);
//
//
//        List<ActivitySWO> Li = new ArrayList<>();
//        Li.add(A);
//        Li.add(B);
//        Li.add(C);
//        Li.add(D);
//        Li.add(E);
//        Li.add(F);
//        Li.add(G);
//        Li.add(H);
//        Li.add(I);
//        Li.add(J);//10
//        Li.add(K);
//        Li.add(L);
//        Li.add(M);
//        Li.add(N);
//        Li.add(O);
//        Li.add(P);
//        Li.add(Q);
//        Li.add(R);
//        Li.add(S);
//        Li.add(T);//20
//        Li.add(U);
//        Li.add(V);
//        Li.add(W);
//        Li.add(X);
//        Li.add(Y);
//        Li.add(Z);
//        Li.add(A1);
//        Li.add(B1);
//        Li.add(C1);
//        Li.add(D1);//30
//        Li.add(E1);
//        Li.add(F1);
//        Li.add(G1);
//        Li.add(H1);
//        Li.add(I1);
//        Li.add(J1);
//        Li.add(K1);
//        Li.add(L1);
//        Li.add(M1);
//        Li.add(N1);//40
//        Li.add(O1);
//        Li.add(P1);
//        Li.add(Q1);
//        Li.add(R1);
//        Li.add(S1);
//        Li.add(T1);
//        Li.add(U1);
//        Li.add(V1);
//        Li.add(W1);
//        Li.add(X1);//50
//        Li.add(Y1);
//        Li.add(Z1);
//        Li.add(AA1);
//        Li.add(AB1);
//        Li.add(AC1);
//        Li.add(AD1);
//        Li.add(AE1);
//        Li.add(AF1);
//        Li.add(AG1);
//        Li.add(AH1); //60
//        Li.add(AI1);
//        Li.add(AJ1);
//        Li.add(AK1);
//        Li.add(AL1);
//        Li.add(AM1);
//        Li.add(AN1);
//        Li.add(AO1);
//        Li.add(AP1);
//        Li.add(AQ1);
//        Li.add(AR1);//70
//        Li.add(AS1);
//        Li.add(AT1);
//        Li.add(AU1);
//        Li.add(AV1);
//        Li.add(AW1);//75
//        ScheduleSWO Sch = new ScheduleSWO(Li);
//        List<Constraint> lo = new ArrayList<>();
//        List<UnaryUtility> u = new ArrayList<>();
//        LinearActivityUtility u1 = new LinearActivityUtility(A, 1);
//        u.add(u1);
//        SWO swo = new SWO(Sch, lo, dm, u, new ArrayList<>());
//
//        OrderingConstraint o = new OrderingConstraint(A, B);
//        lo.add(o);
//        MinimumTemporalActivityDistanceConstraint o2 = new MinimumTemporalActivityDistanceConstraint(A, B, 3, dm);
//        lo.add(o2);
//
//        swo.setupActivitiesBeforeBeginOfAlgorithm();
//        swo.setupConstraintsForAllActivities();
//        swo.setupUtilitySourcesBeforeBeginOfAlgorithm();
//        swo.setupUtilityProvidersForEaseOfAcces();
//        long startTime = System.nanoTime();
//        swo.generateWithReorganisation();
//        long endTime = System.nanoTime();
//        long durationInMillis = (endTime - startTime) / 1_000_000;
//        System.out.println("Time taken for generateWithReorganisation and output: " + durationInMillis + " ms");
//        for (ActivitySWO act : swo.getP().getActivities()) {
//            System.out.println(act.getName() + " : " + act.getId());
//            for (ActivityPartSWO actp : act.getParts()) {
//                System.out.println(actp);
//            }
//        }
//        System.out.println("Total activity nr : " + Sch.getActivities().size());
//        System.out.println("Nr of scheduled: " + swo.getP().getActivities().size());
//        System.out.println("Total generated utility: " + swo.getUtilityOfP());
//        analyzeScheduleCoverage(swo.getP().getActivities());


        //ACTIVITY A
        LocationSWO l11 = new LocationSWO(1, "X");
        LocationSWO l12 = new LocationSWO(2, "Y");
        List<LocationSWO> l1 = new ArrayList<>();
        l1.add(l11);
        l1.add(l12);
        TemporalIntervalSWO t11 = new TemporalIntervalSWO(0, 25);
        TemporalIntervalSWO t12 = new TemporalIntervalSWO(30, 60);
        List<TemporalIntervalSWO> t1 = new ArrayList<>();
        t1.add(t11);
        t1.add(t12);
        ActivitySWO A = new ActivitySWO(1, "A", 4, 3, 3, 1, 999999999, 2, t1, l1);


        //ACTIVITY B
        List<LocationSWO> l2 = new ArrayList<>();
        l2.add(l11);
        TemporalIntervalSWO t22 = new TemporalIntervalSWO(2, 11);
        TemporalIntervalSWO t23 = new TemporalIntervalSWO(14, 19);
        List<TemporalIntervalSWO> t2 = new ArrayList<>();
        t2.add(t22);
        t2.add(t23);
        ActivitySWO B = new ActivitySWO(2, "B", 4, 2, t2, l2);


        //ACTIVITY D
        TemporalIntervalSWO t41 = new TemporalIntervalSWO(11, 14);
        List<TemporalIntervalSWO> t4 = new ArrayList<>();
        t4.add(t41);
        ActivitySWO D = new ActivitySWO(4, "D", 3, 3, t4, l1);


        //ACTIVITY E
        TemporalIntervalSWO t51 = new TemporalIntervalSWO(2, 3);
        List<TemporalIntervalSWO> t5 = new ArrayList<>();
        t5.add(t51);
        ActivitySWO E = new ActivitySWO(5, "E", 1, 1, t5, l1);


        //ACTIVITY F
        TemporalIntervalSWO t61 = new TemporalIntervalSWO(17, 19);
        List<TemporalIntervalSWO> t6 = new ArrayList<>();
        t6.add(t61);
        ActivitySWO F = new ActivitySWO(6, "F", 2, 2, t6, l1);

        //ACTIVITY C
        List<LocationSWO> l3 = new ArrayList<>();
        l3.add(l12);
        TemporalIntervalSWO t31 = new TemporalIntervalSWO(3, 19);
        List<TemporalIntervalSWO> t3 = new ArrayList<>();
        t3.add(t31);
        ActivitySWO C = new ActivitySWO(3, "C", 6, 3, 2, 1, 10, 0, t3, l3);

        DistanceMatrix dm = new DistanceMatrix();
        dm.addLocationsFromActivity(A);
        dm.addLocationsFromActivity(B);
        dm.addLocationsFromActivity(C);
        dm.addLocationsWithDistance(l11, l12, 2);

        List<ActivitySWO> L = new ArrayList<>();
        L.add(A);
        L.add(B);
        L.add(C);
        L.add(D);
        L.add(E);
        L.add(F);

        OrderingConstraint o = new OrderingConstraint(A, B);
        MinimumTemporalActivityDistanceConstraint tmin = new MinimumTemporalActivityDistanceConstraint(A, B, 3, dm);
        List<Constraint> lo = new ArrayList<>();
        lo.add(o);
        lo.add(tmin);
        ScheduleSWO S = new ScheduleSWO(L);
        LinearActivityUtility u1 = new LinearActivityUtility(A, 1);
        List<UnaryUtility> u = new ArrayList<>();
        u.add(u1);
        SWO swo = new SWO(S, lo, dm, u, new ArrayList<>());
        swo.setupActivitiesBeforeBeginOfAlgorithm();
        swo.setupConstraintsForAllActivities();
        swo.setupUtilitySourcesBeforeBeginOfAlgorithm();
        swo.setupUtilityProvidersForEaseOfAcces();
        long startTime = System.nanoTime();

        swo.generateWithReorganisation();
        long endTime = System.nanoTime();


        for (ActivitySWO act : swo.getP().getActivities()) {
            System.out.println(act.getName() + " : " + act.getId());
            for (ActivityPartSWO actp : act.getParts()) {
                System.out.println(actp);
            }
        }
    }



    public static void analyzeScheduleCoverage(List<ActivitySWO> activities) {
        int minStart = Integer.MAX_VALUE;
        int maxEnd = Integer.MIN_VALUE;
        int totalScheduledTime = 0;

        for (ActivitySWO activity : activities) {
            for (ActivityPartSWO part : activity.getParts()) {
                int start = part.getTij();
                int end = part.calculateEndTime();

                // Update global bounds
                if (start < minStart) minStart = start;
                if (end > maxEnd) maxEnd = end;

                // Accumulate scheduled duration
                totalScheduledTime += (end - start);
            }
        }

        if (minStart >= maxEnd) {
            System.out.println("No scheduled activities found.");
            return;
        }

        int totalInterval = maxEnd - minStart;
        double usageRatio = (double) totalScheduledTime / 100;

        System.out.println("\n--- Schedule Usage Analysis ---");
        System.out.println("Earliest start: " + minStart);
        System.out.println("Latest end: " + maxEnd);
        System.out.println("Total available interval: " + totalInterval);
        System.out.println("Total scheduled time: " + totalScheduledTime);
        System.out.printf("Usage ratio: %.2f%%\n", usageRatio * 100);
    }

}



