package org.example.RSC.Service.Constraint;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivitiesDistanceConstraintDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Constraint.ActivitiesDistanceConstraint;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Constraint.ActivitiesDistanceConstraintRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivitiesDistanceConstraintService
{
    private final ActivitiesDistanceConstraintRepository repo;
    private final ActivityIdeaRepository actRepo;
    public ActivitiesDistanceConstraint create(ActivitiesDistanceConstraintDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea1_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea1_id() + " not found"));

        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdea2_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea2_id() + " not found"));

        ActivitiesDistanceConstraint constraint = ActivitiesDistanceConstraint.builder()
                .activityIdea1(idea1)
                .activityIdea2(idea2)
                .distance(dto.getDistance())
                .type(dto.getType())
                .build();
        return repo.save(constraint);
    }
    public ActivitiesDistanceConstraint getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"ActivitiesDistanceConstraint not found");
    }
    public List<ActivitiesDistanceConstraint> getAll()
    {
        return repo.findAll();
    }
    public ActivitiesDistanceConstraint update(Integer id, ActivitiesDistanceConstraintDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea1_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea1_id() + " not found"));

        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdea2_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea2_id() + " not found"));
        ActivitiesDistanceConstraint tgt=getById(id);
        tgt.setActivityIdea1(idea1);
        tgt.setActivityIdea2(idea2);
        tgt.setDistance(dto.getDistance());
        tgt.setType(dto.getType());
        return repo.save(tgt);
    }
    public void delete(Integer id){repo.deleteById(id);}
}
