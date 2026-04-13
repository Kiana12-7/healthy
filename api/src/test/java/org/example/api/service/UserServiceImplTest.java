package org.example.api.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.api.dto.CurrentUserDTO;
import org.example.api.dto.LoggedInUserDTO;
import org.example.api.entity.User;
import org.example.api.repository.UserRepository;
import org.example.api.repository.WorkoutDurationStatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WorkoutDurationStatRepository workoutDurationStatRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, workoutDurationStatRepository);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registerCreatesUserAndLogsIn() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(request.getSession(true)).thenReturn(session);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(11L);
            return user;
        });

        LoggedInUserDTO result = userService.register("alice", "123456", request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("alice", savedUser.getUsername());
        assertEquals("alice", savedUser.getName());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals("11", result.getUserId());
        assertEquals("alice", result.getDisplayName());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(session).setAttribute(any(String.class), any());
    }

    @Test
    void registerRejectsDuplicateUsername() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("alice");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existingUser));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.register("alice", "123456", request)
        );

        assertEquals(409, exception.getStatusCode().value());
    }

    @Test
    void getCurrentLoginUserReturnsProfileData() {
        User user = new User();
        user.setId(8L);
        user.setUsername("alice");
        user.setName("Alice");
        user.setPassword("encoded-password");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);
        when(request.getSession(true)).thenReturn(session);
        when(workoutDurationStatRepository.countByUserAndTotalDurationSecondsGreaterThan(user, 0)).thenReturn(6L);

        userService.login("alice", "123456", request);
        CurrentUserDTO currentUser = userService.getCurrentLoginUser();

        assertEquals(8L, currentUser.getId());
        assertEquals("Alice", currentUser.getName());
        assertEquals(6, currentUser.getDays());
        assertEquals(0, currentUser.getCalories());
        assertEquals(0, currentUser.getCourses());
    }

    @Test
    void logoutInvalidatesSessionAndClearsSecurityContext() {
        when(request.getSession(false)).thenReturn(session);

        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        "alice",
                        null
                )
        );

        userService.logout(request);

        verify(session).invalidate();
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
    }
}
