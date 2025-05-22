package org.example.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Entity.Location;
import org.example.Entity.TemporalInterval;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ActivityDTO
{
    private Integer id;
    private String name;
    private Integer duration;

    private Integer minPartDuration;
    private Integer maxPartDuration;
    private Integer minGapBetweenParts;
    private Integer maxGapBetweenParts;

    private List<TemporalInterval> intervals;
    private List<Location> locations;


    
}
