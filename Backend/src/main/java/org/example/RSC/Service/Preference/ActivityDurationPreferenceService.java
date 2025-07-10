package org.example.RSC.Service.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityDurationPreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivityDurationPreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivityDurationPreferenceRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityDurationPreferenceService
{
    private final ActivityDurationPreferenceRepository repo;
    private final ActivityIdeaRepository actRepo;

    public ActivityDurationPreference create(ActivityDurationPreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));

        ActivityDurationPreference pref = ActivityDurationPreference.builder()
                .activityIdea(idea1)
                .maximumDuration(dto.getMaximumDuration())
                .minimumDuration(dto.getMinimumDuration())
                .build();
        return repo.save(pref);
    }
    public ActivityDurationPreference getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"ActivityDurationPreference not found");
    }
    public List<ActivityDurationPreference> getAll()
    {
        return repo.findAll();
    }
    public ActivityDurationPreference update(Integer id, ActivityDurationPreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));
        ActivityDurationPreference tgt=getById(id);
        tgt.setActivityIdea(idea1);
        tgt.setMinimumDuration(dto.getMinimumDuration());
        tgt.setMaximumDuration(dto.getMaximumDuration());
        return repo.save(tgt);
    }
    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}