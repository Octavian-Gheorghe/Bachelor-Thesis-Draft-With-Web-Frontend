package org.example.Scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;
import org.example.Scheduler.Entity.Constraint.Binary.MaximumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Entity.Constraint.Binary.MinimumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Entity.Constraint.Binary.OrderingConstraint;
import org.example.Scheduler.Entity.Constraint.Constraint;
import org.example.Scheduler.Entity.Constraint.Unary.*;
import org.example.Scheduler.Entity.DistanceMatrix;
import org.example.Scheduler.Entity.ForwardCheckingUtils.Candidate;
import org.example.Scheduler.Entity.ForwardCheckingUtils.DomainContext;
import org.example.Scheduler.Entity.Location;
import org.example.Scheduler.Entity.Schedule;
import org.example.Scheduler.Entity.TemporalInterval;
import org.example.Scheduler.Entity.Utility.Binary.*;
import org.example.Scheduler.Entity.Utility.Unary.ActivityDurationUtility;
import org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility.DomainActivityUtility;
import org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility.LinearActivityUtility;
import org.example.Scheduler.Entity.Utility.Unary.DomainActivityUtility.StepwiseActivityUtility;
import org.example.Scheduler.Entity.Utility.Unary.PartDistanceUtility.MaximumPartDistanceUtility;
import org.example.Scheduler.Entity.Utility.Unary.PartDistanceUtility.MinimumPartDistanceUtility;
import org.example.Scheduler.Entity.Utility.Unary.PartDistanceUtility.PartDistanceUtility;
import org.example.Scheduler.Entity.Utility.Unary.UnaryUtility;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SWO
{
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    private Schedule S;
    private Schedule P;
    private Schedule scheduled;
    private Schedule notScheduled;
    private double UtilityOfP;
    private DistanceMatrix matrix;

    private List<Activity> work;

    private List<UnaryUtility> allUnaryUtilitySources; // both preferences and hard constraints;
    private Map<Integer, List<UnaryUtility>> allUnaryUtilitySourcesForActivity;
    private List<BinaryUtility> allBinaryUtilitySources; // both preferences and hard constraints
    private Map<Integer, List<BinaryUtility>> allBinaryUtilitySourcesForActivity;

    private List<Constraint> allConstraints;// used only to get stuff inside the object
    private Map<Integer, Map<String, List<Constraint>>> allConstraintsForActivity;

    public SWO(Schedule s, List<Constraint> allConstraints, DistanceMatrix matrix, List<UnaryUtility> allUnaryUtilitySources, List<BinaryUtility> allBinaryUtilitySources)
    {
        this.S = s;
        this.allConstraints = allConstraints;
        this.allUnaryUtilitySources = allUnaryUtilitySources;
        this.allBinaryUtilitySources = allBinaryUtilitySources;
        this.matrix = matrix;
        List<Activity> activitiesScheduled = new ArrayList<>();
        this.scheduled = new Schedule();
        scheduled.setActivities(activitiesScheduled);
        List<Activity> activitiesP = new ArrayList<>();
        this.P = new Schedule();
        P.setActivities(activitiesP);
        this.UtilityOfP = -1;
        List<Activity> activities = new ArrayList<>();
        this.notScheduled = new Schedule();
        notScheduled.setActivities(activities);
        allConstraintsForActivity = new LinkedHashMap<>();
        allBinaryUtilitySourcesForActivity = new LinkedHashMap<>();
        allUnaryUtilitySourcesForActivity = new LinkedHashMap<>();
        for(Activity a : s.getActivities())
        {
            String constraintIndexUnary = "unary";
            List<Constraint> constraintListUnary = new ArrayList<>();
            String constraintIndexBinary = "binary";
            List<Constraint> constraintListBinary = new ArrayList<>();
            LinkedHashMap hashmapOfAllConstraintsForActivity = new LinkedHashMap();
            hashmapOfAllConstraintsForActivity.put(constraintIndexUnary, constraintListUnary);
            hashmapOfAllConstraintsForActivity.put(constraintIndexBinary, constraintListBinary);
            allConstraintsForActivity.put(a.getId(),hashmapOfAllConstraintsForActivity);
            allUnaryUtilitySourcesForActivity.put(a.getId(), new ArrayList<>());
            allBinaryUtilitySourcesForActivity.put(a.getId(), new ArrayList<>());
        }
    }

    public void setupConstraintsForAllActivities()
    {
        for(Constraint c : allConstraints)
        {
            if (c instanceof OrderingConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested1();
                allConstraintsForActivity.get(a1.getId()).get("binary").add(c);
            }
            else if (c instanceof MinimumTemporalActivityDistanceConstraint oc)
            {
                Activity a1 = oc.getA1();
                Activity a2 = oc.getA2();
                allConstraintsForActivity.get(a1.getId()).get("binary").add(c);
                allConstraintsForActivity.get(a2.getId()).get("binary").add(c);
            }
            else if (c instanceof MaximumTemporalActivityDistanceConstraint oc)
            {
                Activity a1 = oc.getA1();
                Activity a2 = oc.getA2();
                allConstraintsForActivity.get(a1.getId()).get("binary").add(c);
                allConstraintsForActivity.get(a2.getId()).get("binary").add(c);
            }
            else if (c instanceof ActivityPartDurationConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof ActivityPartTemporalDomainConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof DurationRangeConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof MaximumTemporalPartDistanceConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof MinimumTemporalPartDistanceConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof PartDurationRangeConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
        }
        System.out.println("Setup of constraints finished, apparently!");
    }

    public void setupActivitiesBeforeBeginOfAlgorithm()
    {
        int nrOfActivities = getS().getActivities().size();
        for(int i = 0 ; i < nrOfActivities ; i++)
        {
            Activity act = getS().getActivities().get(i);
            ActivityPartTemporalDomainConstraint d1 = new ActivityPartTemporalDomainConstraint(act);
            MinimumTemporalPartDistanceConstraint d2 = new MinimumTemporalPartDistanceConstraint(act, matrix, act.getDmini());
            MaximumTemporalPartDistanceConstraint d3 = new MaximumTemporalPartDistanceConstraint(act, act.getDmaxi());
            PartDurationRangeConstraint d4 = new PartDurationRangeConstraint(act);
            for(int j = i + 1 ; j < nrOfActivities ; j++)
            {
                Activity otherAct = getS().getActivities().get(j);
                MinimumTemporalActivityDistanceConstraint d5 = new MinimumTemporalActivityDistanceConstraint(act, otherAct, 0, matrix);
                allConstraints.add(d5);
            }
            allConstraints.add(d1);
            allConstraints.add(d2);
            allConstraints.add(d3);
            allConstraints.add(d4);
        }
    }

    public void setupUtilitySourcesBeforeBeginOfAlgorithm()
    {
        int nrOfActivities = getS().getActivities().size();
        for(int i = 0 ; i < nrOfActivities ; i++)
        {
            Activity act = getS().getActivities().get(i);
            ActivityDurationUtility u1 = new ActivityDurationUtility(act,act.getDurimin(),act.getDurimax(),0.2,0.8);
            MinimumPartDistanceUtility u2 = new MinimumPartDistanceUtility(act, act.getDmini());
            MaximumPartDistanceUtility u3 = new MaximumPartDistanceUtility(act, act.getDmaxi());
            allUnaryUtilitySourcesForActivity.get(act.getId()).add(u1);
            allUnaryUtilitySourcesForActivity.get(act.getId()).add(u2);
            allUnaryUtilitySourcesForActivity.get(act.getId()).add(u3);
        }
        for(Constraint c : allConstraints)
        {
            if (c instanceof OrderingConstraint oc)
            {
                Activity a1 = oc.getActivityToBeTested1();
                Activity a2 = oc.getActivityToBeTested2();
                allBinaryUtilitySources.add(new OrderingPreferenceUtility(a1, a2));
            }
            else if (c instanceof MinimumTemporalActivityDistanceConstraint oc)
            {
                Activity a1 = oc.getA1();
                Activity a2 = oc.getA2();
                Integer pdmin = oc.getDmin();
                allBinaryUtilitySources.add(new MinimumActivitiesPartDistanceUtility(a1, a2, pdmin));
            }
            else if (c instanceof MaximumTemporalActivityDistanceConstraint oc)
            {
                Activity a1 = oc.getA1();
                Activity a2 = oc.getA2();
                Integer pdmin = oc.getDmax();
                allBinaryUtilitySources.add(new MaximumActivitiesPartDistanceUtility(a1, a2, pdmin));
            }
        }
    }

    public void setupUtilityProvidersForEaseOfAcces()
    {
        for(UnaryUtility u : allUnaryUtilitySources)
        {
            if(u instanceof ActivityDurationUtility)
            {
                Activity act =  ((ActivityDurationUtility) u).getA();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof LinearActivityUtility)
            {
                Activity act = ((LinearActivityUtility) u).getA();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof StepwiseActivityUtility)
            {
                Activity act = ((StepwiseActivityUtility) u).getA();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof MaximumPartDistanceUtility)
            {
                Activity act = ((MaximumPartDistanceUtility) u).getActivity();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof MinimumPartDistanceUtility)
            {
                Activity act = ((MinimumPartDistanceUtility) u).getActivity();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
        }
        for(BinaryUtility u : allBinaryUtilitySources)
        {
            if(u instanceof ImplicationUtility)
            {
                Activity act = ((ImplicationUtility) u).getA2();
                allBinaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if(u instanceof MaximumActivitiesPartDistanceUtility)
            {
                Activity act1 = ((MaximumActivitiesPartDistanceUtility) u).getA();
                Activity act2 = ((MaximumActivitiesPartDistanceUtility) u).getB();
                allBinaryUtilitySourcesForActivity.get(act1.getId()).add(u);
                allBinaryUtilitySourcesForActivity.get(act2.getId()).add(u);
            }
            else if(u instanceof MinimumActivitiesPartDistanceUtility)
            {
                Activity act1 = ((MinimumActivitiesPartDistanceUtility) u).getA();
                Activity act2 = ((MinimumActivitiesPartDistanceUtility) u).getB();
                allBinaryUtilitySourcesForActivity.get(act1.getId()).add(u);
                allBinaryUtilitySourcesForActivity.get(act2.getId()).add(u);
            }
            else if(u instanceof OrderingPreferenceUtility)
            {
                Activity act = ((OrderingPreferenceUtility) u).getA();
                allBinaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
        }
    }

    public double calculateUtility()
    {
        double totalUtil = 0.0;
        for(Activity a : scheduled.getActivities())
        {
            {
                totalUtil += calculateUtilityForActivity(a);
            }
        }
        return totalUtil;
    }

    public double calculateUtility(Activity a)
    {
        a.setDuri(0);
        for(ActivityPart ap : a.getParts())
        {
            a.setDuri(a.getDuri() + ap.getDurij()) ;
        }
        scheduled.addAnotherActivity(a);
        double util = calculateUtility();
        scheduled.getActivities().remove(a);
        return util;
    }


    public double calculateUtilityForActivity(Activity a)
    {
        double totalUtil = 0.0;
        System.out.println(totalUtil);
        List<ActivityPart> actParts = a.getParts();
        int duration = a.getDuri();
        Activity A = S.getViaId(a.getId());
        A.setDuri(duration);
        List<UnaryUtility> alLUtilitiesForA = allUnaryUtilitySourcesForActivity.get(a.getId());
        for(UnaryUtility u : alLUtilitiesForA)
        {
            System.out.println(u.computeUtility(actParts));
            System.out.println(u.getClass().toString());
            totalUtil += u.computeUtility(actParts);
        }
        System.out.println(totalUtil);
        List<BinaryUtility> allUtilitiesForAAndActivity = allBinaryUtilitySourcesForActivity.get(a.getId());
        Set<Integer> scheduledIds = new HashSet<>();
        for (Activity scheduled : scheduled.getActivities())
            scheduledIds.add(scheduled.getId());
        for(BinaryUtility u : allUtilitiesForAAndActivity) {
            if (u instanceof ImplicationUtility u1) {
                int otherId = (Objects.equals(u1.getA1().getId(), A.getId())) ? u1.getA2().getId() : u1.getA1().getId();

                if (!scheduledIds.contains(otherId))
                    continue;

                Activity temp = scheduled.getViaId(otherId);
                Activity original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u1.computeUtility(actParts, original2.getParts());

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            } else if (u instanceof MinimumActivitiesPartDistanceUtility u2) {
                int otherId = (Objects.equals(u2.getA().getId(), A.getId())) ? u2.getB().getId() : u2.getA().getId();

                if (!scheduledIds.contains(otherId))
                    continue;

                Activity temp = scheduled.getViaId(otherId);
                Activity original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u2.computeUtility(actParts, original2.getParts());

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            } else if (u instanceof MaximumActivitiesPartDistanceUtility u3) {
                int otherId = (Objects.equals(u3.getA().getId(), A.getId())) ? u3.getB().getId() : u3.getA().getId();

                if (!scheduledIds.contains(otherId))
                    continue;

                Activity temp = scheduled.getViaId(otherId);
                Activity original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u3.computeUtility(actParts, original2.getParts());

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            } else if (u instanceof OrderingPreferenceUtility u4) {
                int otherId = (Objects.equals(u4.getA().getId(), A.getId())) ? u4.getB().getId() : u4.getA().getId();

                if (!scheduledIds.contains(otherId))
                    continue;

                Activity temp = scheduled.getViaId(otherId);
                Activity original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u4.computeUtility(actParts, original2.getParts());

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            }
        }
        System.out.println(totalUtil);
        System.out.println();
        A.setDuri(0);
        A.setParts(new ArrayList<>());
        return totalUtil;
    }

    public double estimateUtility(List<Activity> work)
    {
        double totalUtil = 0.0;
        Set<Integer> scheduledIds = new HashSet<>();
        for (Activity scheduled : scheduled.getActivities())
            scheduledIds.add(scheduled.getId());
        for(Activity a : work)
        {
            totalUtil += estimateUtilityForActivity(a, work);
            List<BinaryUtility> allBinariesForAct = allBinaryUtilitySourcesForActivity.get(a.getId());
            for(BinaryUtility b : allBinariesForAct)
            {
                if(b instanceof ImplicationUtility)
                {
                    int otherId = (Objects.equals(((ImplicationUtility) b).getA2().getId(), a.getId())) ? ((ImplicationUtility) b).getA1().getId() : -1;
                    if(otherId == -1)
                        continue;
                    if(scheduledIds.contains(otherId))
                        totalUtil += 1;
                }
            }
        }
        System.out.println(totalUtil);
        return totalUtil;
    }

    public Map<Integer, Double> estimatedUtilitySnapshot(List<Activity> work)
    {
        Map<Integer, Double> snapshot = new HashMap<>();
        for(Activity a : work)
        {
            snapshot.put(a.getId(), estimateUtilityForActivity(a, work));
        }
        return snapshot;
    }

    public double estimateUtilityForActivity(Activity work, List<Activity> allWork)
    {
        double maxUtil = 0.0;
        Activity A = S.getViaId(work.getId());
        List<TemporalInterval> originalActivityTemporalDomain = A.getF();
        A.setF(work.getF());
        List<UnaryUtility> alLUtilitiesForA = allUnaryUtilitySourcesForActivity.get(work.getId());
        for(UnaryUtility u : alLUtilitiesForA)
        {
            if(u instanceof DomainActivityUtility)
            {
                double utilMin = ((DomainActivityUtility) u).calculateActivityForBestCaseScenario(A.getDurimin());
                double utilMax = ((DomainActivityUtility) u).calculateActivityForBestCaseScenario(A.getDurimax());
                maxUtil = Math.max(maxUtil,((utilMax + utilMin)/2));
            }
            else if(u instanceof PartDistanceUtility)
            {
                double utilMin = ((PartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin());
                double utilMax = ((PartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax());
                maxUtil = Math.max(maxUtil,((utilMax + utilMin)/2));
            }
        }
        List<BinaryUtility> allUtilitiesForAAndActivity = allBinaryUtilitySourcesForActivity.get(work.getId());

        for(BinaryUtility u : allUtilitiesForAAndActivity) {
             if (u instanceof MinimumActivitiesPartDistanceUtility u2) {
                 int otherId = (Objects.equals(u2.getA().getId(), A.getId())) ? u2.getB().getId() : u2.getA().getId();

                 for(Activity act : allWork)
                 {
                     if(act.getId() == otherId)
                     {
                         Activity original2 = S.getViaId(otherId);
                         List<TemporalInterval> originalDomain = original2.getF();
                         original2.setF(act.getF());
                         double utilMinMin = ((MinimumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin(), original2.getDurimin());
                         double utilMaxMin = ((MinimumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax(), original2.getDurimin());
                         double utilMinMax = ((MinimumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin(), original2.getDurimax());
                         double utilMaxMax = ((MinimumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax(), original2.getDurimax());
                         original2.setF(originalDomain);
                         maxUtil = Math.max(maxUtil,((utilMinMin + utilMaxMin + utilMinMax + utilMaxMax)/4));
                         break;
                     }
                 }
            } else if (u instanceof MaximumActivitiesPartDistanceUtility u3) {
                 int otherId = (Objects.equals(u3.getA().getId(), A.getId())) ? u3.getB().getId() : u3.getA().getId();
                 for(Activity act : allWork)
                 {
                     if(act.getId() == otherId)
                     {
                         Activity original2 = S.getViaId(otherId);
                         List<TemporalInterval> originalDomain = original2.getF();
                         original2.setF(act.getF());
                         double utilMinMin = ((MaximumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin(), original2.getDurimin());
                         double utilMaxMin = ((MaximumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax(), original2.getDurimin());
                         double utilMinMax = ((MaximumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin(), original2.getDurimax());
                         double utilMaxMax = ((MaximumActivitiesPartDistanceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax(), original2.getDurimax());
                         original2.setF(originalDomain);
                         maxUtil = Math.max(maxUtil,((utilMinMin + utilMaxMin + utilMinMax + utilMaxMax)/4));
                         break;
                     }
                 }
            } else if (u instanceof OrderingPreferenceUtility u4) {
                 int otherId = (Objects.equals(u4.getA().getId(), A.getId())) ? u4.getB().getId() : -1;
                 if(otherId == -1)
                     continue;
                 for(Activity act : allWork)
                 {
                     if(act.getId() == otherId)
                     {
                         Activity original2 = S.getViaId(otherId);
                         List<TemporalInterval> originalDomain = original2.getF();
                         original2.setF(act.getF());
                         double utilMinMin = ((OrderingPreferenceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin(), original2.getDurimin());
                         double utilMaxMin = ((OrderingPreferenceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax(), original2.getDurimin());
                         double utilMinMax = ((OrderingPreferenceUtility) u).calculateActivityForBestCaseScenario(A.getDurimin(), original2.getDurimax());
                         double utilMaxMax = ((OrderingPreferenceUtility) u).calculateActivityForBestCaseScenario(A.getDurimax(), original2.getDurimax());
                         original2.setF(originalDomain);
                         maxUtil = Math.max(maxUtil,((utilMinMin + utilMaxMin + utilMinMax + utilMaxMax)/4));
                         break;
                     }
                 }
            }
        }
        A.setF(originalActivityTemporalDomain);
        A.setDuri(0);
        A.setParts(new ArrayList<>());
        return maxUtil;
    }

    private void scheduleInterruptible(Activity act)
    {
        act.sortTemporalIntervalsByStart();
        DomainContext nonConstrCtx = new DomainContext();
        DomainContext constraintCtx = null;
        boolean success = true;
        int totalDur = 0;
        int partId   = 1;

        while (totalDur < act.getDurimin())
        {
            Candidate cand = pickBestPart(act, work, matrix, allConstraints, nonConstrCtx);
            if (cand == null)
            {
                success = false;
                break;
            }

            forwardCheck(act, cand.ti(), cand.dur(), cand.loc(), work, matrix, List.of(), nonConstrCtx);
            exclude(act, cand.ti(), cand.ti() + cand.dur() + act.getDmini(), nonConstrCtx);
            act.sortTemporalIntervalsByStart();

            if (constraintCtx == null)
                constraintCtx = new DomainContext();
            forwardCheck(act, cand.ti(), cand.dur(), cand.loc(), work, matrix, allConstraints, constraintCtx);

            act.getParts().add(new ActivityPart(partId++, cand.ti(), cand.dur(), cand.loc()));
            act.sortActivityPartsByStart();
            totalDur += cand.dur();

            if (totalDur >= act.getDurimin())
            {
                constraintCtx = null;
            }

            if (totalDur >= act.getDurimax())
            {
                break;
            }
            if (totalDur >= act.getDurimin())
            {
                break;
            }
        }

        if (!success || totalDur < act.getDurimin())
        {
            nonConstrCtx.close();
            if (constraintCtx != null)
                constraintCtx.close();
            act.getParts().clear();
            notScheduled.getActivities().add(act);
        }
        else
        {
            act.setDuri(totalDur);
            act.setScheduled(true);
            act.setPi(act.getParts().size());
            scheduled.getActivities().add(act);
        }

/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========

/// todo
        //option 1 - difficulty
        //double score = difficultySum(work);


        //option 2 - estimated utility/difficulty;
        double score = estimateUtility(work)/difficultySum(work);


        //option 3 - utility/difficulty;
        //double score = calculateUtility(act)/difficultySum(work);



/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========

        while (totalDur < act.getDurimax())
        {
            Candidate cand = pickBestPart(act, work, matrix, allConstraints, nonConstrCtx);
            if (cand == null)
                break;
            try (DomainContext probe = new DomainContext())
            {
                commitPart(act, cand, -1, probe);
                commitPartWithCons(act, cand, probe);

/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========
                double difficulty = difficultySum(work);


                //todo

                //option 1 - difficulty
                //double currentScore = difficulty;

                //option 2 - estimated utility/difficulty;
                double currentScore = estimateUtility(work)/difficultySum(work);

                //option 3 - utility/difficulty;
                //double currentScore = calculateUtility(act)/difficultySum(work);


                if (difficulty < 0)
                    break;

                //option 1 - difficulty
                  if(currentScore > score)
                        break;



                //option 2 - estimated utility/difficulty;
//                if(score < currentScore)
//                    break;


                //option 3 - utility/difficulty;
//                if(score < currentScore)
//                    break;

                commitPart(act, cand, partId++, nonConstrCtx);
                commitPartWithCons(act, cand, nonConstrCtx);

                ///currentScore = score;
                score  = currentScore;
                totalDur += cand.dur();
/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========
            }
        }
    }

    private Candidate pickBestPart(Activity act, List<Activity> queue, DistanceMatrix dm, List<Constraint> cons, DomainContext baseCtx)
    {
        act.sortTemporalIntervalsByStart();
/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========
        //double bestScore = Double.POSITIVE_INFINITY;
        double bestScore = Double.MIN_VALUE;
/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========

        Integer bestDur = null;
        Integer bestTi = null;
        Location bestLoc = null;
        LOGDisplayCurrentActivityParts(act);

        for (int dur = act.getSmaxi(); dur >= act.getSmini(); dur--)
        {
            boolean valid = act.isActivityDurationFeasable(dur);
            if (!valid)
                continue;

            DomainContext base = new DomainContext();
            for (ActivityPart p : act.getParts())
            {
                exclude(act, p.getTij(), p.calculateEndTime() + act.getDmini(), base);
            }
            for (int ti : act.getAllAvailableStartTimes(dur))
            {
                for (Location loc : act.getLoci())
                {
                    System.out.println("We will try to add an activity part for " + act.getName() + " with id: " + act.getId() + " at " + ti + " with duration " + dur + " at Location " + loc.getLocationName());
                    try (DomainContext trialCtx = new DomainContext())
                    {
                        LOGDisplayCurrentyPropagatedInterruptibleActivityTimeInterval(act);

                        forwardCheck(act, ti, dur, loc, queue, dm, cons, trialCtx);
                        sortActivitiesTemporalDomainsInPriorityQueu(work);

                        LOGCurrentlyPropagatedPriorityScheduleActivities(work);



/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========

                        double difficulty = difficultySum(queue);

                        if(difficulty < 0)
                            break;


                        System.out.println("Difficulty is good");
                        System.out.println("We are about to check the constraints...");

                        if(!candidateRespectsConstraints(act, ti, dur, loc))
                            continue;

//todo
                        //option 1 - difficulty
                        //double score = difficulty;

                        //option 2 -estimated utility/difficulty
                        double estimateUtility = estimateUtility(work);
                        double score = estimateUtility/difficulty;

                        //option 3 - utility/difficulty
//                        double estimateUtility = calculateUtility(act);
//                        double score = estimateUtility/difficulty;


                        if (score > bestScore)
                        //if(score < bestScore)
                        {
                            bestScore = score;
                            bestDur = dur;
                            bestTi = ti;
                            bestLoc = loc;
                        }
/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========

                    }
                }
            }
            base.close();
        }
        return (bestLoc == null) ? null : new Candidate(bestDur, bestTi, bestLoc);
    }

    private void scheduleNonInterruptible(Activity act)
    {
        act.sortTemporalIntervalsByStart();
        //double bestScore = Double.POSITIVE_INFINITY;
        double bestScore = Double.MIN_VALUE;
        int bestDur = -1;
        int bestTi = -1;
        Location bestLoc = null;

        for (int dur = act.getDurimax(); dur >= act.getDurimin(); dur--)
            for (int ti : act.getAllAvailableStartTimes(dur))
                for (Location loc : act.getLoci())
                {
                    LOGCurrentActivitySchedullingOptions(act, dur, ti, loc);
                    try (DomainContext ctx = new DomainContext())
                    {
                        forwardCheck(act, ti, dur, loc, work, matrix, allConstraints, ctx);
                        sortActivitiesTemporalDomainsInPriorityQueu(work);
                        LOGCurrentlyPropagatedPriorityScheduleActivities(work);

/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========



                        double difficulty = difficultySum(work);
                        if(difficulty < 0)
                            break;

                        if(!candidateRespectsConstraints(act, ti, dur, loc))
                            continue;


                        //todo
                        //option 1 - difficulty
                        //double score = difficulty;

                        //option 2 -estimated utility/difficulty
                        double estimateUtility = estimateUtility(work);
                        double score = estimateUtility/difficulty;

                        //option 3 - utility/difficulty
//                        double estimateUtility = calculateUtility(act);
//                        double score = estimateUtility/difficulty;

                        if (score > bestScore)
                        //if(score < bestScore)
                        {
                            //bestScore = score;
                            bestScore = score;
                            bestDur = dur;
                            bestTi = ti;
                            bestLoc = loc;
                        }
/// =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== =========== ===========

                    }
                }

        if (bestLoc == null)
        {
            notScheduled.getActivities().add(act);
            return;
        }

        DomainContext commit = new DomainContext();
        forwardCheck(act, bestTi, bestDur, bestLoc, work, matrix, allConstraints, commit);

        act.setScheduled(true);
        act.setDuri(bestDur);
        act.getParts().add(new ActivityPart(1, bestTi, bestDur, bestLoc));
        act.setPi(1);
        scheduled.getActivities().add(act);
    }

    private void commitPart(Activity act, Candidate c, int id, DomainContext ctx)
    {
        forwardCheck(act, c.ti(), c.dur(), c.loc(), work, matrix, List.of(), ctx);
        exclude(act, c.ti(), c.ti() + c.dur(), ctx);
        if (id > 0)
            act.getParts().add(new ActivityPart(id, c.ti(), c.dur(), c.loc()));
        act.sortTemporalIntervalsByStart();
    }

    private void commitPartWithCons(Activity act, Candidate c, DomainContext ctx)
    {
        forwardCheck(act, c.ti(), c.dur(), c.loc(), work, matrix, allConstraints, ctx);
    }

    private double difficultySum(List<Activity> list)
    {
        double sum = 0.0;
        for (Activity a : list)
        {
            a.calculateDifficulty();
            LOGDifficultyOfPropagatedPriorityScheduleActivity(a);
            if (a.getDifficulty() > 1.0)
                return -1.0;
            sum += a.getDifficulty();
        }
        return sum;
    }

    void forwardCheck(Activity fixed, int ti, int dur, Location li, List<Activity> remaining, DistanceMatrix dm, List<Constraint> constraints, DomainContext ctx)
    {
        int fixedEnd = ti + dur;
        for (Activity tj : remaining)
        {
            Location closest = dm.closestDistancedLocation(li, tj);
            int travel = dm.Dist(li, closest);

            List<Constraint> pairCons = collectConstraints(constraints, fixed, tj);

            if (pairCons.isEmpty())
            {
                exclude(tj, ti, fixedEnd + travel, ctx);
                continue;
            }

            for (Constraint c : pairCons)
            {
                if (c instanceof OrderingConstraint)
                {
                    int domMin = tj.getF().stream().mapToInt(TemporalInterval::getAi).min().orElseThrow();
                    exclude(tj, domMin, fixedEnd + travel, ctx);
                }

                else if (c instanceof MinimumTemporalActivityDistanceConstraint min) {
                    int dmin = min.getDmin();
                    exclude(tj, ti - dmin, fixedEnd + Math.max(travel, dmin), ctx);
                }

                else if (c instanceof MaximumTemporalActivityDistanceConstraint max) {
                    int dmax   = max.getDmax();
                    int domMin = tj.getF().stream().mapToInt(TemporalInterval::getAi).min().orElseThrow();
                    int domMax = tj.getF().stream().mapToInt(TemporalInterval::getBi).max().orElseThrow();

                    exclude(tj, domMin,ti - dmax,ctx);
                    exclude(tj, ti, fixedEnd + travel, ctx);
                    exclude(tj, fixedEnd + travel + dmax, domMax, ctx);
                }
            }
        }
    }

    private List<Constraint> collectConstraints(List<Constraint> list, Activity a, Activity b)
    {
        List<Constraint> result = new ArrayList<>();
        for (Constraint c : allConstraintsForActivity.get(a.getId()).get("binary"))
        {
            if (c instanceof OrderingConstraint oc)
            {
                if (Objects.equals(oc.getActivityToBeTested1().getId(), a.getId()) && Objects.equals(oc.getActivityToBeTested2().getId(), b.getId()))
                {
                    result.add(c);
                }
            }

            else if (c instanceof MinimumTemporalActivityDistanceConstraint min)
            {
                if ((Objects.equals(min.getA1().getId(), a.getId()) && Objects.equals(min.getA2().getId(), b.getId())) || (Objects.equals(min.getA1().getId(), b.getId()) && Objects.equals(min.getA2().getId(), a.getId())))
                {
                    result.add(c);
                }
            }

            else if (c instanceof MaximumTemporalActivityDistanceConstraint max)
            {
                if ((Objects.equals(max.getA1().getId(), a.getId()) && Objects.equals(max.getA2().getId(), b.getId())) || (Objects.equals(max.getA1().getId(), b.getId()) && Objects.equals(max.getA2().getId(), a.getId())))
                {
                    result.add(c);
                }
            }
        }
        return result;
    }

    private void exclude(Activity act, int L, int R, DomainContext ctx)
    {
        List<TemporalInterval> toAdd = new ArrayList<>();
        List<TemporalInterval> toRemove = new ArrayList<>();

        Iterator<TemporalInterval> it = act.getF().iterator();
        while (it.hasNext())
        {
            TemporalInterval cur = it.next();

            if (R <= cur.getAi() || L >= cur.getBi())
                continue;
            List<TemporalInterval> kept = subtract(cur, L, R);

            it.remove();
            toRemove.add(cur);

            toAdd.addAll(kept);
        }

        act.getF().addAll(toAdd);

        for (TemporalInterval r : toRemove)
            ctx.recordRemove(act, r);
        for (TemporalInterval k : toAdd)
            ctx.recordAdd(act, k);
    }

    static List<TemporalInterval> subtract(TemporalInterval src, int cutL, int cutR)
    {
        if (cutR <= src.getAi() || cutL >= src.getBi())
            return List.of(src);

        List<TemporalInterval> out = new ArrayList<>(2);
        if (cutL > src.getAi())
            out.add(new TemporalInterval(src.getAi(), cutL));
        if (cutR < src.getBi())
            out.add(new TemporalInterval(cutR, src.getBi()));
        return out;
    }

    private void sortActivitiesTemporalDomainsInPriorityQueu(List<Activity> as)
    {
        for(Activity a : work)
        {
            a.sortTemporalIntervalsByStart();
        }
    }

    private static List<Activity> deepCopyActivities(List<Activity> src)
    {
        List<Activity> out = new ArrayList<>(src.size());
        for (Activity a : src) out.add(copyActivity(a));
        return out;
    }

    private static Activity copyActivity(Activity o)
    {
        Activity x = new Activity();
        x.setId(o.getId());
        x.setName(o.getName());
        x.setDurimin(o.getDurimin());
        x.setDurimax(o.getDurimax());
        x.setDmini(o.getDmini());
        x.setDmaxi(o.getDmaxi());
        x.setSmini(o.getSmini());
        x.setSmaxi(o.getSmaxi());
        x.setIsInterruptible(o.getIsInterruptible());
        x.setDifficulty(o.getDifficulty());

        List<TemporalInterval> fCopy = new ArrayList<>();
        for (TemporalInterval t : o.getF())
            fCopy.add(new TemporalInterval(t.getAi(), t.getBi()));
        x.setF(fCopy);

        x.setParts(new ArrayList<>());
        x.setLoci(new ArrayList<>(o.getLoci()));
        return x;
    }


    private void LOGCurrentlyFinishedActivities(List<Activity> work)
    {
        System.out.println("Currently, the following activities are done processing: ");
        for(Activity a : work)
        {
            System.out.println("Activity " + a.getName() +  " : " + a.getId());
            for(ActivityPart part : a.getParts())
            {
                System.out.println(part);
            }
        }
    }

    private void LOGDifficultyOfPropagatedPriorityScheduleActivity(Activity a)
    {
        System.out.println(PURPLE +"For acitvity with id: " + a.getId() + " and name " + a.getName() + " the difficulty is " + a.getDifficulty() + " and wether it is a problem or not, the answwe is " + (a.getDifficulty() > 1));
    }

    private void LOGCurrentlyPropagatedPriorityScheduleActivities(List<Activity> as)
    {
        for(Activity a : as)
        {
            System.out.println(BLUE + "For activity with id: " + a.getId() + " and name " + a.getName() + " the currently propagated domains are:" );
            for(TemporalInterval t : a.getF())
            {
                System.out.println(BLUE + t.getAi() + " - " + t.getBi());
            }
        }
    }

    private void LOGCurrentActivitySchedullingOptions(Activity a, int dur, int t, Location l)
    {
        System.out.println(YELLOW + "Activity with id: " + a.getId() + " and name " + a.getName() + " can start at time " + t + " whilst having duration " + dur + " at location " + l.getLocationName());
    }

    private void LOGDisplayCurrentActivityParts(Activity a)
    {
        System.out.println(GREEN + "Currently, for the activity " + a.getName() + " with id: " + a.getId() + " we have these parts: ");
        for(ActivityPart act : a.getParts())
        {
            System.out.println(GREEN + act);
        }
    }

    private void LOGDisplayCurrentyPropagatedInterruptibleActivityTimeInterval(Activity a)
    {
        System.out.println(RED + "Currently, for the activity " + a.getName() + " with id: " + a.getId() + " we have these temporal Intervals: ");
        for(TemporalInterval act : a.getF())
        {
            System.out.println(RED + act.getAi() + " - " + act.getBi());
        }
    }

    private boolean candidateRespectsConstraints(Activity actCopy, int ti, int dur, Location loc)
    {
        Activity original = S.getActivities().stream().filter(a -> Objects.equals(a.getId(), actCopy.getId())).findFirst().orElse(null);

        if (original == null)
            return false;

        ActivityPart probe = new ActivityPart(original.getParts().size() + 1, ti, dur, loc);

        int oldDuri = original.getDuri();
        for(ActivityPart a : actCopy.getParts())
        {
            System.out.println(a);
            original.setDuri(original.getDuri() + a.getDurij());
            original.getParts().add(a);
        }
        original.getParts().add(probe);
        original.sortActivityPartsByStart();
        original.setDuri(dur);
        boolean ok = true;

        List<Constraint> unaryCons = allConstraintsForActivity.get(original.getId()).get("unary");

        for (Constraint c : unaryCons)
        {
            if (c instanceof ActivityPartTemporalDomainConstraint || c instanceof PartDurationRangeConstraint  || c instanceof MaximumTemporalPartDistanceConstraint || c instanceof MinimumTemporalPartDistanceConstraint)
            {
                if (!c.eval())
                {
                    System.out.println(c.getClass().toString() + " failed for " + actCopy.getName());
                    ok = false;
                    break;
                }
            }
        }

        if (ok)
        {
            Set<Integer> scheduledIds = new HashSet<>();
            for (Activity a : scheduled.getActivities())
                scheduledIds.add(a.getId());

            List<Constraint> binaryCons = allConstraintsForActivity.get(original.getId()).get("binary");

            for (Constraint c : binaryCons)
            {
                if (c instanceof MaximumTemporalActivityDistanceConstraint max)
                {
                    int otherId = (Objects.equals(max.getA1().getId(), original.getId())) ? max.getA2().getId() : max.getA1().getId();

                    if (!scheduledIds.contains(otherId))
                        continue;

                    Activity temp = scheduled.getViaId(otherId);
                    Activity original2 = S.getViaId(otherId);
                    original2.setParts(temp.getParts());

                    if (!max.eval())
                    {
                        ok = false;
                        original2.setParts(new ArrayList<>());
                        break;
                    }
                    else {
                        original2.setParts(new ArrayList<>());
                    }
                }


                else if (c instanceof MinimumTemporalActivityDistanceConstraint min)
                {
                    int otherId = (Objects.equals(min.getA1().getId(), original.getId())) ? min.getA2().getId() : min.getA1().getId();


                    if (!scheduledIds.contains(otherId))
                        continue;

                    Activity temp = scheduled.getViaId(otherId);
                    Activity original2 = S.getViaId(otherId);

                    original2.setParts(temp.getParts());


                    if (!min.eval())
                    {
                        ok = false;
                        original2.setParts(new ArrayList<>());
                        break;
                    }
                    else
                    {
                        original2.setParts(new ArrayList<>());
                    }
                }
                else if (c instanceof OrderingConstraint ord)
                {
                    int otherId = (Objects.equals(ord.getActivityToBeTested1().getId(), original.getId())) ? ord.getActivityToBeTested2().getId() : -1;

                    if (!scheduledIds.contains(otherId))
                        continue;

                    Activity temp = scheduled.getViaId(otherId);
                    Activity original2 = S.getViaId(otherId);

                    original2.setParts(temp.getParts());

                    if (!ord.eval())
                    {
                        ok = false;
                        original2.setParts(new ArrayList<>());
                        break;
                    }
                    else
                    {
                        original2.setParts(new ArrayList<>());
                    }
                }
            }
        }

        original.setParts(new ArrayList<>());
        original.setDuri(0);

        return ok;
    }

    int round = 1;
    public void generateWithReorganisation()
    {
        final int STAGNATION_LIMIT = 10;   // how many runs in a row with no utility change
        int stagnantRuns = 0;
        List<Integer> baseOrder = S.getActivities().stream().sorted(Comparator.comparingDouble(Activity::getDifficulty).reversed()).map(Activity::getId).toList();

        Map<Integer,Integer> promoCount = new HashMap<>();
        Map<Integer,Integer> firstPromoter= new HashMap<>();
        List<List<Integer>> orderHistory = new ArrayList<>();

        while (stagnantRuns < STAGNATION_LIMIT)
        {

            work = deepCopyActivities(S.getActivities());
            scheduled = new Schedule();
            scheduled.setActivities(new ArrayList<>());
            notScheduled = new Schedule();
            notScheduled.setActivities(new ArrayList<>());

            List<Integer> finalBaseOrder = baseOrder;
            work.sort(Comparator.comparingInt(a -> finalBaseOrder.indexOf(a.getId())));
            System.out.println(GREEN + "Round " + round + " started – scheduled order : " + finalBaseOrder);

            Map<Integer,Integer> localPromoCount = new HashMap<>();
            Map<Integer,Integer> localFirstPromoter = new HashMap<>();
            List<Integer> actualOrder = new ArrayList<>();

            while (!work.isEmpty())
            {
                Activity next = work.get(0);
                work.remove(0);

                //Map<Integer, Double> before = estimatedUtilitySnapshot(work);
                Map<Integer,Double> before = difficultySnapshot(work);

                if (!next.isInterruptible)
                    scheduleNonInterruptible(next);
                else
                    scheduleInterruptible(next);

                actualOrder.add(next.getId());

                //Map<Integer, Double> after = estimatedUtilitySnapshot(work);
                Map<Integer,Double> after = difficultySnapshot(work);


                for (Activity a : work)
                {
                    double b = before.get(a.getId());
                    double aNow = after.get(a.getId());

                    if (aNow > b + 1e-9)
                    {
                        localPromoCount.merge(a.getId(), 1, Integer::sum);

                        /* remember the FIRST promoter only */
                        localFirstPromoter.putIfAbsent(a.getId(), next.getId());
                    }
                }
            }


            for (Integer id : localPromoCount.keySet())
            {
                promoCount.merge(id, localPromoCount.get(id), Integer::sum);
                firstPromoter.putIfAbsent(id, localFirstPromoter.get(id));
            }

            baseOrder = applyPromotions(baseOrder, promoCount, firstPromoter);

            if (orderHistory.contains(actualOrder))
                Collections.rotate(baseOrder, 1);

            orderHistory.add(actualOrder);

            double currentUtility = calculateUtility();
            boolean utilityImproved = currentUtility > UtilityOfP + 1e-9;
            if (utilityImproved) {
                UtilityOfP = currentUtility;
                P.setActivities(scheduled.getActivities());
                stagnantRuns = 0;
            } else {
                stagnantRuns++;
            }

            System.out.println("Current run's utility: " + currentUtility);

            System.out.println(GREEN + "Round " + round + " finished – scheduled order : " + actualOrder);
            round ++;
        }
    }



    private Map<Integer,Double> difficultySnapshot(List<Activity> queue)
    {
        Map<Integer,Double> snap = new HashMap<>();
        for (Activity a : queue)
        {
            a.calculateDifficulty();
            snap.put(a.getId(), a.getDifficulty());
        }
        return snap;
    }


    private List<Integer> applyPromotions(List<Integer> current, Map<Integer,Integer> promoCount, Map<Integer,Integer> firstPromoter)
    {
        List<Integer> order = new ArrayList<>(current);

        for (Map.Entry<Integer,Integer> e : promoCount.entrySet())
            if (e.getValue() == 1)
            {
                Integer act = e.getKey();
                Integer promoter = firstPromoter.get(act);

                if (promoter == null)
                    continue;
                int idxAct = order.indexOf(act);
                int idxPromoter = order.indexOf(promoter);

                if (idxAct > idxPromoter)
                {
                    order.remove(idxAct);
                    order.add(idxPromoter, act);
                }
            }

        for (Map.Entry<Integer,Integer> e : promoCount.entrySet())
            if (e.getValue() >= 2)
            {
                Integer act = e.getKey();
                Integer promoter = firstPromoter.get(act);

                if (promoter == null) continue;

                int idxAct = order.indexOf(act);
                int idxPromoter = order.indexOf(promoter);

                if (idxAct > idxPromoter - 1)
                {
                    order.remove(idxAct);
                    order.add(Math.max(0, idxPromoter - 1), act);
                }
            }

        return order;
    }

}