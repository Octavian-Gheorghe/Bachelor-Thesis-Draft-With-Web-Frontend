package org.example.RSC.Controller;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ScheduleDTO;
import org.example.RSC.DTO.ScheduleRequestDTO;
import org.example.RSC.DTO.ScheduledActivityPartDTO;
import org.example.RSC.Entity.*;
import org.example.RSC.Service.Preference.*;
import org.example.Scheduler.Constraint.Binary.OrderingConstraint;
import org.example.Scheduler.Constraint.Binary.MaximumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Constraint.Binary.MinimumTemporalActivityDistanceConstraint;
import org.example.Scheduler.Constraint.Constraint;
import org.example.Scheduler.Constraint.Unary.MaximumTemporalPartDistanceConstraint;
import org.example.Scheduler.Constraint.Unary.MinimumTemporalPartDistanceConstraint;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;
import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.DistanceMatrix;
import org.example.Scheduler.Entity.LocationSWO;
import org.example.Scheduler.Entity.ScheduleSWO;
import org.example.Scheduler.Entity.TemporalIntervalSWO;
import org.example.Scheduler.SWO;
import org.example.Scheduler.Utility.Binary.BinaryUtility;
import org.example.Scheduler.Utility.Binary.MaximumActivitiesPartDistanceUtility;
import org.example.Scheduler.Utility.Binary.MinimumActivitiesPartDistanceUtility;
import org.example.Scheduler.Utility.Binary.OrderingPreferenceUtility;
import org.example.Scheduler.Utility.Unary.ActivityDurationUtility;
import org.example.Scheduler.Utility.Unary.DomainActivityUtility.LinearActivityUtility;
import org.example.Scheduler.Utility.Unary.DomainActivityUtility.StepwiseActivityUtility;
import org.example.Scheduler.Utility.Unary.PartDistanceUtility.MaximumPartDistanceUtility;
import org.example.Scheduler.Utility.Unary.PartDistanceUtility.MinimumPartDistanceUtility;
import org.example.Scheduler.Utility.Unary.UnaryUtility;
import org.example.RSC.Service.ActivityIdeaService;
import org.example.RSC.Service.Constraint.ActivitiesDistanceConstraintService;
import org.example.RSC.Service.Constraint.ActivityPartDistanceConstraintService;
import org.example.RSC.Service.Constraint.OrderingConstraintService;
import org.example.RSC.Service.ScheduleService;
import org.example.RSC.Service.ScheduledActivityPartService;
import org.example.RSC.Service.UserEntityService;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.helpers.LocatorImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
public class SchedulerController
{
    private final ScheduleService scheduleService;
    private final ActivityIdeaService activityIdeaService;
    private final ActivitiesDistanceConstraintService activitiesDistanceConstraintService;
    private final ActivityPartDistanceConstraintService activityPartDistanceConstraintService;
    private final OrderingConstraintService orderingConstraintService;
    private final ActivitiesDistancePreferenceService activitiesDistancePreferenceService;
    private final ActivityDurationPreferenceService activityDurationPreferenceService;
    private final ActivityScheduleTimePreferenceService activityScheduleTimePreferenceService;
    private final ActivityPartDistancePreferenceService activityPartDistancePreferenceService;
    private final ImplicationPreferenceService implicationPreferenceService;
    private final OrderingPreferenceService orderingPreferenceService;
    private final ScheduledActivityPartService scheduledActivityPartService;
    private final UserEntityService userEntityService;

