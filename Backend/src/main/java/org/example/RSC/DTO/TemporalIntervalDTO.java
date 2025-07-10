package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor @Getter
public class TemporalIntervalDTO
{
    private LocalDateTime startInterval;
    private LocalDateTime endInterval;
    private Integer activityIdea_id;
}
