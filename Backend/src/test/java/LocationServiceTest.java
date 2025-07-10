package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.LocationDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Location;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.LocationRepository;
import org.example.RSC.Service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepo;

    @Mock
    private ActivityIdeaRepository activityRepo;

    @InjectMocks
    private LocationService service;

    private LocationDTO dto;
    private Location location;
    private ActivityIdea activity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new LocationDTO("Park", 12.5f, 34.7f);
        location = Location.builder()
                .id(1)
                .name("Park")
                .x(12.5f)
                .y(34.7f)
                .activityIdeas(new ArrayList<>())
                .build();

        activity = ActivityIdea.builder()
                .id(2)
                .name("Jogging")
                .locations(new ArrayList<>())
                .build();
    }

    @Test
    void findAll_success() {
        when(locationRepo.findAll()).thenReturn(List.of(location));
        List<Location> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Park", result.get(0).getName());
    }

    @Test
    void findAll_empty() {
        when(locationRepo.findAll()).thenReturn(List.of());
        List<Location> result = service.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_success() {
        when(locationRepo.findById(1)).thenReturn(Optional.of(location));

        Location result = service.findById(1);
        assertEquals("Park", result.getName());
    }

    @Test
    void findById_notFound_throws() {
        when(locationRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(999));
    }

    @Test
    void save_success() {
        when(locationRepo.save(any())).thenReturn(location);

        Location saved = service.save(dto);
        assertEquals("Park", saved.getName());
    }

    @Test
    void update_success() {
        when(locationRepo.findById(1)).thenReturn(Optional.of(location));
        when(locationRepo.save(any())).thenReturn(location);

        Location updated = service.update(1, new LocationDTO("Updated Park", 10f, 20f));
        assertEquals("Updated Park", updated.getName());
    }

    @Test
    void update_notFound_throws() {
        when(locationRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.update(999, dto));
    }

    @Test
    void mapLocationToActivity_success() {
        when(locationRepo.findById(1)).thenReturn(Optional.of(location));
        when(activityRepo.findById(2)).thenReturn(Optional.of(activity));

        Location result = service.mapLocationToActivity(1, 2);

        assertTrue(result.getActivityIdeas().contains(activity));
        assertTrue(activity.getLocations().contains(location));
        verify(activityRepo, times(1)).save(activity);
    }

    @Test
    void mapLocationToActivity_locationNotFound_throws() {
        when(locationRepo.findById(1)).thenReturn(Optional.empty());
        when(activityRepo.findById(2)).thenReturn(Optional.of(activity));

        assertThrows(EntityNotFoundException.class, () ->
                service.mapLocationToActivity(1, 2));
    }

    @Test
    void mapLocationToActivity_activityNotFound_throws() {
        when(locationRepo.findById(1)).thenReturn(Optional.of(location));
        when(activityRepo.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.mapLocationToActivity(1, 2));
    }
}