    @PostMapping("/{username}")
    public List<ScheduledActivityPartDTO> getAll(@PathVariable String username, @RequestBody ScheduleRequestDTO data)
    {
        UserEntity user = userEntityService.getByUsername(username);
        int idOfUser = user.getId();
        List<ActivityIdea> allUserActivities = activityIdeaService.findAllForUsername(username);

        List<ActivityIdea> selectedActivities = allUserActivities.stream().filter(idea -> data.getActivityIdeaIds().contains(idea.getId())).toList();
        LocalDateTime lowest = findLowestActivityStartInterval(selectedActivities);
        System.out.println("This schedule starts at time: " + lowest);
        LocalDateTime highest = findHighestActivityStartInterval(selectedActivities);
        System.out.println("This schedule ends at time: " + highest);
        List<Location> allLocations = getAllUniqueLocations(selectedActivities);
        DistanceMatrix distanceMatrix = setupDistanceMatrix(allLocations);
        List<ActivitySWO> activitiesSWO = convertIdeasToAlgorithmObjects(selectedActivities, lowest);
        SWO swo = setupSWO(activitiesSWO, selectedActivities, distanceMatrix);
        //swo.generateWithReorganisation();
        ScheduleDTO scheduleDTO = new ScheduleDTO(data.getName(), lowest, highest, idOfUser);
        Schedule schedule = scheduleService.create(scheduleDTO);
        List<ScheduledActivityPartDTO> result = uniformizeResultFromSWO(swo.getP().getActivities(), lowest, schedule.getId());
        for (ScheduledActivityPartDTO dto : result)
        {
            scheduledActivityPartService.create(dto);
        }
        return result;
    }

    public SWO setupSWO(List<ActivitySWO> activitiesSWO, List<ActivityIdea> activities, DistanceMatrix dm)
    {
        ScheduleSWO S = new ScheduleSWO();
        S.setActivities(activitiesSWO);
        List<Constraint> allConstraints = generateConstraints(activities, activitiesSWO, dm);
        List<UnaryUtility> allUnaryUtilities = getAllUnaryUtilities(activities, activitiesSWO);
        List<BinaryUtility> allBinaryUtilities = getAllBinaryUtilities(activities, activitiesSWO);
        for(Constraint c : allConstraints)
        {
            System.out.println(c.getClass());
        }
        for(UnaryUtility c : allUnaryUtilities)
        {
            System.out.println(c.getClass());
        }
        for(BinaryUtility c : allBinaryUtilities)
        {
            System.out.println(c.getClass());
        }
        SWO swo = new SWO(S, allConstraints, dm, allUnaryUtilities, allBinaryUtilities);
        swo.setupActivitiesBasicConstraintsBeforeBeginOfAlgorithm();
        swo.setupProvidedConstraintsBeforeBeginOfAlgorithm();
        swo.setupActivityBasicUtilitySourcesBeforeBeginOfAlgorithm();
        swo.setupProvidedUtilitySourcesBeforeBeginOfAlgorithm();
        swo.generateWithReorganisation();
        for (ActivitySWO act : swo.getP().getActivities()) {
            System.out.println(act.getName() + " : " + act.getId());
            for (ActivityPartSWO actp : act.getParts()) {
                System.out.println(actp);
            }
        }
        System.out.println("Total generated utility: " + swo.getUtilityOfP());
        return swo;
    }

    public List<ActivitySWO> convertIdeasToAlgorithmObjects(List<ActivityIdea> activities, LocalDateTime lowest)
    {
        List<ActivitySWO> activitiesSWO = new ArrayList<>();
        for(ActivityIdea A : activities)
        {
            A.getTemporalIntervals().sort(Comparator.comparing(TemporalInterval::getStartInterval));
            List<LocationSWO> locations = convertLocationsAlgorithmData(A.getLocations());
            List<TemporalIntervalSWO> temporalIntervalSWO = convertTemporalIntervalToAlgorithmData(A.getTemporalIntervals(), lowest);
            if(A.getSmin() == null)
            {
                ActivitySWO act = new ActivitySWO(A.getId(), A.getName(), A.getDurimax(), A.getDurimin(), temporalIntervalSWO, locations);
                activitiesSWO.add(act);
                LOGdisplayActivitySWO(act);
            }
            else
            {
                ActivitySWO act = new ActivitySWO(A.getId(), A.getName(), A.getDurimax(), A.getDurimin(), A.getSmax(), A.getSmin(), A.getDmax(), A.getDmin(), temporalIntervalSWO, locations);
                activitiesSWO.add(act);
                LOGdisplayActivitySWO(act);
            }
        }
        return activitiesSWO;
    }

    private List<ScheduledActivityPartDTO> uniformizeResultFromSWO(List<ActivitySWO> activities, LocalDateTime earliest, int id)
    {
        List<ScheduledActivityPartDTO> result = new ArrayList<>();
        for(ActivitySWO a : activities)
        {
            for(ActivityPartSWO ap : a.getParts())
            {
                ScheduledActivityPartDTO s = new ScheduledActivityPartDTO(id, a.getName(), earliest.plusMinutes(ap.getTij()), ap.getDurij(), ap.getLij().getLocationName());
                result.add(s);
            }
        }
        result.sort(Comparator.comparing(ScheduledActivityPartDTO::getStartTime));
        return result;
    }

