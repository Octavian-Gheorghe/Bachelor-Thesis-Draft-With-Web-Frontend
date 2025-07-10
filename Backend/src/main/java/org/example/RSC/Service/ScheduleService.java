package org.example.RSC.Service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ScheduleDTO;
import org.example.RSC.Entity.Schedule;
import org.example.RSC.Entity.UserEntity;
import org.example.RSC.Repository.ScheduleRepository;
import org.example.RSC.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository repository;
    private final UserRepository userRepo;

    public Schedule create(ScheduleDTO dto)
    {
        UserEntity user = userRepo.findById(dto.getUser_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("User " + dto.getUser_id() + " not found"));

        Schedule idea = Schedule.builder()
                .name(dto.getName())
                .startInterval(dto.getStartInterval())
                .endInterval(dto.getEndInterval())
                .user(user)
                .build();
        return repository.save(idea);
    }

    public List<Schedule> findAllByUsername(String username)
    {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("User " + username+ " not found"));
        int id = user.getId();
        return repository.findByUser_Id(id);
    }

    public List<Schedule> findAll() {
        return repository.findAll();
    }

    public Schedule findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    public Schedule update(int id, ScheduleDTO dto)
    {
        UserEntity user = userRepo.findById(dto.getUser_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("User " + dto.getUser_id() + " not found"));
        Schedule existing = findById(id);
        existing.setName(dto.getName());
        existing.setStartInterval(dto.getStartInterval());
        existing.setEndInterval(dto.getEndInterval());
        existing.setUser(user);
        return repository.save(existing);
    }

    public void delete(int id)
    {
        repository.deleteById(id);
    }
}
