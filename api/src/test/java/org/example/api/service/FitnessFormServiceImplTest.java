package org.example.api.service;

import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.repository.FitnessFormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FitnessFormServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private FitnessFormRepository fitnessFormRepository;

    private FitnessFormServiceImpl fitnessFormService;

    @BeforeEach
    void setUp() {
        fitnessFormService = new FitnessFormServiceImpl(userService, fitnessFormRepository);
    }

    @Test
    void saveCreatesFitnessFormForUserWhenMissing() {
        User user = new User();
        user.setId(1L);

        FitnessForm savedForm = new FitnessForm();
        savedForm.setId(2L);
        savedForm.setUser(user);
        savedForm.setDescription("new description");

        when(fitnessFormRepository.findByUser(user)).thenReturn(Optional.empty());
        when(fitnessFormRepository.save(any(FitnessForm.class))).thenReturn(savedForm);

        FitnessForm result = fitnessFormService.save(user, "new description");

        assertEquals("new description", result.getDescription());
        verify(fitnessFormRepository).save(any(FitnessForm.class));
    }

    @Test
    void saveUpdatesExistingFitnessFormDescription() {
        User user = new User();
        user.setId(1L);

        FitnessForm existingForm = new FitnessForm();
        existingForm.setId(2L);
        existingForm.setUser(user);
        existingForm.setDescription("old description");

        when(fitnessFormRepository.findByUser(user)).thenReturn(Optional.of(existingForm));
        when(fitnessFormRepository.save(existingForm)).thenReturn(existingForm);

        FitnessForm result = fitnessFormService.save(user, "new description");

        assertEquals("new description", result.getDescription());
        verify(fitnessFormRepository, never()).save(any(FitnessForm.class));
        verify(fitnessFormRepository).save(existingForm);
    }
}
