package org.example;

import org.example.Entity.Activity.Activity;
import org.example.Entity.Constraint.Constraint;
import org.example.Entity.Location;
import org.example.Entity.Schedule;
import org.example.Entity.TemporalInterval;
import org.example.SWO.SWO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main
{
    public static void main(String[] args) {
//        List<Location> allLocations = new ArrayList<>();
//        Location sharedLocation = new Location("Library");
//        allLocations.add(sharedLocation);
//
//        List<Activity> activities = new ArrayList<>();
//
//        List<TemporalInterval> intervals1 = new ArrayList<>();
//        intervals1.add(new TemporalInterval(LocalTime.parse("09:00"), LocalTime.parse("12:00")));
//        intervals1.add(new TemporalInterval(LocalTime.parse("13:00"), LocalTime.parse("17:00")));
//        List<Location> locs1 = new ArrayList<>();
//        locs1.add(sharedLocation);
//        Activity a1 = new Activity(1, "Write Report", 90, 90, 90, 0, 0, intervals1, locs1);
//        activities.add(a1);
//
//        List<TemporalInterval> intervals2 = new ArrayList<>();
//        intervals2.add(new TemporalInterval(LocalTime.parse("09:00"), LocalTime.parse("12:00")));
//        intervals2.add(new TemporalInterval(LocalTime.parse("13:00"), LocalTime.parse("17:00")));
//        List<Location> locs2 = new ArrayList<>();
//        locs2.add(sharedLocation);
//        Activity a2 = new Activity(2, "Read Book", 120, 120, 120, 0, 0, intervals2, locs2);
//        activities.add(a2);
//
//        List<TemporalInterval> intervals3 = new ArrayList<>();
//        intervals3.add(new TemporalInterval(LocalTime.parse("09:00"), LocalTime.parse("12:00")));
//        intervals3.add(new TemporalInterval(LocalTime.parse("13:00"), LocalTime.parse("17:00")));
//        List<Location> locs3 = new ArrayList<>();
//        locs3.add(sharedLocation);
//        Activity a3 = new Activity(3, "Group Meeting", 60, 60, 60, 0, 0, intervals3, locs3);
//        activities.add(a3);
//
//        Schedule schedule = new Schedule(activities);
//
//        List<Constraint> constraints = new ArrayList<>();
//
//        SWO swo = new SWO(schedule, constraints, allLocations);
//        swo.begin();
//
//        for (Activity a : schedule.getActivities()) {
//            System.out.println(a);
//
//        }
        SpringApplication.run(Main.class, args);
    }
}

