package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor @Getter

public class ScheduleDTO
{
    private String name;

    private LocalDateTime startInterval;
    private LocalDateTime endInterval;
    private Integer user_id;
}
