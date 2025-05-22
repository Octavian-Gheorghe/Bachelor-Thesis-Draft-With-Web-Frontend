package org.example.Entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Entity.Location;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityPart
{
    private Integer id;
    private LocalTime startTime;
    private Integer duration;
    private Location ChosenLocation;

    public ActivityPart(Integer id, LocalTime startTime, Integer duration)
    {
        this.id =id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalTime calculateEndTime()
    {
        return startTime.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return "ActivityPart{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                ", location=" + (ChosenLocation != null ? ChosenLocation.getLocationName() : "Unassigned") +
                ", endTime=" + calculateEndTime() +
                '}';
    }
}
