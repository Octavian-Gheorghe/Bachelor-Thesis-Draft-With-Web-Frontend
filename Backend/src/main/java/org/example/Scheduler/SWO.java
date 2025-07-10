package org.example.Scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Constraint.Unary.*;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Constraint.Binary.MaximumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Constraint.Binary.MinimumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Constraint.Binary.OrderingConstraint;
import org.example.Scheduler.Constraint.Constraint;
import org.example.Scheduler.Entity.DistanceMatrix;
import org.example.Scheduler.ForwardCheckingUtils.Candidate;
import org.example.Scheduler.ForwardCheckingUtils.DomainContext;
import org.example.Scheduler.Entity.LocationSWO;
import org.example.Scheduler.Entity.ScheduleSWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;
import org.example.Scheduler.Utility.Binary.*;
import org.example.Scheduler.Utility.Unary.ActivityDurationUtility;
import org.example.Scheduler.Utility.Unary.DomainActivityUtility.LinearActivityUtility;
import org.example.Scheduler.Utility.Unary.DomainActivityUtility.StepwiseActivityUtility;
import org.example.Scheduler.Utility.Unary.PartDistanceUtility.MaximumPartDistanceUtility;
import org.example.Scheduler.Utility.Unary.PartDistanceUtility.MinimumPartDistanceUtility;
import org.example.Scheduler.Utility.Unary.UnaryUtility;

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

    private ScheduleSWO S;
    private DistanceMatrix matrix;
    private List<UnaryUtility> allUnaryUtilitySources;
    private List<BinaryUtility> allBinaryUtilitySources;
    private List<Constraint> allConstraints;

    private ScheduleSWO P;
    private double UtilityOfP;
    private ScheduleSWO scheduled;
    private ScheduleSWO notScheduled;

    private List<ActivitySWO> work;

    private Map<Integer, List<UnaryUtility>> allUnaryUtilitySourcesForActivity;
    private Map<Integer, List<BinaryUtility>> allBinaryUtilitySourcesForActivity;
    private Map<Integer, Map<String, List<Constraint>>> allConstraintsForActivity;
    int round = 1;

    public SWO(ScheduleSWO s, List<Constraint> allConstraints, DistanceMatrix matrix, List<UnaryUtility> allUnaryUtilitySources, List<BinaryUtility> allBinaryUtilitySources)
    {
        this.S = s;
        getS().removeUnusableIntervals();
        this.allConstraints = allConstraints;
        this.allUnaryUtilitySources = allUnaryUtilitySources;
        this.allBinaryUtilitySources = allBinaryUtilitySources;
        this.matrix = matrix;
        List<ActivitySWO> activitiesScheduled = new ArrayList<>();
        this.scheduled = new ScheduleSWO();
        scheduled.setActivities(activitiesScheduled);
        List<ActivitySWO> activitiesP = new ArrayList<>();
        this.P = new ScheduleSWO();
        P.setActivities(activitiesP);
        this.UtilityOfP = -1;
        List<ActivitySWO> activities = new ArrayList<>();
        this.notScheduled = new ScheduleSWO();
        notScheduled.setActivities(activities);
        allConstraintsForActivity = new LinkedHashMap<>();
        allBinaryUtilitySourcesForActivity = new LinkedHashMap<>();
        allUnaryUtilitySourcesForActivity = new LinkedHashMap<>();
        for(ActivitySWO a : s.getActivities())
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setupActivitiesBasicConstraintsBeforeBeginOfAlgorithm()
    {
        int nrOfActivities = getS().getActivities().size();
        for(int i = 0 ; i < nrOfActivities ; i++)
        {
            ActivitySWO act = getS().getActivities().get(i);
            ActivityPartTemporalDomainConstraint d1 = new ActivityPartTemporalDomainConstraint(act);
            MinimumTemporalPartDistanceConstraint d2 = new MinimumTemporalPartDistanceConstraint(act, matrix, act.getDmini());
            MaximumTemporalPartDistanceConstraint d3 = new MaximumTemporalPartDistanceConstraint(act, act.getDmaxi());
            PartDurationRangeConstraint d4 = new PartDurationRangeConstraint(act);
            for(int j = i + 1 ; j < nrOfActivities ; j++)
            {
                ActivitySWO otherAct = getS().getActivities().get(j);
                MinimumTemporalActivityDistanceConstraint d5 = new MinimumTemporalActivityDistanceConstraint(act, otherAct, 0, matrix);
                allConstraints.add(d5);
            }
            allConstraints.add(d1);
            allConstraints.add(d2);
            allConstraints.add(d3);
            allConstraints.add(d4);
        }
    }

    public void setupProvidedConstraintsBeforeBeginOfAlgorithm()
    {
        for(Constraint c : allConstraints)
        {
            if (c instanceof OrderingConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested1();
                allConstraintsForActivity.get(a1.getId()).get("binary").add(c);
            }
            else if (c instanceof MinimumTemporalActivityDistanceConstraint oc)
            {
                ActivitySWO a1 = oc.getA1();
                ActivitySWO a2 = oc.getA2();
                allConstraintsForActivity.get(a1.getId()).get("binary").add(c);
                allConstraintsForActivity.get(a2.getId()).get("binary").add(c);
            }
            else if (c instanceof MaximumTemporalActivityDistanceConstraint oc)
            {
                ActivitySWO a1 = oc.getA1();
                ActivitySWO a2 = oc.getA2();
                allConstraintsForActivity.get(a1.getId()).get("binary").add(c);
                allConstraintsForActivity.get(a2.getId()).get("binary").add(c);
            }
            else if (c instanceof ActivityPartDurationConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof ActivityPartTemporalDomainConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof DurationRangeConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof MaximumTemporalPartDistanceConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof MinimumTemporalPartDistanceConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
            else if (c instanceof PartDurationRangeConstraint oc)
            {
                ActivitySWO a1 = oc.getActivityToBeTested();
                allConstraintsForActivity.get(a1.getId()).get("unary").add(c);
            }
        }
    }

    public void setupActivityBasicUtilitySourcesBeforeBeginOfAlgorithm()
    {
        int nrOfActivities = getS().getActivities().size();
        for(int i = 0 ; i < nrOfActivities ; i++)
        {
            ActivitySWO act = getS().getActivities().get(i);
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
                ActivitySWO a1 = oc.getActivityToBeTested1();
                ActivitySWO a2 = oc.getActivityToBeTested2();
                allBinaryUtilitySources.add(new OrderingPreferenceUtility(a1, a2));
            }
            else if (c instanceof MinimumTemporalActivityDistanceConstraint oc)
            {
                ActivitySWO a1 = oc.getA1();
                ActivitySWO a2 = oc.getA2();
                Integer pdmin = oc.getDmin();
                allBinaryUtilitySources.add(new MinimumActivitiesPartDistanceUtility(a1, a2, pdmin));
            }
            else if (c instanceof MaximumTemporalActivityDistanceConstraint oc)
            {
                ActivitySWO a1 = oc.getA1();
                ActivitySWO a2 = oc.getA2();
                Integer pdmin = oc.getDmax();
                allBinaryUtilitySources.add(new MaximumActivitiesPartDistanceUtility(a1, a2, pdmin));
            }
        }
    }

    public void setupProvidedUtilitySourcesBeforeBeginOfAlgorithm()
    {
        for(UnaryUtility u : allUnaryUtilitySources)
        {
            if(u instanceof ActivityDurationUtility)
            {
                ActivitySWO act =  ((ActivityDurationUtility) u).getA();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof LinearActivityUtility)
            {
                ActivitySWO act = ((LinearActivityUtility) u).getA();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof StepwiseActivityUtility)
            {
                ActivitySWO act = ((StepwiseActivityUtility) u).getA();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof MaximumPartDistanceUtility)
            {
                ActivitySWO act = ((MaximumPartDistanceUtility) u).getActivity();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if (u instanceof MinimumPartDistanceUtility)
            {
                ActivitySWO act = ((MinimumPartDistanceUtility) u).getActivity();
                allUnaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
        }
        for(BinaryUtility u : allBinaryUtilitySources)
        {
            if(u instanceof ImplicationUtility)
            {
                ActivitySWO act = ((ImplicationUtility) u).getA2();
                allBinaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
            else if(u instanceof MaximumActivitiesPartDistanceUtility)
            {
                ActivitySWO act1 = ((MaximumActivitiesPartDistanceUtility) u).getA();
                ActivitySWO act2 = ((MaximumActivitiesPartDistanceUtility) u).getB();
                allBinaryUtilitySourcesForActivity.get(act1.getId()).add(u);
                allBinaryUtilitySourcesForActivity.get(act2.getId()).add(u);
            }
            else if(u instanceof MinimumActivitiesPartDistanceUtility)
            {
                ActivitySWO act1 = ((MinimumActivitiesPartDistanceUtility) u).getA();
                ActivitySWO act2 = ((MinimumActivitiesPartDistanceUtility) u).getB();
                allBinaryUtilitySourcesForActivity.get(act1.getId()).add(u);
                allBinaryUtilitySourcesForActivity.get(act2.getId()).add(u);
            }
            else if(u instanceof OrderingPreferenceUtility)
            {
                ActivitySWO act = ((OrderingPreferenceUtility) u).getA();
                allBinaryUtilitySourcesForActivity.get(act.getId()).add(u);
            }
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double calculateUtility()
    {
        double totalUtil = 0.0;
        for(ActivitySWO a : scheduled.getActivities())
        {
            {
                totalUtil += calculateUtilityForActivity(a);
            }
        }
        return totalUtil;
    }

    public double calculateUtilityWithThisActivityPartSelection(ActivitySWO a)
    {
        a.setDuri(0);
        for(ActivityPartSWO ap : a.getParts())
        {
            a.setDuri(a.getDuri() + ap.getDurij()) ;
        }
        scheduled.addAnotherActivity(a);
        double util = calculateUtility();
        scheduled.getActivities().remove(a);
        return util;
    }

    public double calculateUtilityForActivity(ActivitySWO a)
    {
        System.out.println(a);
        double totalUtil = 0.0;
        List<ActivityPartSWO> actParts = a.getParts();
        int duration = a.getDuri();
        ActivitySWO A = S.getViaId(a.getId());
        A.setDuri(duration);
        List<UnaryUtility> alLUtilitiesForA = allUnaryUtilitySourcesForActivity.get(a.getId());
        for(UnaryUtility u : alLUtilitiesForA)
        {
            totalUtil += u.computeUtility(actParts);
            //System.out.println("This is an " + u.getClass() + " for activity " + a.getName() + " with utlity " +  u.computeUtility(actParts));
        }
        List<BinaryUtility> allUtilitiesForAAndActivity = allBinaryUtilitySourcesForActivity.get(a.getId());
        Set<Integer> scheduledIds = new HashSet<>();
        for (ActivitySWO scheduled : scheduled.getActivities())
        {
            scheduledIds.add(scheduled.getId());
        }
        for(BinaryUtility u : allUtilitiesForAAndActivity)
        {
            if (u instanceof ImplicationUtility u1)
            {
                int otherId = (Objects.equals(u1.getA1().getId(), A.getId())) ? u1.getA2().getId() : u1.getA1().getId();
                if (!scheduledIds.contains(otherId))
                    continue;

                ActivitySWO temp = scheduled.getViaId(otherId);
                ActivitySWO original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u1.computeUtility(actParts, original2.getParts());

                //System.out.println("This is an implication preference between activity with " + a.getName() + " and activity " + original2.getName() + " with result " + u1.computeUtility(actParts, original2.getParts()));

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            }
            else if (u instanceof MinimumActivitiesPartDistanceUtility u2)
            {
                int otherId = (Objects.equals(u2.getA().getId(), A.getId())) ? u2.getB().getId() : u2.getA().getId();
                if (!scheduledIds.contains(otherId))
                    continue;

                ActivitySWO temp = scheduled.getViaId(otherId);
                ActivitySWO original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u2.computeUtility(actParts, original2.getParts());

                //System.out.println("This is a minimum activities duration utility preference between activity with " + a.getName() + " and activity " + original2.getName() + " with result " + u2.computeUtility(actParts, original2.getParts()));

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            }
            else if (u instanceof MaximumActivitiesPartDistanceUtility u3)
            {
                int otherId = (Objects.equals(u3.getA().getId(), A.getId())) ? u3.getB().getId() : u3.getA().getId();
                if (!scheduledIds.contains(otherId))
                    continue;

                ActivitySWO temp = scheduled.getViaId(otherId);
                ActivitySWO original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u3.computeUtility(actParts, original2.getParts());

                //System.out.println("This is a maximum activities duration utility preference between activity with " + a.getName() + " and activity " + original2.getName() + " with result " + u3.computeUtility(actParts, original2.getParts()));

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            }
            else if (u instanceof OrderingPreferenceUtility u4)
            {
                int otherId = (Objects.equals(u4.getA().getId(), A.getId())) ? u4.getB().getId() : u4.getA().getId();

                if (!scheduledIds.contains(otherId))
                    continue;

                ActivitySWO temp = scheduled.getViaId(otherId);
                ActivitySWO original2 = S.getViaId(otherId);
                original2.setParts(temp.getParts());
                original2.setDuri(temp.getDuri());

                totalUtil += u4.computeUtility(actParts, original2.getParts());

                //System.out.println("This is a ordering preference duration utility preference between activity with " + a.getName() + " and activity " + original2.getName() + " with result " + u4.computeUtility(actParts, original2.getParts()));

                original2.setParts(new ArrayList<>());
                original2.setDuri(0);
            }
        }
        A.setDuri(0);
        A.setParts(new ArrayList<>());
        //System.out.println();
        return totalUtil;
    }

    private double difficultySum(List<ActivitySWO> list)
    {
        double sum = 0.0;
        for (ActivitySWO a : list)
        {
            a.calculateDifficulty();
            if (a.getDifficulty() > 1.0)
                return -1.0;
            sum += a.getDifficulty();
        }
        return sum;
    }

    private Map<Integer,Double> difficultySnapshot(List<ActivitySWO> queue)
    {
        Map<Integer,Double> snap = new HashMap<>();
        for (ActivitySWO a : queue)
        {
            a.calculateDifficulty();
            snap.put(a.getId(), a.getDifficulty());
        }
        return snap;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void scheduleInterruptible(ActivitySWO act)
    {
        act.sortTemporalIntervalsByStart();
        DomainContext contextWithNoConstraints = new DomainContext();
        DomainContext contextWithConstraints = null;
        boolean success = true;
        int totalDur = 0;
        int partId = 1;

        while (totalDur < act.getDurimin())
        {
            Candidate candidateForActivityPart = pickBestPart(act, work, matrix, allConstraints, contextWithNoConstraints);
            if (candidateForActivityPart == null)
            {
                success = false;
                break;
            }

            forwardCheck(act, candidateForActivityPart.ti(), candidateForActivityPart.dur(), candidateForActivityPart.loc(), work, matrix, List.of(), contextWithNoConstraints);
            exclude(act, candidateForActivityPart.ti(), candidateForActivityPart.ti() + candidateForActivityPart.dur() + act.getDmini(), contextWithNoConstraints);

            act.sortTemporalIntervalsByStart();

            if (contextWithConstraints == null)
                contextWithConstraints = new DomainContext();

            forwardCheck(act, candidateForActivityPart.ti(), candidateForActivityPart.dur(), candidateForActivityPart.loc(), work, matrix, allConstraints, contextWithConstraints);

            act.getParts().add(new ActivityPartSWO(partId++, candidateForActivityPart.ti(), candidateForActivityPart.dur(), candidateForActivityPart.loc()));

            act.sortActivityPartsByStart();
            totalDur += candidateForActivityPart.dur();

            if (totalDur >= act.getDurimin())
            {
                contextWithConstraints = null;
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
            contextWithNoConstraints.close();
            if (contextWithConstraints != null)
                contextWithConstraints.close();
            act.getParts().clear();
            notScheduled.getActivities().add(act);
        }
        else
        {
            act.setDuri(totalDur);
            act.setPi(act.getParts().size());
            scheduled.getActivities().add(act);
        }
        double score = calculateUtilityWithThisActivityPartSelection(act)/difficultySum(work);

        while (totalDur < act.getDurimax())
        {
            Candidate candidateForActivityPart = pickBestPart(act, work, matrix, allConstraints, contextWithNoConstraints);
            if (candidateForActivityPart == null)
                break;
            try (DomainContext probe = new DomainContext())
            {
                commitPart(act, candidateForActivityPart, -1, probe);
                commitPartWithCons(act, candidateForActivityPart, probe);

                double difficulty = difficultySum(work);
                if (difficulty < 0)
                    break;

                double currentScore = calculateUtilityWithThisActivityPartSelection(act)/difficultySum(work);

                if(currentScore > score)
                    break;
                
                commitPart(act, candidateForActivityPart, partId++, contextWithNoConstraints);
                commitPartWithCons(act, candidateForActivityPart, contextWithNoConstraints);
                score = currentScore;
                totalDur += candidateForActivityPart.dur();
            }
        }
    }

    private Candidate pickBestPart(ActivitySWO act, List<ActivitySWO> queue, DistanceMatrix dm, List<Constraint> cons, DomainContext baseCtx)
    {
        act.sortTemporalIntervalsByStart();
        double bestScore = Double.MIN_VALUE;
        Integer bestDur = null;
        Integer bestTi = null;
        LocationSWO bestLoc = null;

        for (int dur = act.getSmaxi(); dur >= act.getSmini(); dur--)
        {
            boolean valid = act.isActivityDurationFeasable(dur);
            if (!valid)
                continue;

            DomainContext contextForRemovingActivityPartsDomains = new DomainContext();
            for (ActivityPartSWO p : act.getParts())
            {
                exclude(act, p.getTij(), p.calculateEndTime() + act.getDmini(), contextForRemovingActivityPartsDomains);
            }
            for (int ti : act.getAllAvailableStartTimes(dur))
            {
                for (LocationSWO loc : act.getLoci())
                {
                    try (DomainContext trialCtx = new DomainContext())
                    {
                        forwardCheck(act, ti, dur, loc, queue, dm, cons, trialCtx);
                        sortActivitiesTemporalDomainsInPriorityQueue();
                        double difficulty = difficultySum(queue);
                        if(difficulty < 0)
                            break;

                        if(candidateRespectsConstraints(act, ti, dur, loc))
                            continue;

                        double calculatedUtility = calculateUtilityWithThisActivityPartSelection(act);
                        double score = calculatedUtility/difficulty;

                        if (score > bestScore)
                        {
                            bestScore = score;
                            bestDur = dur;
                            bestTi = ti;
                            bestLoc = loc;
                        }
                    }
                }
            }
            contextForRemovingActivityPartsDomains.close();
        }
        return (bestLoc == null) ? null : new Candidate(bestDur, bestTi, bestLoc);
    }

    private void commitPart(ActivitySWO act, Candidate c, int id, DomainContext ctx)
    {
        forwardCheck(act, c.ti(), c.dur(), c.loc(), work, matrix, List.of(), ctx);
        exclude(act, c.ti(), c.ti() + c.dur(), ctx);
        if (id > 0)
            act.getParts().add(new ActivityPartSWO(id, c.ti(), c.dur(), c.loc()));
        act.sortTemporalIntervalsByStart();
    }

    private void commitPartWithCons(ActivitySWO act, Candidate c, DomainContext ctx)
    {
        forwardCheck(act, c.ti(), c.dur(), c.loc(), work, matrix, allConstraints, ctx);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void scheduleNonInterruptible(ActivitySWO act)
    {
        act.sortTemporalIntervalsByStart();
        double bestScore = Double.MIN_VALUE;
        Integer bestDur = null;
        Integer bestTi = null;
        LocationSWO bestLoc = null;

        for (int dur = act.getDurimax(); dur >= act.getDurimin(); dur--)
            for (int ti : act.getAllAvailableStartTimes(dur))
                for (LocationSWO loc : act.getLoci())
                {
                    try (DomainContext contextForTemporalStorageOfActivityConstraints = new DomainContext())
                    {
                        forwardCheck(act, ti, dur, loc, work, matrix, allConstraints, contextForTemporalStorageOfActivityConstraints);
                        sortActivitiesTemporalDomainsInPriorityQueue();
                        double difficulty = difficultySum(work);
                        if(difficulty < 0)
                            break;

                        if(candidateRespectsConstraints(act, ti, dur, loc))
                            continue;

                        double calculatedUtility = calculateUtilityWithThisActivityPartSelection(act);
                        double score = calculatedUtility/difficulty;

                        if (score > bestScore)
                        {
                            bestScore = score;
                            bestDur = dur;
                            bestTi = ti;
                            bestLoc = loc;
                        }
                    }
                }
        if (bestLoc == null)
        {
            notScheduled.getActivities().add(act);
            return;
        }
        DomainContext commit = new DomainContext();
        forwardCheck(act, bestTi, bestDur, bestLoc, work, matrix, allConstraints, commit);

        act.setDuri(bestDur);
        act.getParts().add(new ActivityPartSWO(1, bestTi, bestDur, bestLoc));
        act.setPi(1);
        scheduled.getActivities().add(act);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<Constraint> collectConstraints(ActivitySWO a, ActivitySWO b)
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

    void forwardCheck(ActivitySWO fixed, int ti, int dur, LocationSWO li, List<ActivitySWO> remaining, DistanceMatrix dm, List<Constraint> constraints, DomainContext ctx)
    {
        int fixedEnd = ti + dur;
        for (ActivitySWO tj : remaining)
        {
            LocationSWO closest = dm.closestDistancedLocation(li, tj);
            int travel = dm.Dist(li, closest);

            List<Constraint> pairCons = collectConstraints(fixed, tj);

            if (pairCons.isEmpty())
            {
                exclude(tj, ti, fixedEnd + travel, ctx);
                continue;
            }

            for (Constraint c : pairCons)
            {
                if (c instanceof OrderingConstraint)
                {
                    int domMin = tj.getF().stream().mapToInt(TemporalIntervalSWO::getAi).min().orElseThrow();
                    exclude(tj, domMin, fixedEnd + travel, ctx);
                }

                else if (c instanceof MinimumTemporalActivityDistanceConstraint min)
                {
                    int dmin = min.getDmin();
                    exclude(tj, ti - dmin, fixedEnd + Math.max(travel, dmin), ctx);
                }

                else if (c instanceof MaximumTemporalActivityDistanceConstraint max)
                {
                    int dmax   = max.getDmax();
                    int domMin = tj.getF().stream().mapToInt(TemporalIntervalSWO::getAi).min().orElseThrow();
                    int domMax = tj.getF().stream().mapToInt(TemporalIntervalSWO::getBi).max().orElseThrow();

                    exclude(tj, domMin,ti - dmax,ctx);
                    exclude(tj, ti, fixedEnd + travel, ctx);
                    exclude(tj, fixedEnd + travel + dmax, domMax, ctx);
                }
            }
        }
    }

    private void exclude(ActivitySWO act, int L, int R, DomainContext context)
    {
        List<TemporalIntervalSWO> toAdd = new ArrayList<>();
        List<TemporalIntervalSWO> toRemove = new ArrayList<>();
        Iterator<TemporalIntervalSWO> it = act.getF().iterator();
        while (it.hasNext())
        {
            TemporalIntervalSWO cur = it.next();

            if (R <= cur.getAi() || L >= cur.getBi())
                continue;
            List<TemporalIntervalSWO> kept = subtract(cur, L, R);
            it.remove();
            toRemove.add(cur);
            toAdd.addAll(kept);
        }
        act.getF().addAll(toAdd);

        for (TemporalIntervalSWO r : toRemove)
            context.recordRemove(act, r);
        for (TemporalIntervalSWO k : toAdd)
            context.recordAdd(act, k);
    }

    static List<TemporalIntervalSWO> subtract(TemporalIntervalSWO src, int cutL, int cutR)
    {
        if (cutR <= src.getAi() || cutL >= src.getBi())
            return List.of(src);

        List<TemporalIntervalSWO> out = new ArrayList<>(2);
        if (cutL > src.getAi())
            out.add(new TemporalIntervalSWO(src.getAi(), cutL));
        if (cutR < src.getBi())
            out.add(new TemporalIntervalSWO(cutR, src.getBi()));
        return out;
    }

    private boolean candidateRespectsConstraints(ActivitySWO actCopy, int ti, int dur, LocationSWO loc)
    {
        ActivitySWO original = S.getActivities().stream().filter(a -> Objects.equals(a.getId(), actCopy.getId())).findFirst().orElse(null);
        if (original == null)
            return true;

        ActivityPartSWO probe = new ActivityPartSWO(original.getParts().size() + 1, ti, dur, loc);

        for(ActivityPartSWO a : actCopy.getParts())
        {
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
                    System.out.println(RED + c.getClass() + " failed for " + actCopy.getName() + RESET);
                    ok = false;
                    break;
                }
            }
        }
        if (ok)
        {
            Set<Integer> scheduledIds = new HashSet<>();
            for (ActivitySWO a : scheduled.getActivities())
                scheduledIds.add(a.getId());

            List<Constraint> binaryCons = allConstraintsForActivity.get(original.getId()).get("binary");

            for (Constraint c : binaryCons)
            {
                if (c instanceof MaximumTemporalActivityDistanceConstraint max)
                {
                    int otherId = (Objects.equals(max.getA1().getId(), original.getId())) ? max.getA2().getId() : max.getA1().getId();
                    if (!scheduledIds.contains(otherId))
                        continue;

                    ActivitySWO temp = scheduled.getViaId(otherId);
                    ActivitySWO original2 = S.getViaId(otherId);
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

                    ActivitySWO temp = scheduled.getViaId(otherId);
                    ActivitySWO original2 = S.getViaId(otherId);
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

                    ActivitySWO temp = scheduled.getViaId(otherId);
                    ActivitySWO original2 = S.getViaId(otherId);
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
        return !ok;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static List<ActivitySWO> deepCopyActivities(List<ActivitySWO> src)
    {
        List<ActivitySWO> out = new ArrayList<>(src.size());
        for (ActivitySWO a : src) out.add(copyActivity(a));
        return out;
    }

    private static ActivitySWO copyActivity(ActivitySWO o)
    {
        ActivitySWO x = new ActivitySWO();
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

        List<TemporalIntervalSWO> fCopy = new ArrayList<>();
        for (TemporalIntervalSWO t : o.getF())
            fCopy.add(new TemporalIntervalSWO(t.getAi(), t.getBi()));
        x.setF(fCopy);

        x.setParts(new ArrayList<>());
        x.setLoci(new ArrayList<>(o.getLoci()));
        return x;
    }

    public void generateWithReorganisation()
    {
        final int STAGNATION_LIMIT = 10;
        int stagnantRuns = 0;
        List<Integer> baseOrder = S.getActivities().stream().sorted(Comparator.comparingDouble(ActivitySWO::getDifficulty).reversed()).map(ActivitySWO::getId).toList();

        Map<Integer,Integer> promoCount = new HashMap<>();
        Map<Integer,Integer> firstPromoter= new HashMap<>();
        List<List<Integer>> orderHistory = new ArrayList<>();

        while (stagnantRuns < STAGNATION_LIMIT)
        {
            work = deepCopyActivities(S.getActivities());
            scheduled = new ScheduleSWO();
            scheduled.setActivities(new ArrayList<>());
            notScheduled = new ScheduleSWO();
            notScheduled.setActivities(new ArrayList<>());

            List<Integer> finalBaseOrder = baseOrder;
            work.sort(Comparator.comparingInt(a -> finalBaseOrder.indexOf(a.getId())));
            System.out.println(GREEN + "Round " + round + " started – scheduled order : " + finalBaseOrder);

            Map<Integer,Integer> localPromoCount = new HashMap<>();
            Map<Integer,Integer> localFirstPromoter = new HashMap<>();
            List<Integer> actualOrder = new ArrayList<>();

            while (!work.isEmpty())
            {
                ActivitySWO next = work.get(0);
                work.remove(0);
                Map<Integer,Double> before = difficultySnapshot(work);

                if (!next.getIsInterruptible())
                    scheduleNonInterruptible(next);
                else
                    scheduleInterruptible(next);

                actualOrder.add(next.getId());
                Map<Integer,Double> after = difficultySnapshot(work);

                for (ActivitySWO a : work)
                {
                    double b = before.get(a.getId());
                    double aNow = after.get(a.getId());

                    if (aNow > b + 1e-9)
                    {
                        localPromoCount.merge(a.getId(), 1, Integer::sum);
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

            System.out.println("Computing the utility of the resulting schedule");
            double currentUtility = calculateUtility();
            boolean utilityImproved = currentUtility > UtilityOfP + 1e-9;
            if (utilityImproved)
            {
                UtilityOfP = currentUtility;
                P.setActivities(scheduled.getActivities());
                stagnantRuns = 0;
            }
            else
            {
                stagnantRuns++;
            }

            System.out.println("Current run's utility: " + currentUtility);

            System.out.println(GREEN + "Round " + round + " finished – scheduled order : " + actualOrder + RESET);
//            System.out.println("Scheduled");
//            for(ActivitySWO a : scheduled.getActivities())
//                System.out.println(a);
//
//            System.out.println("Not Scheduled");
//            for(ActivitySWO a : notScheduled.getActivities())
//                System.out.println(a);
            round++;
        }
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
            if (e.getValue() >= 1.1)
            {
                Integer act = e.getKey();
                Integer promoter = firstPromoter.get(act);

                if (promoter == null)
                    continue;

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

    private void sortActivitiesTemporalDomainsInPriorityQueue()
    {
        for(ActivitySWO a : work)
        {
            a.sortTemporalIntervalsByStart();
        }
    }

//    private void LOGCurrentlyFinishedActivities(List<ActivitySWO> work)
//    {
//        System.out.println("Currently, the following activities are done processing: ");
//        for(ActivitySWO a : work)
//        {
//            System.out.println("Activity " + a.getName() +  " : " + a.getId());
//            for(ActivityPartSWO part : a.getParts())
//            {
//                System.out.println(part);
//            }
//        }
//    }
//
//    private void LOGDifficultyOfPropagatedPriorityScheduleActivity(ActivitySWO a)
//    {
//        System.out.println(PURPLE +"For acitvity with id: " + a.getId() + " and name " + a.getName() + " the difficulty is " + a.getDifficulty() + " and wether it is a problem or not, the answwe is " + (a.getDifficulty() > 1));
//    }
//
//    private void LOGCurrentlyPropagatedPriorityScheduleActivities(List<ActivitySWO> as)
//    {
//        for(ActivitySWO a : as)
//        {
//            System.out.println(BLUE + "For activity with id: " + a.getId() + " and name " + a.getName() + " the currently propagated domains are:" );
//            for(TemporalIntervalSWO t : a.getF())
//            {
//                System.out.println(BLUE + t.getAi() + " - " + t.getBi());
//            }
//        }
//    }
//
//    private void LOGCurrentActivitySchedullingOptions(ActivitySWO a, int dur, int t, LocationSWO l)
//    {
//        System.out.println(YELLOW + "Activity with id: " + a.getId() + " and name " + a.getName() + " can start at time " + t + " whilst having duration " + dur + " at location " + l.getLocationName());
//    }
//
//    private void LOGDisplayCurrentActivityParts(ActivitySWO a)
//    {
//        System.out.println(GREEN + "Currently, for the activity " + a.getName() + " with id: " + a.getId() + " we have these parts: ");
//        for(ActivityPartSWO act : a.getParts())
//        {
//            System.out.println(GREEN + act);
//        }
//    }
//
//    private void LOGDisplayCurrentyPropagatedInterruptibleActivityTimeInterval(ActivitySWO a)
//    {
//        System.out.println(RED + "Currently, for the activity " + a.getName() + " with id: " + a.getId() + " we have these temporal Intervals: ");
//        for(TemporalIntervalSWO act : a.getF())
//        {
//            System.out.println(RED + act.getAi() + " - " + act.getBi());
//        }
//    }
}