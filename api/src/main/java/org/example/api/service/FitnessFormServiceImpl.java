package org.example.api.service;

import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.repository.FitnessFormRepository;
import org.example.api.repository.specs.FitnessFormSpec;
import org.springframework.stereotype.Service;

@Service
public class FitnessFormServiceImpl implements FitnessFormService{
    private final UserService userService;
    private final FitnessFormRepository fitnessFormRepository;

    public FitnessFormServiceImpl(UserService userService, FitnessFormRepository fitnessFormRepository) {
        this.userService = userService;
        this.fitnessFormRepository = fitnessFormRepository;
    }

    @Override
    public FitnessForm save(String description) {
        User user = this.userService.getCurrentLoginUserDetails();

        return this.save(user, description);
    }

    @Override
    public FitnessForm save(User user, String description) {
        FitnessForm fitnessForm = this.fitnessFormRepository.findBy(FitnessFormSpec.isUser(user)).orElseGet(() -> {
            FitnessForm instance = new FitnessForm();
            instance.setUser(user);
            instance.setDescription(description);
            return instance;
        });

        return this.fitnessFormRepository.save(fitnessForm);
    }
}
