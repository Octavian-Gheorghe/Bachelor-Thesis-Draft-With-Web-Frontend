package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor @Getter
public class ScheduledActivityPartDTO
{
    private Integer schedule_id;
    private String name;
    private LocalDateTime startTime;
    private Integer duration;
    private String locationName;
}
