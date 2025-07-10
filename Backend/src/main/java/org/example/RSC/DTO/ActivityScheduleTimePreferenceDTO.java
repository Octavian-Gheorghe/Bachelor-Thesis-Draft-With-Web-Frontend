package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.RSC.Enum.TimePreferenceType;

import java.time.LocalDateTime;

@AllArgsConstructor @Getter
public class ActivityScheduleTimePreferenceDTO
{
    private Integer activityIdea_id;
    private LocalDateTime timeOfAnalysis;
    private TimePreferenceType type;
}
