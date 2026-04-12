package org.example.api.controller;

import org.example.api.service.FitnessFormService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fitnessForm")
public class FitnessFormController {
    private final FitnessFormService fitnessFormService;

    public FitnessFormController(FitnessFormService fitnessFormService) {
        this.fitnessFormService = fitnessFormService;
    }

    @PostMapping("/save")
    public void save(@RequestBody String description) {
         fitnessFormService.save(description);
    }
}
