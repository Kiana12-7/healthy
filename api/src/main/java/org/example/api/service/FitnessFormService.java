package org.example.api.service;

import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;

public interface FitnessFormService {
    FitnessForm save(String description);

    FitnessForm save(User user, String description);
}