    private List<BinaryUtility> getAllBinaryUtilities(List<ActivityIdea> activities, List<ActivitySWO> activitiesSWO) {
        Map<Integer, ActivitySWO> activityMap = activitiesSWO.stream().collect(Collectors.toMap(ActivitySWO::getId, a -> a));
        List<BinaryUtility> utilities = new ArrayList<>();

        activitiesDistancePreferenceService.getAll().forEach(p -> {
            ActivitySWO a = activityMap.get(p.getActivityIdea1().getId());
            ActivitySWO b = activityMap.get(p.getActivityIdea2().getId());
            if (a != null && b != null)
            {
                switch (p.getType())
                {
                    case MINIMUM -> utilities.add(new MinimumActivitiesPartDistanceUtility(a, b, p.getDistance()));
                    case MAXIMUM -> utilities.add(new MaximumActivitiesPartDistanceUtility(a, b, p.getDistance()));
                }
            }
        });

        orderingPreferenceService.getAll().forEach(p -> {
            ActivitySWO a = activityMap.get(p.getActivityIdeaBefore().getId());
            ActivitySWO b = activityMap.get(p.getActivityIdeaAfter().getId());
            if (a != null && b != null)
            {
                utilities.add(new OrderingPreferenceUtility(a, b));
            }
        });

        implicationPreferenceService.getAll().forEach(p -> {
            ActivitySWO a = activityMap.get(p.getActivityIdeaInitial().getId());
            ActivitySWO b = activityMap.get(p.getActivityIdeaImplied().getId());
            if (a != null && b != null)
            {
                utilities.add(new OrderingPreferenceUtility(a, b));
            }
        });

        return utilities;
    }

    private List<Constraint> generateConstraints(List<ActivityIdea> activities, List<ActivitySWO> activitiesSWO, DistanceMatrix dm) {
        List<Constraint> constraints = new ArrayList<>();

        Map<Integer, ActivitySWO> activityMap = activitiesSWO.stream().collect(Collectors.toMap(ActivitySWO::getId, a -> a));

        activityPartDistanceConstraintService.getAll().forEach(c -> {
            ActivitySWO act = activityMap.get(c.getActivityIdea().getId());
            if (act != null) {
                switch (c.getType()) {
                    case MINIMUM -> constraints.add(new MinimumTemporalPartDistanceConstraint(act, dm, c.getDistance()));
                    case MAXIMUM -> constraints.add(new MaximumTemporalPartDistanceConstraint(act, c.getDistance()));
                }
            }
        });

        activitiesDistanceConstraintService.getAll().forEach(c -> {
            ActivitySWO a1 = activityMap.get(c.getActivityIdea1().getId());
            ActivitySWO a2 = activityMap.get(c.getActivityIdea2().getId());
            if (a1 != null && a2 != null) {
                switch (c.getType()) {
                    case MINIMUM -> constraints.add(new MinimumTemporalActivityDistanceConstraint(a1, a2, c.getDistance(), dm));
                    case MAXIMUM -> constraints.add(new MaximumTemporalActivityDistanceConstraint(a1, a2, c.getDistance()));
                }
            }
        });

        orderingConstraintService.getAll().forEach(c -> {
            ActivitySWO a1 = activityMap.get(c.getActivityIdeaBefore().getId());
            ActivitySWO a2 = activityMap.get(c.getActivityIdeaAfter().getId());
            if (a1 != null && a2 != null) {
                constraints.add(new OrderingConstraint(a1, a2));
            }
        });

        return constraints;
    }

