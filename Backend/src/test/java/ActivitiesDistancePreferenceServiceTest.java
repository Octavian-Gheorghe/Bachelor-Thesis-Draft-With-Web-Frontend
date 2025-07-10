package test.java;
import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ActivitiesDistancePreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivitiesDistancePreference;
import org.example.RSC.Enum.DistanceType;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivitiesDistancePreferenceRepository;
import org.example.RSC.Service.Preference.ActivitiesDistancePreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivitiesDistancePreferenceServiceTest {

    @Mock
    private ActivitiesDistancePreferenceRepository repo;

    @Mock
    private ActivityIdeaRepository actRepo;

    @InjectMocks
    private ActivitiesDistancePreferenceService service;

    private ActivityIdea idea1, idea2;
    private ActivitiesDistancePreference pref;
    private ActivitiesDistancePreferenceDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea1 = ActivityIdea.builder().id(1).name("Idea 1").build();
        idea2 = ActivityIdea.builder().id(2).name("Idea 2").build();

        pref = ActivitiesDistancePreference.builder()
                .id(1)
                .activityIdea1(idea1)
                .activityIdea2(idea2)
                .distance(20)
                .type(DistanceType.MINIMUM)
                .build();

        dto = new ActivitiesDistancePreferenceDTO(1, 2, 20, DistanceType.MINIMUM);
    }

    @Test
    void create_success() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(repo.save(any())).thenReturn(pref);

        ActivitiesDistancePreference result = service.create(dto);

        assertEquals(20, result.getDistance());
        assertEquals(DistanceType.MINIMUM, result.getType());
    }

    @Test
    void create_activity1NotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void create_activity2NotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(repo.findById(1)).thenReturn(Optional.of(pref));

        ActivitiesDistancePreference result = service.getById(1);
        assertEquals(20, result.getDistance());
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(repo.findAll()).thenReturn(List.of(pref));

        List<ActivitiesDistancePreference> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void update_success() {
        when(repo.findById(1)).thenReturn(Optional.of(pref));
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(repo.save(any())).thenReturn(pref);

        ActivitiesDistancePreference result = service.update(1, dto);

        assertEquals(20, result.getDistance());
    }

    @Test
    void update_activity1NotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_activity2NotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(repo, times(1)).deleteById(1);
    }
}
