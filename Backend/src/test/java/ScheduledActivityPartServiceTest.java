package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ScheduledActivityPartDTO;
import org.example.RSC.Entity.Schedule;
import org.example.RSC.Entity.ScheduledActivityPart;
import org.example.RSC.Repository.ScheduleRepository;
import org.example.RSC.Repository.ScheduledActivityPartRepository;
import org.example.RSC.Service.ScheduledActivityPartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduledActivityPartServiceTest {

    @Mock
    private ScheduledActivityPartRepository partRepo;

    @Mock
    private ScheduleRepository scheduleRepo;

    @InjectMocks
    private ScheduledActivityPartService service;

    private Schedule schedule;
    private ScheduledActivityPart part;
    private ScheduledActivityPartDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        schedule = new Schedule();
        schedule.setId(1);
        schedule.setName("Week 1");

        part = new ScheduledActivityPart();
        part.setId(1);
        part.setName("Morning Workout");
        part.setStartTime(LocalDateTime.of(2025, 6, 20, 7, 0));
        part.setDuration(60);
        part.setLocationName("Gym");
        part.setSchedule(schedule);

        dto = new ScheduledActivityPartDTO(
                1,
                "Morning Workout",
                LocalDateTime.of(2025, 6, 20, 7, 0),
                60,
                "Gym"
        );
    }

    @Test
    void create_success() {
        when(scheduleRepo.findById(1)).thenReturn(Optional.of(schedule));
        when(partRepo.save(any())).thenReturn(part);

        ScheduledActivityPart result = service.create(dto);
        assertEquals("Morning Workout", result.getName());
        assertEquals("Gym", result.getLocationName());
    }

    @Test
    void create_scheduleNotFound_throws() {
        when(scheduleRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(partRepo.findById(1)).thenReturn(Optional.of(part));
        ScheduledActivityPart result = service.getById(1);
        assertEquals("Morning Workout", result.getName());
    }

    @Test
    void getById_notFound_throws() {
        when(partRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(partRepo.findAll()).thenReturn(List.of(part));
        List<ScheduledActivityPart> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void getAll_empty() {
        when(partRepo.findAll()).thenReturn(List.of());
        List<ScheduledActivityPart> result = service.getAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void update_success() {
        when(scheduleRepo.findById(1)).thenReturn(Optional.of(schedule));
        when(partRepo.findById(1)).thenReturn(Optional.of(part));
        when(partRepo.save(any())).thenReturn(part);

        ScheduledActivityPart result = service.update(1, dto);
        assertEquals("Morning Workout", result.getName());
    }

    @Test
    void update_scheduleNotFound_throws() {
        when(scheduleRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_partNotFound_throws() {
        when(scheduleRepo.findById(1)).thenReturn(Optional.of(schedule));
        when(partRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(partRepo, times(1)).deleteById(1);
    }
}