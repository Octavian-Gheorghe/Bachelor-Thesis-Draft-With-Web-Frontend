package org.example.RSC.Service.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityScheduleTimePreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivityScheduleTimePreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivityScheduleTimePreferenceRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityScheduleTimePreferenceService {
    private final ActivityScheduleTimePreferenceRepository repo;
    private final ActivityIdeaRepository actRepo;
    public ActivityScheduleTimePreference create(ActivityScheduleTimePreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));

        ActivityScheduleTimePreference pref = ActivityScheduleTimePreference.builder()
                .activityIdea(idea1)
                .type(dto.getType())
                .timeOfAnalysis(dto.getTimeOfAnalysis())
                .build();
        return repo.save(pref);
    }
    public ActivityScheduleTimePreference getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"ActivityScheduleTimePreference not found");
    }
    public List<ActivityScheduleTimePreference> getAll()
    {
        return repo.findAll();
    }
    public ActivityScheduleTimePreference update(Integer id, ActivityScheduleTimePreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdea_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdea_id() + " not found"));
        ActivityScheduleTimePreference tgt=getById(id);
        tgt.setActivityIdea(idea1);
        tgt.setTimeOfAnalysis(dto.getTimeOfAnalysis());
        tgt.setType(dto.getType());
        return repo.save(tgt);
    }
    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}