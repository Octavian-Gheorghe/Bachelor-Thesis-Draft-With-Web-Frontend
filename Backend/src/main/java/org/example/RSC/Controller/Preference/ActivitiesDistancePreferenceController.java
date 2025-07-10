package org.example.RSC.Controller.Preference;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivitiesDistancePreferenceDTO;
import org.example.RSC.Entity.Preference.ActivitiesDistancePreference;
import org.example.RSC.Service.Preference.ActivitiesDistancePreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities-distance-preferences")
@RequiredArgsConstructor
public class ActivitiesDistancePreferenceController {
    private final ActivitiesDistancePreferenceService svc;

    @PostMapping
    public ResponseEntity<ActivitiesDistancePreference> create(@RequestBody ActivitiesDistancePreferenceDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivitiesDistancePreference> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivitiesDistancePreference>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivitiesDistancePreference> update(@PathVariable Integer id, @RequestBody ActivitiesDistancePreferenceDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}