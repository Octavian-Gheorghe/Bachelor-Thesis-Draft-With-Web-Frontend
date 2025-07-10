package org.example.RSC.Controller.Preference;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityPartDistancePreferenceDTO;
import org.example.RSC.Entity.Preference.ActivityPartDistancePreference;
import org.example.RSC.Service.Preference.ActivityPartDistancePreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-part-distance-preferences")
@RequiredArgsConstructor
public class ActivityPartDistancePreferenceController {
    private final ActivityPartDistancePreferenceService svc;

    @PostMapping
    public ResponseEntity<ActivityPartDistancePreference> create(@RequestBody ActivityPartDistancePreferenceDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityPartDistancePreference> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivityPartDistancePreference>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityPartDistancePreference> update(@PathVariable Integer id, @RequestBody ActivityPartDistancePreferenceDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}