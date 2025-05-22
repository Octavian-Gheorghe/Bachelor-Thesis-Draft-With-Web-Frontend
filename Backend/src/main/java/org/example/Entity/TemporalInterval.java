package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class TemporalInterval
{
    private LocalTime startTime;
    private LocalTime endTime;

    public Integer getWeight()
    {
        return Math.toIntExact(Math.abs(Duration.between(
                endTime,
                startTime
        ).toMinutes()));
    }
}
