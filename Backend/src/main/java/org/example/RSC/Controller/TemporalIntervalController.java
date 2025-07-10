package org.example.RSC.Controller;

import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.TemporalIntervalDTO;
import org.example.RSC.Entity.TemporalInterval;
import org.example.RSC.Service.TemporalIntervalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temporal-intervals")
@RequiredArgsConstructor
public class TemporalIntervalController
{
    private final TemporalIntervalService service;

    @PostMapping
    public ResponseEntity<TemporalInterval> create(@RequestBody TemporalIntervalDTO temporalInterval)
    {
        return new ResponseEntity<>(service.save(temporalInterval), HttpStatus.CREATED);
    }

    @GetMapping
    public List<TemporalInterval> getAll()
    {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TemporalInterval getById(@PathVariable int id)
    {
        return service.findOne(id);
    }

    @PutMapping("/{id}")
    public TemporalInterval update(@PathVariable int id, @RequestBody TemporalIntervalDTO updated)
    {
        return service.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id)
    {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
