package org.example.RSC.Service.Preference;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.OrderingPreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.OrderingPreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.OrderingPreferenceRepository;
import org.example.RSC.Service.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderingPreferenceService {
    private final OrderingPreferenceRepository repo;
    private final ActivityIdeaRepository actRepo;
    public OrderingPreference create(OrderingPreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdeaBefore_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaBefore_id() + " not found"));
        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdeaAfter_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaAfter_id() + " not found"));

        OrderingPreference pref = OrderingPreference.builder()
                .activityIdeaBefore(idea1)
                .activityIdeaAfter(idea2)
                .build();
        return repo.save(pref);
    }
    public OrderingPreference getById(Integer id)
    {
        return ServiceUtil.requirePresent(repo.findById(id),"OrderingPreference not found");
    }
    public List<OrderingPreference> getAll()
    {
        return repo.findAll();
    }
    public OrderingPreference update(Integer id, OrderingPreferenceDTO dto)
    {
        ActivityIdea idea1 = actRepo.findById(dto.getActivityIdeaBefore_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaBefore_id() + " not found"));
        ActivityIdea idea2 = actRepo.findById(dto.getActivityIdeaAfter_id()).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + dto.getActivityIdeaAfter_id() + " not found"));
        OrderingPreference tgt=getById(id);
        tgt.setActivityIdeaBefore(idea1);
        tgt.setActivityIdeaAfter(idea2);
        return repo.save(tgt);
    }
    public void delete(Integer id)
    {
        repo.deleteById(id);
    }
}
