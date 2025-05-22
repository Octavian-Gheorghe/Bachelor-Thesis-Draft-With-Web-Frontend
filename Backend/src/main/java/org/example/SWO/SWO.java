package org.example.SWO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Entity.Activity.Activity;
import org.example.Entity.Constraint.Constraint;
import org.example.Entity.Location;
import org.example.Entity.Schedule;

import javax.imageio.stream.FileCacheImageOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SWO
{
    private Schedule schedule;
    private List<Constraint> allConstraints;
    private List<Location> allExistingLocations;

    public Map<Location, Map<Location, Integer>> createMatrixOfLocation() {
        Map<Location, Map<Location, Integer>> distanceMatrix = new HashMap<>();

        for(Location from : allExistingLocations)
        {
            Map<Location, Integer> row = new HashMap<>();
            for (Location to : allExistingLocations)
            {
                String fromString = from.getLocationName();
                String toString = to.getLocationName();
                int distance = fromString.equals(toString) ? 0 : 0; // Replace with your logic
                row.put(to, distance);
            }
            distanceMatrix.put(from, row);
        }
        return distanceMatrix;
    }

    public void begin() {
        Map<Location, Map<Location, Integer>> matrixOfLocations = createMatrixOfLocation();
        schedule.removeUnusableIntervals();
        schedule.setActivitiesAsNonTroublemakers();


        List<Activity> activities = schedule.getActivities();
        Analyzer analyzer = new Analyzer(activities);
        Prioritizer prioritizer = new Prioritizer(activities);

        boolean finished = false;
        int maxIterations = 100;
        int iteration = 0;

        schedule.sortActivitiesByDifficulty();

        Constructor constructor = new Constructor(schedule.getActivities(), matrixOfLocations, allConstraints);

        while (!finished && iteration < maxIterations) {
            iteration++;
            System.out.println("---- SWO Iteration " + iteration + " ----");

            List<Activity> constructedActivities = constructor.construct();
            analyzer.setActivities(constructedActivities);
            List<Activity> troublemakers = analyzer.identifyTroublemakers();

            if (troublemakers.isEmpty())
            {
                finished = true;
                System.out.println("Valid schedule found.");
                for(Activity a : activities)
                {
                    System.out.println(a);
                }
            }
            else
            {
                System.out.println("Troublemakers detected: " + troublemakers.size());
                prioritizer.setActivities(constructedActivities);
                List<Activity> prioritizedActivities = prioritizer.promoteTroublemakers(troublemakers);
                constructor.setActivities(prioritizedActivities);
            }
        }

        if (!finished)
        {
            System.out.println("SWO stopped after max iterations. Final result may not be complete.");
        }
    }
}
