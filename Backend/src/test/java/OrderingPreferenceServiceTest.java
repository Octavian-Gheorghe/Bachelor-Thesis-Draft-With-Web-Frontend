package test.java;


import org.example.RSC.DTO.OrderingPreferenceDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Preference.OrderingPreference;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Preference.OrderingPreferenceRepository;
import org.example.RSC.Service.Preference.OrderingPreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderingPreferenceServiceTest {

    @Mock
    private OrderingPreferenceRepository repo;

    @Mock
    private ActivityIdeaRepository actRepo;

    @InjectMocks
    private OrderingPreferenceService service;

    private ActivityIdea ideaBefore;
    private ActivityIdea ideaAfter;

    @BeforeEach
    void setUp() {
        ideaBefore = new ActivityIdea();
        ideaBefore.setId(1);
        ideaAfter = new ActivityIdea();
        ideaAfter.setId(2);
    }

    @Test
    void testCreate_success() {
        OrderingPreferenceDTO dto = new OrderingPreferenceDTO(1, 2);
        OrderingPreference mockSaved = OrderingPreference.builder()
                .activityIdeaBefore(ideaBefore)
                .activityIdeaAfter(ideaAfter)
                .build();

        when(actRepo.findById(1)).thenReturn(Optional.of(ideaBefore));
        when(actRepo.findById(2)).thenReturn(Optional.of(ideaAfter));
        when(repo.save(any(OrderingPreference.class))).thenReturn(mockSaved);

        OrderingPreference result = service.create(dto);

        assertNotNull(result);
        assertEquals(ideaBefore, result.getActivityIdeaBefore());
        assertEquals(ideaAfter, result.getActivityIdeaAfter());
        verify(repo).save(any(OrderingPreference.class));
    }

    @Test
    void testCreate_activityIdeaNotFound() {
        OrderingPreferenceDTO dto = new OrderingPreferenceDTO(1, 2);
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.create(dto));
    }

    @Test
    void testGetById_success() {
        OrderingPreference constraint = OrderingPreference.builder()
                .activityIdeaBefore(ideaBefore)
                .activityIdeaAfter(ideaAfter)
                .build();
        when(repo.findById(1)).thenReturn(Optional.of(constraint));

        OrderingPreference result = service.getById(1);
        assertNotNull(result);
    }

    @Test
    void testGetById_notFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of());
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void testUpdate_success() {
        OrderingPreference existing = OrderingPreference.builder()
                .activityIdeaBefore(ideaBefore)
                .activityIdeaAfter(ideaAfter)
                .build();
        OrderingPreferenceDTO dto = new OrderingPreferenceDTO(1, 2);

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(actRepo.findById(1)).thenReturn(Optional.of(ideaBefore));
        when(actRepo.findById(2)).thenReturn(Optional.of(ideaAfter));
        when(repo.save(any(OrderingPreference.class))).thenReturn(existing); // return the updated entity

        OrderingPreference updated = service.update(1, dto);

        assertNotNull(updated);
        assertEquals(ideaBefore, updated.getActivityIdeaBefore());
        assertEquals(ideaAfter, updated.getActivityIdeaAfter());
    }

    @Test
    void testDelete() {
        service.delete(1);
        verify(repo).deleteById(1);
    }
}
