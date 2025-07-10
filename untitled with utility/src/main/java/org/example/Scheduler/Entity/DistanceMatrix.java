package org.example.Scheduler.Entity;

import org.example.Scheduler.Entity.Activity.ActivitySWO;
import org.example.Scheduler.Entity.Activity.ActivityPartSWO;

import java.util.*;

/**
 * A symmetric distance matrix for {@link LocationSWO}s.
 * Dist(Anywhere, L) = Dist(L, Anywhere) = 0 is enforced at all times.
 */
public class DistanceMatrix
{
    private final Map<LocationSWO, Map<LocationSWO, Integer>> distances = new HashMap<>();

    public DistanceMatrix()
    {
        addLocation(LocationSWO.ANYWHERE);
    }

    public static DistanceMatrix of(Collection<LocationSWO> initial) {
        DistanceMatrix m = new DistanceMatrix();
        initial.forEach(m::addLocation);
        return m;
    }

    public void addLocation(LocationSWO loc)
    {
        if (loc == null)
            return;
        distances.computeIfAbsent(loc, __ -> new HashMap<>()).put(loc, 0);

        distances.get(loc).put(LocationSWO.ANYWHERE, 0);
        distances.computeIfAbsent(LocationSWO.ANYWHERE, __ -> new HashMap<>()).put(loc, 0);
    }

    public void addLocationsFromActivity(ActivitySWO a)
    {
        if (a == null || a.getLoci() == null)
            return;
        a.getLoci().forEach(this::addLocation);
    }

    public void addLocationsWithDistance(LocationSWO a, LocationSWO b, int distance)
    {
        if (distance < 0)
            throw new IllegalArgumentException("Distance cannot be negative");
        addLocation(a);
        addLocation(b);
        distances.get(a).put(b, distance);
        distances.get(b).put(a, distance);
    }

    public int Dist(LocationSWO a, LocationSWO b)
    {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        Map<LocationSWO, Integer> row = distances.get(a);
        if (row == null || !row.containsKey(b))
        {
            throw new IllegalStateException("No distance defined between " + a + " and " + b);
        }
        return row.get(b);
    }

    public LocationSWO closestDistancedLocation(LocationSWO a, ActivitySWO A)
    {
        LocationSWO closest = new LocationSWO();
        Objects.requireNonNull(a);
        Objects.requireNonNull(A.getLoci());
        Map<LocationSWO, Integer> row = distances.get(a);
        if (row == null)
        {
            throw new IllegalStateException("Inexistent Location " + a);
        }
        else
        {
            int minimumDistance = 999999999;
            for(LocationSWO b : A.getLoci())
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

    public LocationSWO closestDistancedLocationToActivityPart(LocationSWO a, ActivityPartSWO A)
    {
        LocationSWO closest = new LocationSWO();
        Objects.requireNonNull(a);
        Objects.requireNonNull(A.getLij());
        Map<LocationSWO, Integer> row = distances.get(a);
        if (row == null)
        {
            throw new IllegalStateException("Inexistent Location " + a);
        }
        else
        {
            int minimumDistance = 999999999;
            LocationSWO b = A.getLij();
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

    public Set<LocationSWO> allLocations()
    {
        return Collections.unmodifiableSet(distances.keySet());
    }
}