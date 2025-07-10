package org.example.RSC.Controller;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ScheduleDTO;
import org.example.RSC.Entity.Schedule;
import org.example.RSC.Service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService service;

    @PostMapping
    public ResponseEntity<Schedule> create(@RequestBody ScheduleDTO schedule) {
        return new ResponseEntity<>(service.create(schedule), HttpStatus.CREATED);
    }

    @GetMapping("/for/{username}")
    public List<Schedule> getByUserId(@PathVariable String username) {
        return service.findAllByUsername(username);
    }

    @GetMapping
    public List<Schedule> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Schedule getById(@PathVariable int id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Schedule update(@PathVariable int id, @RequestBody ScheduleDTO updated) {
        return service.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
