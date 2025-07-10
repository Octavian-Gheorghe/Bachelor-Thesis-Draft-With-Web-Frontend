package org.example.RSC.Controller.Constraint;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityPartDistanceConstraintDTO;
import org.example.RSC.Entity.Constraint.ActivityPartDistanceConstraint;
import org.example.RSC.Service.Constraint.ActivityPartDistanceConstraintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/activity-part-distance-constraints")
@RequiredArgsConstructor
public class ActivityPartDistanceConstraintController {
    private final ActivityPartDistanceConstraintService svc;

    @PostMapping
    public ResponseEntity<ActivityPartDistanceConstraint> create(@RequestBody ActivityPartDistanceConstraintDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityPartDistanceConstraint> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivityPartDistanceConstraint>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityPartDistanceConstraint> update(@PathVariable Integer id, @RequestBody ActivityPartDistanceConstraintDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}