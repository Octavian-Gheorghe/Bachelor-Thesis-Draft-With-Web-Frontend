package org.example.RSC.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.ScheduledActivityPartDTO;
import org.example.RSC.Entity.Schedule;
import org.example.RSC.Entity.ScheduledActivityPart;
import org.example.RSC.Repository.ScheduleRepository;
import org.example.RSC.Repository.ScheduledActivityPartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledActivityPartService
{
    private final ScheduledActivityPartRepository repository;
    private final ScheduleRepository scheduleRepository;

    public ScheduledActivityPart create(ScheduledActivityPartDTO dto)
    {
        Schedule idea = scheduleRepository.findById(dto.getSchedule_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Schedule " +
                                dto.getSchedule_id() + " not found"));

        ScheduledActivityPart part = ScheduledActivityPart.builder()
                .schedule(idea)
                .name(dto.getName())
                .duration(dto.getDuration())
                .locationName(dto.getLocationName())
                .startTime(dto.getStartTime())
                .build();

        return repository.save(part);
    }

    public ScheduledActivityPart getById(Integer id)
    {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("ScheduledActivityPart not found with id " + id));
    }

    public List<ScheduledActivityPart> getAll()
    {
        return repository.findAll();
    }

    public ScheduledActivityPart update(Integer id, ScheduledActivityPartDTO dto)
    {
        Schedule idea = scheduleRepository.findById(dto.getSchedule_id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Schedule " +
                                dto.getSchedule_id() + " not found"));
        ScheduledActivityPart existing = getById(id);
        existing.setName(dto.getName());
        existing.setStartTime(dto.getStartTime());
        existing.setDuration(dto.getDuration());
        existing.setLocationName(dto.getLocationName());
        existing.setSchedule(idea);
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}