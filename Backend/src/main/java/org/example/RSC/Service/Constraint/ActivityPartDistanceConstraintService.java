package org.example.RSC.Service.Constraint;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityPartDistanceConstraintDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Constraint.ActivityPartDistanceConstraint;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Constraint.ActivityPartDistanceConstraintRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityPartDistanceConstraintService
{
    private final ActivityPartDistanceConstraintRepository repo;
    private final ActivityIdeaRepository actRepo;

    public ActivityPartDistanceConstraint create(ActivityPartDistanceConstraintDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " +                                dto.getActivityIdea_id() + " not found"));

        ActivityPartDistanceConstraint constraint = ActivityPartDistanceConstraint.builder()
                .activityIdea(idea1)
                .distance(dto.getDistance())
                .type(dto.getType())
                .build();

        return repo.save(constraint);
    }

    public ActivityPartDistanceConstraint getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id), "ActivityPartDistanceConstraint not found");
    }

    public List<ActivityPartDistanceConstraint> getAll()
    {
        return repo.findAll();
    }

    public ActivityPartDistanceConstraint update(Integer id, ActivityPartDistanceConstraintDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));

        ActivityPartDistanceConstraint tgt = getById(id);
        tgt.setActivityIdea(idea1);
        tgt.setDistance(dto.getDistance());
        tgt.setType(dto.getType());
        return repo.save(tgt);
    }

    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}