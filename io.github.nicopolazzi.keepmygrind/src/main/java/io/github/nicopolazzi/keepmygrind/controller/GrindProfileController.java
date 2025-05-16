package io.github.nicopolazzi.keepmygrind.controller;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

public class GrindProfileController {
    private GrindProfileRepository grindProfileRepository;
    private GrindProfileView grindProfileView;
    private CoffeeRepository coffeeRepository;

    public GrindProfileController(GrindProfileRepository grindProfileRepository, GrindProfileView grindProfileView,
            CoffeeRepository coffeeRepository) {
        this.grindProfileRepository = grindProfileRepository;
        this.grindProfileView = grindProfileView;
        this.coffeeRepository = coffeeRepository;
    }

    public void allGrindProfiles() {
        grindProfileView.showAllGrindProfiles(grindProfileRepository.findAll());
    }

    public void newGrindProfile(GrindProfile profile) {
        String coffeeId = profile.getCoffee().getId();
        if (coffeeRepository.findById(coffeeId).isEmpty()) {
            grindProfileView.showCoffeeNotFoundError(coffeeId);
            return;
        }

        grindProfileRepository.findById(profile.getId())
                .ifPresentOrElse(existing -> grindProfileView.showExistingGrindProfileError(existing), () -> {
                    grindProfileRepository.save(profile);
                    grindProfileView.grindProfileAdded(profile);
                });
    }

}
