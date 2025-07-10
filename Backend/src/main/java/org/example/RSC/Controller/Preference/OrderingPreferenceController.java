package org.example.RSC.Controller.Preference;


import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.OrderingPreferenceDTO;
import org.example.RSC.Entity.Preference.OrderingPreference;
import org.example.RSC.Service.Preference.OrderingPreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordering-preferences")
@RequiredArgsConstructor
public class OrderingPreferenceController {
    private final OrderingPreferenceService svc;

    @PostMapping
    public ResponseEntity<OrderingPreference> create(@RequestBody OrderingPreferenceDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderingPreference> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderingPreference>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderingPreference> update(@PathVariable Integer id, @RequestBody OrderingPreferenceDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
