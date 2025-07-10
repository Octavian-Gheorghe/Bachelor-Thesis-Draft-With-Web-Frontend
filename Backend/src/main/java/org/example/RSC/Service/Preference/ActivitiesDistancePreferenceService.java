package org.example.RSC.Service.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivitiesDistancePreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivitiesDistancePreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivitiesDistancePreferenceRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivitiesDistancePreferenceService
{
    private final ActivitiesDistancePreferenceRepository repo;
    private final ActivityIdeaRepository actRepo;
    public ActivitiesDistancePreference create(ActivitiesDistancePreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea1_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea1_id() + " not found"));
        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdea2_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea2_id() + " not found"));

        ActivitiesDistancePreference pref = ActivitiesDistancePreference.builder()
                .activityIdea1(idea1)
                .activityIdea2(idea2)
                .distance(dto.getDistance())
                .type(dto.getType())
                .build();
        return repo.save(pref);
    }
    public ActivitiesDistancePreference getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"ActivitiesDistancePreference not found");
    }
    public List<ActivitiesDistancePreference> getAll()
    {
        return repo.findAll();
    }
    public ActivitiesDistancePreference update(Integer id, ActivitiesDistancePreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea1_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " +dto.getActivityIdea1_id() + " not found"));
        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdea2_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea2_id() + " not found"));
        ActivitiesDistancePreference tgt=getById(id);
        tgt.setActivityIdea1(idea1);
        tgt.setActivityIdea2(idea2);
        tgt.setDistance(dto.getDistance());
        tgt.setType(dto.getType());
        return repo.save(tgt);
    }
    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}