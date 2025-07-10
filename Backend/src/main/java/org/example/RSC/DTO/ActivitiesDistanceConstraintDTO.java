package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.RSC.Enum.DistanceType;
@AllArgsConstructor @Getter
public class ActivitiesDistanceConstraintDTO
{
    private Integer activityIdea1_id;
    private Integer activityIdea2_id;
    private Integer distance;
    private DistanceType type;
}
