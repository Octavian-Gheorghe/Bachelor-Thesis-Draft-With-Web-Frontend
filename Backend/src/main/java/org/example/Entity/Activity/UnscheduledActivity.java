package org.example.Entity.Activity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//Entry data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UnscheduledActivity
{
    private Integer id;
    private String name;

    //the duration range
    private Integer minimumDuration;
    private Integer maximumDuration;

    //interruptibility
    private Integer numberOfMaxPartsInWhichCanBeSplit;
    private List<ActivityPart> partsOfTheActivity;
}
