package test.java;


import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ActivityDurationPreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivityDurationPreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivityDurationPreferenceRepository;
import org.example.RSC.Service.Preference.ActivityDurationPreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivityDurationPreferenceServiceTest {

    @Mock
    private ActivityDurationPreferenceRepository prefRepo;

    @Mock
    private ActivityIdeaRepository ideaRepo;

    @InjectMocks
    private ActivityDurationPreferenceService service;

    private ActivityIdea idea;
    private ActivityDurationPreference pref;
    private ActivityDurationPreferenceDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea = ActivityIdea.builder().id(1).name("Yoga").build();
        pref = ActivityDurationPreference.builder()
                .id(1)
                .activityIdea(idea)
                .minimumDuration(30)
                .maximumDuration(90)
                .build();
        dto = new ActivityDurationPreferenceDTO(1, 30, 90);
    }

    @Test
    void create_success() {
        when(ideaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(prefRepo.save(any())).thenReturn(pref);

        ActivityDurationPreference result = service.create(dto);

        assertEquals(30, result.getMinimumDuration());
        assertEquals(90, result.getMaximumDuration());
    }

    @Test
    void create_ideaNotFound_throws() {
        when(ideaRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(prefRepo.findById(1)).thenReturn(Optional.of(pref));
        assertEquals(pref, service.getById(1));
    }

    @Test
    void getById_notFound_throws() {
        when(prefRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(prefRepo.findAll()).thenReturn(List.of(pref));
        List<ActivityDurationPreference> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void getAll_empty() {
        when(prefRepo.findAll()).thenReturn(List.of());
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void update_success() {
        ActivityDurationPreference updated = ActivityDurationPreference.builder()
                .id(1)
                .activityIdea(idea)
                .minimumDuration(45)
                .maximumDuration(60)
                .build();

        when(ideaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(prefRepo.findById(1)).thenReturn(Optional.of(pref));
        when(prefRepo.save(any())).thenReturn(updated);

        ActivityDurationPreference result = service.update(1, new ActivityDurationPreferenceDTO(1, 45, 60));

        assertEquals(45, result.getMinimumDuration());
        assertEquals(60, result.getMaximumDuration());
    }

    @Test
    void update_ideaNotFound_throws() {
        when(ideaRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_prefNotFound_throws() {
        when(ideaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(prefRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(prefRepo, times(1)).deleteById(1);
    }
}
