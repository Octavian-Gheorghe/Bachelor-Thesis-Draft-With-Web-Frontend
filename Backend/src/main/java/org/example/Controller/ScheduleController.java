package org.example.Controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.DTO.ActivityDTO;
import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.Constraint.Constraint;
import org.example.Entity.Location;
import org.example.Entity.Schedule;
import org.example.Entity.TemporalInterval;
import org.example.SWO.SWO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @PostMapping
    public List<ScheduledActivityResponse> runScheduling(@RequestBody List<ActivityDTO> activityDtos) {
        List<Activity> activities = new ArrayList<>();
        Set<Location> allLocations = new HashSet<>();

        for (ActivityDTO dto : activityDtos) {

            System.out.println("------ ACTIVITY DTO RECEIVED ------");
            System.out.println("ID: " + dto.getId());
            System.out.println("Name: " + dto.getName());
            System.out.println("Duration: " + dto.getDuration());
            System.out.println("MinPartDuration: " + dto.getMinPartDuration());
            System.out.println("MaxPartDuration: " + dto.getMaxPartDuration());
            System.out.println("MinGapBetweenParts: " + dto.getMinGapBetweenParts());
            System.out.println("MaxGapBetweenParts: " + dto.getMaxGapBetweenParts());

            System.out.println("Intervals:");
            if (dto.getIntervals() != null) {
                for (var interval : dto.getIntervals()) {
                    System.out.println("  Start: " + interval.getStartTime() + " - End: " + interval.getEndTime());
                }
            } else {
                System.out.println("  NULL");
            }

            System.out.println("Locations:");
            if (dto.getLocations() != null) {
                for (var loc : dto.getLocations()) {
                    System.out.println("  " + loc.getLocationName());
                }
            } else {
                System.out.println("  NULL");
            }
            System.out.println("-----------------------------------");


            Activity activity = new Activity(
                    dto.getId(),
                    dto.getName(),
                    dto.getDuration(),
                    dto.getMinPartDuration(),
                    dto.getMaxPartDuration(),
                    dto.getMinGapBetweenParts(),
                    dto.getMaxGapBetweenParts(),
                    dto.getIntervals(),
                    dto.getLocations()
            );
            activities.add(activity);
            allLocations.addAll(dto.getLocations());
        }

        List<Constraint> constraints = new ArrayList<>();
        List<Location> locations = new ArrayList<>(allLocations);
        Schedule schedule = new Schedule(activities);
        SWO swo = new SWO(schedule, constraints, locations);
        swo.begin();

        List<ScheduledActivityResponse> result = new ArrayList<>();
        for (Activity a : schedule.getActivities()) {
            ActivityPart part = a.getParts().get(0);
            System.out.println(part);
            result.add(new ScheduledActivityResponse(
                    a.getName(),
                    part.getStartTime().toString(),
                    part.calculateEndTime().toString(),
                    part.getDuration(),
                    part.getChosenLocation().getLocationName()
            ));
        }

        return result;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    static class ScheduledActivityResponse {
        private String activityName;
        private String startTime;
        private String endTime;
        private int duration;
        private String location;
    }
}
