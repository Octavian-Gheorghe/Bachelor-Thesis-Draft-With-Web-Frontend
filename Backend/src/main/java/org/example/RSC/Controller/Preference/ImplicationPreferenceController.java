package org.example.RSC.Controller.Preference;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ImplicationPreferenceDTO;
import org.example.RSC.Entity.Preference.ImplicationPreference;
import org.example.RSC.Service.Preference.ImplicationPreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/implication-preferences")
@RequiredArgsConstructor
public class ImplicationPreferenceController {
    private final ImplicationPreferenceService svc;

    @PostMapping
    public ResponseEntity<ImplicationPreference> create(@RequestBody ImplicationPreferenceDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImplicationPreference> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ImplicationPreference>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImplicationPreference> update(@PathVariable Integer id, @RequestBody ImplicationPreferenceDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}