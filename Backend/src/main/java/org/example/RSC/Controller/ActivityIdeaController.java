package org.example.RSC.Controller;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityIdeaDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Service.ActivityIdeaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-ideas")
@RequiredArgsConstructor
public class ActivityIdeaController
{
    private final ActivityIdeaService service;

    @PostMapping
    public ResponseEntity<ActivityIdea> create(@RequestBody ActivityIdeaDTO activityIdea) {
        return new ResponseEntity<>(service.create(activityIdea), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ActivityIdea> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ActivityIdea getById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/for/{username}")
    public List<ActivityIdea> getByUserId(@PathVariable String username) {
        return service.findAllForUsername(username);
    }

    @PutMapping("/{id}")
    public ActivityIdea update(@PathVariable int id, @RequestBody ActivityIdeaDTO updated) {
        return service.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}