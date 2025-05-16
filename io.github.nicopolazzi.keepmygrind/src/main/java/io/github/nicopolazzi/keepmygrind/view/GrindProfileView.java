package io.github.nicopolazzi.keepmygrind.view;

import java.util.List;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;

public interface GrindProfileView {

    void showAllGrindProfiles(List<GrindProfile> profiles);

    void grindProfileAdded(GrindProfile profile);

    void grindProfileDeleted(GrindProfile profile);

    void showExistingGrindProfileError(GrindProfile existingProfile);

    void showCoffeeNotFoundError(String coffeeId);

    void showNotExistingGrindProfileError(GrindProfile profile);

}
