package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.OrderingConstraintDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Constraint.OrderingConstraint;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.Constraint.OrderingConstraintRepository;
import org.example.RSC.Service.Constraint.OrderingConstraintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderingConstraintServiceTest {

    @Mock
    private OrderingConstraintRepository constraintRepo;

    @Mock
    private ActivityIdeaRepository activityRepo;

    @InjectMocks
    private OrderingConstraintService service;

    private ActivityIdea idea1;
    private ActivityIdea idea2;
    private OrderingConstraint constraint;
    private OrderingConstraintDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea1 = ActivityIdea.builder().id(1).name("Morning Run").build();
        idea2 = ActivityIdea.builder().id(2).name("Breakfast").build();

        constraint = OrderingConstraint.builder()
                .id(1)
                .activityIdeaBefore(idea1)
                .activityIdeaAfter(idea2)
                .build();

        dto = new OrderingConstraintDTO(1, 1, 2);
    }

    @Test
    void create_success() {
        when(activityRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(activityRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(constraintRepo.save(any())).thenReturn(constraint);

        OrderingConstraint result = service.create(dto);

        assertEquals(idea1, result.getActivityIdeaBefore());
        assertEquals(idea2, result.getActivityIdeaAfter());
    }

    @Test
    void create_activityIdea1NotFound_throws() {
        when(activityRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void create_activityIdea2NotFound_throws() {
        when(activityRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(activityRepo.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void getById_success() {
        when(constraintRepo.findById(1)).thenReturn(Optional.of(constraint));

        OrderingConstraint result = service.getById(1);
        assertEquals(idea1, result.getActivityIdeaBefore());
    }

    @Test
    void getById_notFound_throws() {
        when(constraintRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1));
    }

    @Test
    void getAll_success() {
        when(constraintRepo.findAll()).thenReturn(List.of(constraint));

        List<OrderingConstraint> result = service.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void update_success() {
        when(activityRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(activityRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(constraintRepo.findById(1)).thenReturn(Optional.of(constraint));
        when(constraintRepo.save(any())).thenReturn(constraint);

        OrderingConstraint result = service.update(1, dto);
        assertEquals(idea1, result.getActivityIdeaBefore());
        assertEquals(idea2, result.getActivityIdeaAfter());
    }

    @Test
    void update_activityIdea1NotFound_throws() {
        when(activityRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));
    }

    @Test
    void update_constraintNotFound_throws() {
        when(activityRepo.findById(1)).thenReturn(Optional.of(idea1));
        when(activityRepo.findById(2)).thenReturn(Optional.of(idea2));
        when(constraintRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(1, dto));
    }

    @Test
    void delete_success() {
        service.delete(1);
        verify(constraintRepo, times(1)).deleteById(1);
    }
}
