package org.example.RSC.Controller.Constraint;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.OrderingConstraintDTO;
import org.example.RSC.Entity.Constraint.OrderingConstraint;
import org.example.RSC.Service.Constraint.OrderingConstraintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordering-constraints")
@RequiredArgsConstructor
public class OrderingConstraintController {
    private final OrderingConstraintService svc;

    @PostMapping
    public ResponseEntity<OrderingConstraint> create(@RequestBody OrderingConstraintDTO e) {
        return new ResponseEntity<>(svc.create(e), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderingConstraint> get(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderingConstraint>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderingConstraint> update(@PathVariable Integer id, @RequestBody OrderingConstraintDTO e) {
        return ResponseEntity.ok(svc.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}