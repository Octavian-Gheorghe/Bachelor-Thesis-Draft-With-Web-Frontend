package org.example.RSC.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ActivityIdeaDTO
{
    private String  name;
    private Integer durimin;
    private Integer durimax;
    private Integer smin;
    private Integer dmin;
    private Integer smax;
    private Integer dmax;
    private String user_id;
}
