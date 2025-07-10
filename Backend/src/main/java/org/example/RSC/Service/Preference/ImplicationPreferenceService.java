package org.example.RSC.Service.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ImplicationPreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ImplicationPreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ImplicationPreferenceRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImplicationPreferenceService {
    private final ImplicationPreferenceRepository repo;
    private final ActivityIdeaRepository actRepo;
    public ImplicationPreference create(ImplicationPreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdeaInitial_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaInitial_id() + " not found"));

        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdeaImplied_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaImplied_id() + " not found"));

        ImplicationPreference pref = ImplicationPreference.builder()
                .activityIdeaInitial(idea1)
                .activityIdeaImplied(idea2)
                .build();
        return repo.save(pref);
    }
    public ImplicationPreference getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"ImplicationPreference not found");
    }
    public List<ImplicationPreference> getAll()
    {
        return repo.findAll();
    }
    public ImplicationPreference update(Integer id, ImplicationPreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdeaInitial_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaInitial_id() + " not found"));
        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdeaImplied_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaImplied_id() + " not found"));
        ImplicationPreference tgt=getById(id);
        tgt.setActivityIdeaInitial(idea1);
        tgt.setActivityIdeaImplied(idea2);
        return repo.save(tgt);
    }
    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}