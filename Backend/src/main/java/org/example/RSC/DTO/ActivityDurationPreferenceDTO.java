package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class ActivityDurationPreferenceDTO {
    private Integer activityIdea_id;
    private Integer minimumDuration;
    private Integer maximumDuration;
}