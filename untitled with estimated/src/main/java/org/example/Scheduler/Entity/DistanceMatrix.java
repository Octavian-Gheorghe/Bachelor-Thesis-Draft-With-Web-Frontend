package org.example.Scheduler.Entity;

import org.example.Scheduler.Entity.Activity.Activity;
import org.example.Scheduler.Entity.Activity.ActivityPart;

import java.util.*;

/**
 * A symmetric distance matrix for {@link Location}s.
 * Dist(Anywhere, L) = Dist(L, Anywhere) = 0 is enforced at all times.
 */
public class DistanceMatrix
{
    private final Map<Location, Map<Location, Integer>> distances = new HashMap<>();

    public DistanceMatrix()
    {
        addLocation(Location.ANYWHERE);
    }

    public static DistanceMatrix of(Collection<Location> initial) {
        DistanceMatrix m = new DistanceMatrix();
        initial.forEach(m::addLocation);
        return m;
    }

    public void addLocation(Location loc)
    {
        if (loc == null)
            return;
        distances.computeIfAbsent(loc, __ -> new HashMap<>()).put(loc, 0);

        distances.get(loc).put(Location.ANYWHERE, 0);
        distances.computeIfAbsent(Location.ANYWHERE, __ -> new HashMap<>()).put(loc, 0);
    }

    public void addLocationsFromActivity(Activity a)
    {
        if (a == null || a.getLoci() == null)
            return;
        a.getLoci().forEach(this::addLocation);
    }

    public void addLocationsWithDistance(Location a, Location b, int distance)
    {
        if (distance < 0)
            throw new IllegalArgumentException("Distance cannot be negative");
        addLocation(a);
        addLocation(b);
        distances.get(a).put(b, distance);
        distances.get(b).put(a, distance);
    }

    public int Dist(Location a, Location b)
    {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        Map<Location, Integer> row = distances.get(a);
        if (row == null || !row.containsKey(b))
        {
            throw new IllegalStateException("No distance defined between " + a + " and " + b);
        }
        return row.get(b);
    }

    public Location closestDistancedLocation(Location a, Activity A)
    {
        Location closest = new Location();
        Objects.requireNonNull(a);
        Objects.requireNonNull(A.getLoci());
        Map<Location, Integer> row = distances.get(a);
        if (row == null)
        {
            throw new IllegalStateException("Inexistent Location " + a);
        }
        else
        {
            int minimumDistance = 999999999;
            for(Location b : A.getLoci())
            {
                if(row.containsKey(b))
                {
                    if(Dist(a, b) < minimumDistance)
                    {
                        closest = b;
                        minimumDistance = Dist(a, b);
                    }
                }
            }
        }
        return closest;
    }

    public Location closestDistancedLocationToActivityPart(Location a, ActivityPart A)
    {
        Location closest = new Location();
        Objects.requireNonNull(a);
        Objects.requireNonNull(A.getLij());
        Map<Location, Integer> row = distances.get(a);
        if (row == null)
        {
            throw new IllegalStateException("Inexistent Location " + a);
        }
        else
        {
            int minimumDistance = 999999999;
            Location b = A.getLij();
                if(row.containsKey(b))
                {
                    if(Dist(a, b) < minimumDistance)
                    {
                        closest = b;
                        return closest;
                    }
                }
            }
        return closest;
    }

    public Set<Location> allLocations()
    {
        return Collections.unmodifiableSet(distances.keySet());
    }
}