package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.TemporalIntervalDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.TemporalInterval;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.TemporalIntervalRepository;
import org.example.RSC.Service.TemporalIntervalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TemporalIntervalServiceTest {

    @Mock
    private TemporalIntervalRepository intervalRepo;

    @Mock
    private ActivityIdeaRepository activityIdeaRepo;

    @InjectMocks
    private TemporalIntervalService service;

    private ActivityIdea idea;
    private TemporalInterval interval;
    private TemporalIntervalDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idea = ActivityIdea.builder().id(1).name("Jogging").build();

        interval = TemporalInterval.builder()
                .id(10)
                .activityIdea(idea)
                .startInterval(LocalDateTime.of(2024, 1, 1, 10, 0))
                .endInterval(LocalDateTime.of(2024, 1, 1, 11, 0))
                .build();

        dto = new TemporalIntervalDTO(
                interval.getStartInterval(),
                interval.getEndInterval(),
                idea.getId()
        );
    }

    @Test
    void findAll_success() {
        when(intervalRepo.findAll()).thenReturn(List.of(interval));
        List<TemporalInterval> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getId());
    }

    @Test
    void findAll_empty() {
        when(intervalRepo.findAll()).thenReturn(List.of());

        List<TemporalInterval> result = service.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findOne_success() {
        when(intervalRepo.findById(10)).thenReturn(Optional.of(interval));

        TemporalInterval result = service.findOne(10);

        assertEquals(10, result.getId());
    }

    @Test
    void findOne_notFound_throws() {
        when(intervalRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findOne(999));
    }

    @Test
    void save_success() {
        when(activityIdeaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(intervalRepo.save(any())).thenReturn(interval);

        TemporalInterval result = service.save(dto);

        assertEquals("Jogging", result.getActivityIdea().getName());
    }

    @Test
    void save_activityNotFound_throws() {
        when(activityIdeaRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.save(dto));
    }

    @Test
    void update_success() {
        when(activityIdeaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(intervalRepo.existsById(10)).thenReturn(true);
        when(intervalRepo.findById(10)).thenReturn(Optional.of(interval));
        when(intervalRepo.save(any())).thenReturn(interval);

        TemporalInterval updated = service.update(10, dto);

        assertEquals(10, updated.getId());
    }

    @Test
    void update_notFound_throws() {
        when(activityIdeaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(intervalRepo.existsById(999)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.update(999, dto));
    }

    @Test
    void update_activityNotFound_throws() {
        when(activityIdeaRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(10, dto));
    }

    @Test
    void delete_success() {
        service.delete(10);
        verify(intervalRepo, times(1)).deleteById(10);
    }

    @Test
    void delete_checkCall() {
        doNothing().when(intervalRepo).deleteById(10);
        service.delete(10);
        verify(intervalRepo).deleteById(10);
    }
}