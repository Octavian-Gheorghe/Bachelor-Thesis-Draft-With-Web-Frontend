package org.example.Entity.Activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.Entity.Location;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class PlacedBlock {
    private Location location;
    private LocalTime start;
    private LocalTime end;
}