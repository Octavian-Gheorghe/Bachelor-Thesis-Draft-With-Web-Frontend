package org.example.Scheduler.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TemporalIntervalSWO
{
    private Integer ai;
    private Integer bi;

    public Integer getWeight()
    {
        return bi - ai;
    }
}
