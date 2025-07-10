package test.java;

import jakarta.persistence.EntityNotFoundException;
import org.example.RSC.DTO.ActivityIdeaDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.UserEntity;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.UserRepository;
import org.example.RSC.Service.ActivityIdeaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActivityIdeaServiceTest {

    @Mock
    private ActivityIdeaRepository repository;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private ActivityIdeaService service;

    private UserEntity user;
    private ActivityIdea idea;
    private ActivityIdeaDTO dto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1);
        user.setUsername("john");

        idea = ActivityIdea.builder()
                .id(10)
                .name("Run")
                .durimin(30)
                .durimax(60)
                .user(user)
                .build();

        dto = new ActivityIdeaDTO("Run", 30, 60, 0, 0, 0, 0, "john");
    }

    @Test
    void create_userNotFound_throws() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void findAllForUsername_success() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(repository.findByUser_Id(1)).thenReturn(List.of(idea));

        List<ActivityIdea> result = service.findAllForUsername("john");

        assertEquals(1, result.size());
        assertEquals("Run", result.get(0).getName());
    }

    @Test
    void findAllForUsername_userNotFound_throws() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findAllForUsername("john"));
    }

    @Test
    void findAll_success() {
        when(repository.findAll()).thenReturn(List.of(idea));

        List<ActivityIdea> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_empty() {
        when(repository.findAll()).thenReturn(List.of());

        List<ActivityIdea> result = service.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_success() {
        when(repository.findById(10)).thenReturn(Optional.of(idea));

        ActivityIdea result = service.findById(10);

        assertEquals(10, result.getId());
    }

    @Test
    void findById_notFound_throws() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(99));
    }

    @Test
    void update_userNotFound_throws() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(10, dto));
    }

    @Test
    void delete_success() {
        service.delete(10);
        verify(repository, times(1)).deleteById(10);
    }

    @Test
    void delete_verifyCall() {
        doNothing().when(repository).deleteById(10);
        service.delete(10);
        verify(repository).deleteById(10);
    }

    @Test
    void getIdeasForUser_success() {
        when(repository.findByUser_Id(1)).thenReturn(List.of(idea));

        List<ActivityIdea> result = service.getIdeasForUser(1);

        assertEquals(1, result.size());
    }

    @Test
    void getIdeasForUser_emptyList() {
        when(repository.findByUser_Id(1)).thenReturn(List.of());

        List<ActivityIdea> result = service.getIdeasForUser(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void getFullActivity_success() {
        when(repository.findDetailedById(10)).thenReturn(Optional.of(idea));

        ActivityIdea result = service.getFullActivity(10);

        assertEquals(10, result.getId());
    }

    @Test
    void getFullActivity_notFound_throws() {
        when(repository.findDetailedById(10)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getFullActivity(10));
    }
}