package test.java;

import org.example.RSC.Entity.UserEntity;
import org.example.RSC.Repository.UserRepository;
import org.example.RSC.Service.UserEntityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class UserEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEntityService userEntityService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("securepassword");
    }


    @Test
    public void loadUserByUsername_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        var userDetails = userEntityService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("securepassword", userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userEntityService.loadUserByUsername("unknown")
        );
    }

    @Test
    public void getByUsername_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        var user = userEntityService.getByUsername("testuser");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void getByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userEntityService.getByUsername("ghost")
        );
    }

    @Test
    public void existsByUsername_returnsTrue() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertTrue(userEntityService.existsByUsername("testuser"));
    }

    @Test
    public void existsByUsername_returnsFalse() {
        when(userRepository.existsByUsername("ghost")).thenReturn(false);

        assertFalse(userEntityService.existsByUsername("ghost"));
    }

    @Test
    public void save_user_success() {
        userEntityService.save(testUser);
        verify(userRepository, org.mockito.Mockito.times(1)).save(testUser);;
    }

}