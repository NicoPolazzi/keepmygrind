package io.github.nicopolazzi.keepmygrind.controller;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

public class GrindProfileController {
    private GrindProfileRepository grindProfileRepository;
    private GrindProfileView grindProfileView;

    public GrindProfileController(GrindProfileRepository grindProfileRepository, GrindProfileView grindProfileView) {
        this.grindProfileRepository = grindProfileRepository;
        this.grindProfileView = grindProfileView;
    }

    public void allGrindProfiles() {
        grindProfileView.showAllGrindProfiles(grindProfileRepository.findAll());
    }

    public void newGrindProfile(GrindProfile profile) {
        // TODO Auto-generated method stub

    }

}
