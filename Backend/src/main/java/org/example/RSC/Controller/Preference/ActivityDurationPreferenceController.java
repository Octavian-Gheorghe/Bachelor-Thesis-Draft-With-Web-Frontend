package org.example.RSC.Controller.Preference;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityDurationPreferenceDTO;
import org.example.RSC.Entity.Preference.ActivityDurationPreference;
import org.example.RSC.Service.Preference.ActivityDurationPreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-duration-preferences")
@RequiredArgsConstructor
public class ActivityDurationPreferenceController {
    private final ActivityDurationPreferenceService svc;

    @PostMapping
    public ResponseEntity<ActivityDurationPreference> create(@RequestBody ActivityDurationPreferenceDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDurationPreference> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivityDurationPreference>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDurationPreference> update(@PathVariable Integer id, @RequestBody ActivityDurationPreferenceDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}