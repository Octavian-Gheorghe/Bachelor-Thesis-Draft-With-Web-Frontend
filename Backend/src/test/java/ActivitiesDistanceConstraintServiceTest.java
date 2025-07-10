package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ActivitiesDistanceConstraintDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Constraint.ActivitiesDistanceConstraint;
import org.example.RSC.Enum.DistanceType;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Constraint.ActivitiesDistanceConstraintRepository;
import org.example.RSC.Service.Constraint.ActivitiesDistanceConstraintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivitiesDistanceConstraintServiceTest {

    @Mock
    private ActivitiesDistanceConstraintRepository repo;

    @Mock
    private ActivityIdeaRepository actRepo;

    @InjectMocks
    private ActivitiesDistanceConstraintService service;

    private ActivityIdea idea1;
    private ActivityIdea idea2;
    private ActivitiesDistanceConstraint constraint;
    private ActivitiesDistanceConstraintDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea1 = ActivityIdea.builder().id(1).name("A1").build();
        idea2 = ActivityIdea.builder().id(2).name("A2").build();

        dto = new ActivitiesDistanceConstraintDTO(1, 2, 10, DistanceType.MINIMUM);

        constraint = ActivitiesDistanceConstraint.builder()
                .id(1)
                .activityIdea1(idea1)
                .activityIdea2(idea2)
                .distance(10)
                .type(DistanceType.MINIMUM)
                .build();
    }

    @Test
    void create_success() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(repo.save(any())).thenReturn(constraint);

        ActivitiesDistanceConstraint result = service.create(dto);

        assertEquals(10, result.getDistance());
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
        when(repo.findById(1)).thenReturn(Optional.of(constraint));

        ActivitiesDistanceConstraint result = service.getById(1);
        assertEquals(10, result.getDistance());
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(repo.findAll()).thenReturn(List.of(constraint));

        List<ActivitiesDistanceConstraint> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void update_success() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(repo.findById(1)).thenReturn(Optional.of(constraint));
        when(repo.save(any())).thenReturn(constraint);

        ActivitiesDistanceConstraint result = service.update(1, dto);
        assertEquals(DistanceType.MINIMUM, result.getType());
    }

    @Test
    void update_activity1NotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_constraintNotFound_throws() {
        when(actRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(actRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(repo, times(1)).deleteById(1);
    }
}
