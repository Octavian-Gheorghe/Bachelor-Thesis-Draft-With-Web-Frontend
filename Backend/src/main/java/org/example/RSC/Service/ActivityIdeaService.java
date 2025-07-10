package org.example.RSC.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ActivityIdeaDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.UserEntity;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityIdeaService {
    private final ActivityIdeaRepository repository;
    private final UserRepository userRepo;

    public ActivityIdea create(ActivityIdeaDTO dto)
    {
        UserEntity user = userRepo.findByUsername(dto.getUser_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("User " + dto.getUser_id() + " not found"));

        ActivityIdea idea = ActivityIdea.builder()
                .name(dto.getName())
                .durimin(dto.getDurimin())
                .durimax(dto.getDurimax())
                .smin(dto.getSmin())
                .smax(dto.getSmax())
                .dmin(dto.getDmin())
                .dmax(dto.getDmax())
                .user(user)
                .build();

        return repository.save(idea);
    }

    public List<ActivityIdea> findAllForUsername(String username)
    {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with username" + username+ " not found"));
        int id = user.getId();
        return repository.findByUser_Id(id);
    }

    public List<ActivityIdea> findAll()
    {
        return repository.findAll();
    }

    public ActivityIdea findById(int id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ActivityIdea not found"));
    }

    public ActivityIdea update(int id, ActivityIdeaDTO dto)
    {
        UserEntity user = userRepo.findByUsername(dto.getUser_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("User " + dto.getUser_id() + " not found"));

        ActivityIdea existing = findById(id);
        existing.setName(dto.getName());
        existing.setDurimin(dto.getDurimin());
        existing.setDurimax(dto.getDurimax());
        existing.setSmin(dto.getSmin());
        existing.setSmax(dto.getSmax());
        existing.setDmin(dto.getDmin());
        existing.setDmax(dto.getDmax());
        existing.setUser(user);
        return repository.save(existing);
    }

    public void delete(int id)
    {
        repository.deleteById(id);
    }

    public List<ActivityIdea> getIdeasForUser(Integer userId)
    {
        return repository.findByUser_Id(userId);
    }

    public ActivityIdea getFullActivity(Integer id)
    {
        return repository.findDetailedById(id).orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + id + " not found"));
    }

}
