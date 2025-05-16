package io.github.nicopolazzi.keepmygrind.controller;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

@ExtendWith(MockitoExtension.class)
class GrindProfileControllerTest {

    @Mock
    private GrindProfileRepository grindProfileRepository;

    @Mock
    private GrindProfileView grindProfileView;

    @InjectMocks
    private GrindProfileController grindProfileController;

    @Test
    void testAllGrindProfiles() {
        List<GrindProfile> profiles = asList(new GrindProfile());
        when(grindProfileRepository.findAll()).thenReturn(profiles);
        grindProfileController.allGrindProfiles();
        verify(grindProfileView).showAllGrindProfiles(profiles);
    }

    @Test
    void testNewGrindProfileWhenCoffeetAlreadyExistsAndGrindProfileDoesnt() {
        var profile = new GrindProfile("1", new Coffee(), "V60", 18, 250, 60);
        when(grindProfileRepository.findById("1")).thenReturn(Optional.empty());
        grindProfileController.newGrindProfile(profile);
        InOrder inOrder = inOrder(grindProfileRepository, grindProfileView);
        inOrder.verify(grindProfileRepository).save(profile);
        inOrder.verify(grindProfileView).grindProfileAdded(profile);
    }

    @Test
    void testNewGrindProfileWhenGrindProfiletAlreadyExists() {
        var existingProfile = new GrindProfile("1", new Coffee(), "V60", 18, 250, 60);
        var profileToAdd = new GrindProfile("1", new Coffee(), "espresso", 18, 250, 60);
        when(grindProfileRepository.findById("1")).thenReturn(Optional.of(existingProfile));
        grindProfileController.newGrindProfile(profileToAdd);
        verify(grindProfileView).showExistingGrindProfileError(existingProfile);
        verifyNoMoreInteractions(ignoreStubs(grindProfileRepository));
    }

}
