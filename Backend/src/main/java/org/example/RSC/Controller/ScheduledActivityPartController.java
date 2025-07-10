package org.example.RSC.Controller;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ScheduledActivityPartDTO;
import org.example.RSC.Entity.ScheduledActivityPart;
import org.example.RSC.Service.ScheduledActivityPartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduled-activity-parts")
@RequiredArgsConstructor
public class ScheduledActivityPartController {

    private final ScheduledActivityPartService service;

    @PostMapping
    public ResponseEntity<ScheduledActivityPart> create(@RequestBody ScheduledActivityPartDTO part) {
        return new ResponseEntity<>(service.create(part), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledActivityPart> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScheduledActivityPart>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduledActivityPart> update(@PathVariable Integer id, @RequestBody ScheduledActivityPartDTO part) {
        return ResponseEntity.ok(service.update(id, part));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
