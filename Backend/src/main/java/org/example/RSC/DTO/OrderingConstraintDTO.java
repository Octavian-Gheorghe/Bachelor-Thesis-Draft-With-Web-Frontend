package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class OrderingConstraintDTO
{
    private Integer id;
    private Integer activityIdeaBefore_id;
    private Integer activityIdeaAfter_id;
}
