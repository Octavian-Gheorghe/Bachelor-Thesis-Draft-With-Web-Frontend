package test.java;

import org.example.RSC.DTO.ActivityScheduleTimePreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ActivityScheduleTimePreference;
import org.example.RSC.Enum.TimePreferenceType;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ActivityScheduleTimePreferenceRepository;
import org.example.RSC.Service.Preference.ActivityScheduleTimePreferenceService;
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

class ActivityScheduleTimePreferenceServiceTest {

    @Mock
    private ActivityScheduleTimePreferenceRepository repo;
    @Mock
    private ActivityIdeaRepository actRepo;

    @InjectMocks
    private ActivityScheduleTimePreferenceService service;

    private ActivityIdea idea;
    private ActivityScheduleTimePreferenceDTO dto;
    private ActivityScheduleTimePreference preference;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idea = ActivityIdea.builder().id(1).name("Test Idea").build();
        dto = new ActivityScheduleTimePreferenceDTO(1, LocalDateTime.now(), TimePreferenceType.LATER);
        preference = ActivityScheduleTimePreference.builder()
                .id(1)
                .activityIdea(idea)
                .type(dto.getType())
                .timeOfAnalysis(dto.getTimeOfAnalysis())
                .build();
    }

    @Test
    void create_success() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.save(any())).thenReturn(preference);

        ActivityScheduleTimePreference result = service.create(dto);

        assertNotNull(result);
        assertEquals(idea, result.getActivityIdea());
        verify(repo).save(any());
    }

    @Test
    void create_fail_activityIdeaNotFound() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(repo.findById(1)).thenReturn(Optional.of(preference));

        ActivityScheduleTimePreference result = service.getById(1);

        assertEquals(preference, result);
    }

    @Test
    void getById_fail_notFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_returnsList() {
        when(repo.findAll()).thenReturn(List.of(preference));

        List<ActivityScheduleTimePreference> all = service.getAll();

        assertEquals(1, all.size());
    }

    @Test
    void update_success() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.findById(1)).thenReturn(Optional.of(preference));
        when(repo.save(any())).thenReturn(preference);

        ActivityScheduleTimePreference result = service.update(1, dto);

        assertEquals(idea, result.getActivityIdea());
    }

    @Test
    void update_fail_activityIdeaNotFound() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void update_fail_preferenceNotFound() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(repo).deleteById(1);
    }
}
