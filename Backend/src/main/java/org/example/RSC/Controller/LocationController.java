package org.example.RSC.Controller;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.LocationDTO;
import org.example.RSC.Entity.Location;
import org.example.RSC.Service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController
{
    private final LocationService service;

    @PostMapping
    public ResponseEntity<Location> create(@RequestBody LocationDTO location)
    {
        return new ResponseEntity<>(service.save(location), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Location> getAll()
    {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Location getById(@PathVariable int id)
    {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Location update(@PathVariable int id, @RequestBody LocationDTO updated)
    {
        return service.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id)
    {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{locationId}/map-to-activity/{activityId}")
    public ResponseEntity<Location> mapToActivity(@PathVariable Integer locationId, @PathVariable Integer activityId) {
        Location updatedLocation = service.mapLocationToActivity(locationId, activityId);
        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}/unmap-to-activity/{activityId}")
    public ResponseEntity<Location> unmapToActivity(@PathVariable Integer locationId, @PathVariable Integer activityId) {
        Location updatedLocation = service.unmapLocationToActivity(locationId, activityId);
        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }
}
