package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ScheduleDTO;
import org.example.RSC.Entity.Schedule;
import org.example.RSC.Entity.UserEntity;
import org.example.RSC.Repository.ScheduleRepository;
import org.example.RSC.Repository.UserRepository;
import org.example.RSC.Service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private ScheduleService service;

    private UserEntity testUser;
    private Schedule testSchedule;
    private ScheduleDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setName("Weekly Plan");
        testSchedule.setStartInterval(LocalDateTime.of(2025, 6, 1, 8, 0));
        testSchedule.setEndInterval(LocalDateTime.of(2025, 6, 7, 20, 0));
        testSchedule.setUser(testUser);

        dto = new ScheduleDTO(
                "Weekly Plan",
                LocalDateTime.of(2025, 6, 1, 8, 0),
                LocalDateTime.of(2025, 6, 7, 20, 0),
                1
        );
    }

    @Test
    void create_success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(testUser));
        when(scheduleRepo.save(any())).thenReturn(testSchedule);

        Schedule result = service.create(dto);

        assertEquals("Weekly Plan", result.getName());
        assertEquals(testUser, result.getUser());
    }

    @Test
    void create_userNotFound_throws() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void findAllByUsername_success() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(scheduleRepo.findByUser_Id(1)).thenReturn(List.of(testSchedule));

        List<Schedule> result = service.findAllByUsername("testuser");

        assertEquals(1, result.size());
        assertEquals("Weekly Plan", result.get(0).getName());
    }

    @Test
    void findAllByUsername_userNotFound_throws() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findAllByUsername("ghost"));
    }

    @Test
    void findAll_success() {
        when(scheduleRepo.findAll()).thenReturn(List.of(testSchedule));

        List<Schedule> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_empty() {
        when(scheduleRepo.findAll()).thenReturn(List.of());

        List<Schedule> result = service.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_success() {
        when(scheduleRepo.findById(1)).thenReturn(Optional.of(testSchedule));

        Schedule result = service.findById(1);
        assertEquals("Weekly Plan", result.getName());
    }

    @Test
    void findById_notFound_throws() {
        when(scheduleRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(999));
    }

    @Test
    void update_success() {
        Schedule updated = Schedule.builder()
                .id(1)
                .name("Updated Plan")
                .startInterval(dto.getStartInterval())
                .endInterval(dto.getEndInterval())
                .user(testUser)
                .build();

        when(userRepo.findById(1)).thenReturn(Optional.of(testUser));
        when(scheduleRepo.findById(1)).thenReturn(Optional.of(testSchedule));
        when(scheduleRepo.save(any())).thenReturn(updated);

        Schedule result = service.update(1, dto);

        assertEquals("Updated Plan", result.getName());
    }

    @Test
    void update_userNotFound_throws() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_scheduleNotFound_throws() {
        when(userRepo.findById(1)).thenReturn(Optional.of(testUser));
        when(scheduleRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(scheduleRepo, times(1)).deleteById(1);
    }
}