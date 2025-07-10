package org.example.RSC.Controller.Preference;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityScheduleTimePreferenceDTO;
import org.example.RSC.Entity.Preference.ActivityScheduleTimePreference;
import org.example.RSC.Service.Preference.ActivityScheduleTimePreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/activity-schedule-time-preferences")
@RequiredArgsConstructor
public class ActivityScheduleTimePreferenceController {
    private final ActivityScheduleTimePreferenceService svc;

    @PostMapping
    public ResponseEntity<ActivityScheduleTimePreference> create(@RequestBody ActivityScheduleTimePreferenceDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityScheduleTimePreference> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivityScheduleTimePreference>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityScheduleTimePreference> update(@PathVariable Integer id, @RequestBody ActivityScheduleTimePreferenceDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}