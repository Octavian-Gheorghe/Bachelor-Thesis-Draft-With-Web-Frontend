package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ActivityPartDistancePreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivityPartDistancePreference;
import org.example.RSC.Enum.DistanceType;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivityPartDistancePreferenceRepository;
import org.example.RSC.Service.Preference.ActivityPartDistancePreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivityPartDistancePreferenceServiceTest {

    @Mock
    private ActivityPartDistancePreferenceRepository repo;

    @Mock
    private ActivityIdeaRepository actRepo;

    @InjectMocks
    private ActivityPartDistancePreferenceService service;

    private ActivityIdea idea;
    private ActivityPartDistancePreference pref;
    private ActivityPartDistancePreferenceDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea = ActivityIdea.builder()
                .id(1)
                .name("Idea 1")
                .build();

        pref = ActivityPartDistancePreference.builder()
                .id(1)
                .activityIdea(idea)
                .distance(15)
                .type(DistanceType.MINIMUM)
                .build();

        dto = new ActivityPartDistancePreferenceDTO(1, 15, DistanceType.MINIMUM);
    }

    @Test
    void create_success() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.save(any())).thenReturn(pref);

        ActivityPartDistancePreference result = service.create(dto);
        assertEquals(15, result.getDistance());
    }

    @Test
    void create_ideaNotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(repo.findById(1)).thenReturn(Optional.of(pref));
        ActivityPartDistancePreference result = service.getById(1);
        assertEquals(DistanceType.MINIMUM, result.getType());
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(repo.findAll()).thenReturn(List.of(pref));
        List<ActivityPartDistancePreference> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void getAll_empty() {
        when(repo.findAll()).thenReturn(List.of());
        List<ActivityPartDistancePreference> result = service.getAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void update_success() {
        ActivityPartDistancePreference updated = ActivityPartDistancePreference.builder()
                .id(1)
                .activityIdea(idea)
                .distance(25)
                .type(DistanceType.MAXIMUM)
                .build();

        ActivityPartDistancePreferenceDTO updateDTO = new ActivityPartDistancePreferenceDTO(1, 25, DistanceType.MAXIMUM);

        when(actRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.findById(1)).thenReturn(Optional.of(pref));
        when(repo.save(any())).thenReturn(updated);

        ActivityPartDistancePreference result = service.update(1, updateDTO);
        assertEquals(25, result.getDistance());
        assertEquals(DistanceType.MAXIMUM, result.getType());
    }

    @Test
    void update_ideaNotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_prefNotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(repo, times(1)).deleteById(1);
    }
}
