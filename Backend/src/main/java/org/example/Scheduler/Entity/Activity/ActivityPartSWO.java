package org.example.Scheduler.Entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.LocationSWO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityPartSWO
{
    private Integer id;
    private Integer tij;
    private Integer durij;
    private LocationSWO lij;

    public ActivityPartSWO(Integer id, Integer startTime, Integer duration)
    {
        this.id = id;
        this.tij = startTime;
        this.durij = duration;
    }

    public Integer calculateEndTime()
    {
        return tij + durij;
    }

    @Override
    public String toString() {
        return "ActivityPart{" +
                "id=" + id +
                ", startTime=" + tij +
                ", duration=" + durij +
                ", location=" + (lij != null ? lij.getLocationName() : "Unassigned") +
                ", endTime=" + calculateEndTime() +
                '}';
    }
}
