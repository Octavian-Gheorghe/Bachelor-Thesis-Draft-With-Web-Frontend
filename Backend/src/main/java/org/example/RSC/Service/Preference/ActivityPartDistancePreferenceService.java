package org.example.RSC.Service.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityPartDistancePreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivityPartDistancePreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivityPartDistancePreferenceRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityPartDistancePreferenceService {
    private final ActivityPartDistancePreferenceRepository repo;
    private final ActivityIdeaRepository actRepo;
    public ActivityPartDistancePreference create(ActivityPartDistancePreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));

        ActivityPartDistancePreference pref = ActivityPartDistancePreference.builder()
                .activityIdea(idea1)
                .distance(dto.getDistance())
                .type(dto.getType())
                .build();
        return repo.save(pref);
    }
    public ActivityPartDistancePreference getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"ActivityPartDistancePreference not found");
    }
    public List<ActivityPartDistancePreference> getAll()
    {
        return repo.findAll();
    }
    public ActivityPartDistancePreference update(Integer id, ActivityPartDistancePreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));
        ActivityPartDistancePreference tgt=getById(id);
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

