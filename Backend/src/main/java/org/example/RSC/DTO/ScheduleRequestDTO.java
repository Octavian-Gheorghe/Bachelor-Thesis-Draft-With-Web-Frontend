package org.example.RSC.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ScheduleRequestDTO {
    private String name;
    private List<Integer> activityIdeaIds;
}