    private List<UnaryUtility> getAllUnaryUtilities(List<ActivityIdea> activities, List<ActivitySWO> activitiesSWO) {
        Map<Integer, ActivitySWO> activityMap = activitiesSWO.stream().collect(Collectors.toMap(ActivitySWO::getId, a -> a));
        List<UnaryUtility> utilities = new ArrayList<>();

        activityDurationPreferenceService.getAll().forEach(p -> {
            ActivitySWO a = activityMap.get(p.getActivityIdea().getId());
            if (a != null)
                utilities.add(new ActivityDurationUtility(a, p.getMinimumDuration(), p.getMaximumDuration(), 0.2, 0.8));
        });

        activityPartDistancePreferenceService.getAll().forEach(p -> {
            ActivitySWO a = activityMap.get(p.getActivityIdea().getId());
            if (a != null) {
                switch (p.getType()) {
                    case MAXIMUM -> utilities.add(new MaximumPartDistanceUtility(a, p.getDistance()));
                    case MINIMUM -> utilities.add(new MinimumPartDistanceUtility(a, p.getDistance()));
                }
            }
        });

        activityScheduleTimePreferenceService.getAll().forEach(p -> {
            ActivitySWO a = activityMap.get(p.getActivityIdea().getId());
            if (a != null) {
                switch (p.getType()) {
                    case EARLIER -> utilities.add(new LinearActivityUtility(a, (int) ChronoUnit.MINUTES.between(findLowestActivityStartInterval(activities), p.getTimeOfAnalysis())));
                    case LATER -> utilities.add(new StepwiseActivityUtility(a, (int) ChronoUnit.MINUTES.between(findLowestActivityStartInterval(activities), p.getTimeOfAnalysis())));
                }
            }
        });
        return utilities;
    }

    private List<TemporalIntervalSWO> convertTemporalIntervalToAlgorithmData(List<TemporalInterval> temporalIntervals, LocalDateTime date)
    {
        List<TemporalIntervalSWO> result = new ArrayList<>();
        for(TemporalInterval t : temporalIntervals)
        {
            long minutes = ChronoUnit.MINUTES.between(date, t.getStartInterval());
            int start = (int) minutes;
            minutes = ChronoUnit.MINUTES.between(date, t.getEndInterval());
            int end = (int) minutes;
            TemporalIntervalSWO temp = new TemporalIntervalSWO(start, end);
            result.add(temp);
        }
        return result;
    }

    private List<LocationSWO> convertLocationsAlgorithmData(List<Location> Location)
    {
        List<LocationSWO> result = new ArrayList<>();
        for(Location l : Location)
        {
            LocationSWO locationSWO = new LocationSWO(l.getId(), l.getName());
            result.add(locationSWO);
        }
        return result;
    }

    private LocalDateTime findLowestActivityStartInterval(List<ActivityIdea> act)
    {
        return act.stream()
                .flatMap(a -> a.getTemporalIntervals().stream())
                .map(TemporalInterval::getStartInterval)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private List<Location> getAllUniqueLocations(List<ActivityIdea> activityIdeas)
    {
        return activityIdeas.stream()
                .flatMap(idea -> idea.getLocations().stream())
                .distinct()
                .toList();
    }

    private LocalDateTime findHighestActivityStartInterval(List<ActivityIdea> act)
    {
        return act.stream()
                .flatMap(a -> a.getTemporalIntervals().stream())
                .map(TemporalInterval::getEndInterval)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public DistanceMatrix setupDistanceMatrix(List<Location> locations)
    {
        System.out.println(locations.size());
        DistanceMatrix dm = new DistanceMatrix();
        int locationsListSize = locations.size();
        for(int i = 0; i < locationsListSize;i++)
        {
            for(int j = 0; j < locationsListSize; j++)
            {
                LocationSWO l1 = new LocationSWO(i, locations.get(i).getName());
                LocationSWO l2 = new LocationSWO(j, locations.get(j).getName());
                dm.addLocationsWithDistance(l1, l2, computeDistance(locations.get(i), locations.get(j)));
            }
        }
        return dm;
    }

    private int computeDistance(Location a, Location b)
    {
        System.out.println(Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY()) * (a.getY()-b.getY())));
        return (int) Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY()) * (a.getY()-b.getY()));
    }

    public void LOGdisplayActivitySWO(ActivitySWO act)
    {
        System.out.println(act);
        for(LocationSWO l : act.getLoci())
        {
            System.out.print(l + ", ");
        }
        System.out.println();
        for(TemporalIntervalSWO t : act.getF())
        {
            System.out.print(t + ", ");
        }

    }
}
