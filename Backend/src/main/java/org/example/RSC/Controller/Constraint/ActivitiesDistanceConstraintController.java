package org.example.RSC.Controller.Constraint;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivitiesDistanceConstraintDTO;
import org.example.RSC.Entity.Constraint.ActivitiesDistanceConstraint;
import org.example.RSC.Service.Constraint.ActivitiesDistanceConstraintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities-distance-constraints")
@RequiredArgsConstructor
public class ActivitiesDistanceConstraintController {
    private final ActivitiesDistanceConstraintService svc;

    @PostMapping
    public ResponseEntity<ActivitiesDistanceConstraint> create(@RequestBody ActivitiesDistanceConstraintDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivitiesDistanceConstraint> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivitiesDistanceConstraint>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivitiesDistanceConstraint> update(@PathVariable Integer id, @RequestBody ActivitiesDistanceConstraintDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}