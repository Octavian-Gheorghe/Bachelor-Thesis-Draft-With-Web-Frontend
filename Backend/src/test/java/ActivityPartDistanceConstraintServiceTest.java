package test.java;


import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ActivityPartDistanceConstraintDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Constraint.ActivityPartDistanceConstraint;
import org.example.RSC.Enum.DistanceType;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Constraint.ActivityPartDistanceConstraintRepository;
import org.example.RSC.Service.Constraint.ActivityPartDistanceConstraintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivityPartDistanceConstraintServiceTest {

    @Mock
    private ActivityPartDistanceConstraintRepository repo;

    @Mock
    private ActivityIdeaRepository ideaRepo;

    @InjectMocks
    private ActivityPartDistanceConstraintService service;

    private ActivityIdea idea;
    private ActivityPartDistanceConstraint constraint;
    private ActivityPartDistanceConstraintDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea = ActivityIdea.builder().id(1).name("Activity 1").build();

        constraint = ActivityPartDistanceConstraint.builder()
                .id(1)
                .activityIdea(idea)
                .distance(15)
                .type(DistanceType.MINIMUM)
                .build();

        dto = new ActivityPartDistanceConstraintDTO(1, 15, DistanceType.MINIMUM);
    }

    @Test
    void create_success() {
        when(ideaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.save(any())).thenReturn(constraint);

        ActivityPartDistanceConstraint result = service.create(dto);

        assertEquals(15, result.getDistance());
        assertEquals(DistanceType.MINIMUM, result.getType());
    }

    @Test
    void create_ideaNotFound_throws() {
        when(ideaRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(repo.findById(1)).thenReturn(Optional.of(constraint));

        ActivityPartDistanceConstraint result = service.getById(1);
        assertEquals(15, result.getDistance());
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(repo.findAll()).thenReturn(List.of(constraint));

        List<ActivityPartDistanceConstraint> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void getAll_empty() {
        when(repo.findAll()).thenReturn(List.of());

        List<ActivityPartDistanceConstraint> result = service.getAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void update_success() {
        when(ideaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.findById(1)).thenReturn(Optional.of(constraint));
        when(repo.save(any())).thenReturn(constraint);

        ActivityPartDistanceConstraint result = service.update(1, dto);
        assertEquals(15, result.getDistance());
    }

    @Test
    void update_ideaNotFound_throws() {
        when(ideaRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_constraintNotFound_throws() {
        when(ideaRepo.findById(1)).thenReturn(Optional.of(idea));
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(repo, times(1)).deleteById(1);
    }
}
