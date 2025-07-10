package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.RSC.Enum.DistanceType;

@AllArgsConstructor @Getter
public class ActivityPartDistancePreferenceDTO {
    private Integer activityIdea_id;
    private Integer distance;
    private DistanceType type;
}
