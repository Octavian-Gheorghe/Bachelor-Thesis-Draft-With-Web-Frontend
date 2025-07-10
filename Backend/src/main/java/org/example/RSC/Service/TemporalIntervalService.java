package org.example.RSC.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.TemporalIntervalDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.TemporalInterval;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.TemporalIntervalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class TemporalIntervalService {
    private final TemporalIntervalRepository repo;
    private final ActivityIdeaRepository actRepo;

    public List<TemporalInterval> findAll()
    {
        return repo.findAll();
    }

    public TemporalInterval findOne(Integer id)
    {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Interval "+id+" not found"));
    }

    public TemporalInterval save(TemporalIntervalDTO dto)
    {
        ActivityIdea idea = actRepo.findById(dto.getActivityIdea_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("ActivityIdea " +
                                dto.getActivityIdea_id() + " not found"));

        TemporalInterval ti = TemporalInterval.builder()
                .activityIdea(idea)
                .startInterval(dto.getStartInterval())
                .endInterval(dto.getEndInterval())
                .build();
        return repo.save(ti);
    }

    public TemporalInterval update(Integer id, TemporalIntervalDTO dto)
    {
        ActivityIdea idea = actRepo.findById(dto.getActivityIdea_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("ActivityIdea " +
                                dto.getActivityIdea_id() + " not found"));
        if(!repo.existsById(id))
            throw new EntityNotFoundException("Interval "+id+" not found");
        TemporalInterval t = findOne(id);
        t.setStartInterval(dto.getStartInterval());
        t.setEndInterval(dto.getEndInterval());
        t.setActivityIdea(idea);
        return repo.save(t);
    }

    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}
