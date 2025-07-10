package org.example.Scheduler.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LocationSWO
{
    public static final LocationSWO ANYWHERE = new LocationSWO(-1, "Anywhere");

    int id;
    private String locationName;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LocationSWO other = (LocationSWO) obj;
        return locationName.equals(other.locationName);
    }

    @Override
    public int hashCode() {
        return locationName.hashCode();
    }
}
