package test.java;


import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ImplicationPreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.ImplicationPreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.ImplicationPreferenceRepository;
import org.example.RSC.Service.Preference.ImplicationPreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImplicationPreferenceServiceTest {

    @Mock
    private ImplicationPreferenceRepository repo;

    @Mock
    private ActivityIdeaRepository actRepo;

    @InjectMocks
    private ImplicationPreferenceService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_success() {
        ActivityIdea initial = new ActivityIdea();
        initial.setId(1);
        ActivityIdea implied = new ActivityIdea();
        implied.setId(2);

        ImplicationPreferenceDTO dto = new ImplicationPreferenceDTO(1, 2);
        ImplicationPreference saved = ImplicationPreference.builder()
                .activityIdeaInitial(initial)
                .activityIdeaImplied(implied)
                .build();

        when(actRepo.findById(1)).thenReturn(Optional.of(initial));
        when(actRepo.findById(2)).thenReturn(Optional.of(implied));
        when(repo.save(any())).thenReturn(saved);

        ImplicationPreference result = service.create(dto);

        assertEquals(initial, result.getActivityIdeaInitial());
        assertEquals(implied, result.getActivityIdeaImplied());
    }

    @Test
    void create_failure_missingInitial() {
        ImplicationPreferenceDTO dto = new ImplicationPreferenceDTO(1, 2);
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        ImplicationPreference pref = new ImplicationPreference();
        pref.setId(1);
        when(repo.findById(1)).thenReturn(Optional.of(pref));
        assertEquals(pref, service.getById(1));
    }

    @Test
    void getById_failure() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        List<ImplicationPreference> list = Arrays.asList(new ImplicationPreference(), new ImplicationPreference());
        when(repo.findAll()).thenReturn(list);
        assertEquals(2, service.getAll().size());
    }

    @Test
    void update_success() {
        ActivityIdea initial = new ActivityIdea();
        initial.setId(1);
        ActivityIdea implied = new ActivityIdea();
        implied.setId(2);

        ImplicationPreference existing = new ImplicationPreference();
        existing.setId(10);

        ImplicationPreferenceDTO dto = new ImplicationPreferenceDTO(1, 2);

        when(actRepo.findById(1)).thenReturn(Optional.of(initial));
        when(actRepo.findById(2)).thenReturn(Optional.of(implied));
        when(repo.findById(10)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        ImplicationPreference result = service.update(10, dto);
        assertEquals(initial, result.getActivityIdeaInitial());
        assertEquals(implied, result.getActivityIdeaImplied());
    }

    @Test
    void update_failure_invalidIdea() {
        ImplicationPreferenceDTO dto = new ImplicationPreferenceDTO(1, 2);
        when(actRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(5);
        verify(repo, times(1)).deleteById(5);
    }
}