package org.example.SWO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Entity.Activity.Activity;
import org.example.Entity.Activity.ActivityPart;
import org.example.Entity.Activity.PlacedBlock;
import org.example.Entity.Constraint.Constraint;
import org.example.Entity.Location;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class Constructor
{
    private List<Activity> activities;
    private Map<Location, Map<Location, Integer>> distanceMatrix;
    private List<Constraint> allConstraints;
    private List<PlacedBlock> occupiedTimeSlots;

    public Constructor(List<Activity> activities, Map<Location, Map<Location, Integer>> distanceMatrix, List<Constraint> allConstraints)
    {
        this.activities = activities;
        this.distanceMatrix = distanceMatrix;
        this.allConstraints = allConstraints;
        occupiedTimeSlots = new ArrayList<>();
    }

    public List<Activity> construct() {
        occupiedTimeSlots.clear();

        for (Activity activity : activities) {
            List<LocalTime> validStartTimes = activity.findPossibleStartTimes();
            boolean foundValidCombination = false;
            double minProductOfRemainingDifficulties = 1.0;
            LocalTime potentialStartLocalTime = LocalTime.of(0, 0);
            Location potentialLocation = null;

            for (LocalTime start : validStartTimes) {
                for (Location location : activity.getLocations()) {

                    if (!isTimeSlotAvailable(start, activity.getDuration(), location, occupiedTimeSlots)) {
                        continue;
                    }

                    double productOfRemainingDifficulties = computeRemainingDifficultyProduct(activity);

                    if (productOfRemainingDifficulties < 1.0)
                    {
                        foundValidCombination = true;
                        if (productOfRemainingDifficulties < minProductOfRemainingDifficulties)
                        {
                            minProductOfRemainingDifficulties = productOfRemainingDifficulties;
                            potentialStartLocalTime = start;
                            potentialLocation = location;
                        }
                    }
                }
            }

            if (!foundValidCombination) {
                activity.setTroublemaker(true);
                return activities;
            } else {
                // Commit the selected assignment
                ActivityPart activityPart = activity.getParts().get(0);
                activityPart.setDuration(activity.getDuration());
                activityPart.setStartTime(potentialStartLocalTime);
                activityPart.setChosenLocation(potentialLocation);

                // Register this as an occupied block
                occupiedTimeSlots.add(new PlacedBlock(
                        potentialLocation,
                        potentialStartLocalTime,
                        potentialStartLocalTime.plusMinutes(activity.getDuration())
                ));
            }
        }

        return activities;
    }

    private List<Constraint> getRelatedConstraints(Activity activity) {
        List<Constraint> related = new ArrayList<>();
        for (Constraint c : allConstraints) {
            if (c.involves(activity))
            {
                related.add(c);
            }
        }
        return related;
    }


    private double computeRemainingDifficultyProduct(Activity currentActivity) {
        double product = 1.0;
        for (Activity a : activities)
        {
            if (!a.equals(currentActivity))
            {
                product *= a.calculateInitialDifficulty(); //TO BE MODIFIED
            }
        }
//        System.out.println("Total Overall Difficulty: " + product);
        return product;
    }

    private boolean isTimeSlotAvailable(LocalTime start, int duration, Location location, List<PlacedBlock> placedBlocks) {
        LocalTime end = start.plusMinutes(duration);
        for (PlacedBlock block : placedBlocks) {
            if (block.getLocation().equals(location)) {
                boolean overlaps = start.isBefore(block.getEnd()) && end.isAfter(block.getStart());

                if (overlaps) {
                    return false;
                }
            }
        }
        return true;
    }
}
