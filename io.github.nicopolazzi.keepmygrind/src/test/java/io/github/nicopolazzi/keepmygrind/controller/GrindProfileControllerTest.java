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
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

@ExtendWith(MockitoExtension.class)
class GrindProfileControllerTest {

    private static final String GRINDPROFILE_FIXTURE_ID = "1";
    private static final String GRINDPROFILE_FIXTURE_BREW = "V60";
    private static final int GRINDPROFILE_FIXTURE_BEANS_GRAMS = 18;
    private static final int GRINDPROFILE_FIXTURE_WATER_MILLILITERS = 250;
    private static final int GRINDPROFILE_FIXTURE_CLICKS = 60;

    @Mock
    private GrindProfileRepository grindProfileRepository;

    @Mock
    private GrindProfileView grindProfileView;

    @Mock
    private CoffeeRepository coffeeRepository;

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
        var coffee = new Coffee("1", "test", "test");
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_ID, coffee, GRINDPROFILE_FIXTURE_BREW,
                GRINDPROFILE_FIXTURE_BEANS_GRAMS, GRINDPROFILE_FIXTURE_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_CLICKS);
        when(grindProfileRepository.findById(GRINDPROFILE_FIXTURE_ID)).thenReturn(Optional.empty());
        when(coffeeRepository.findById("1")).thenReturn(Optional.of(coffee));
        grindProfileController.newGrindProfile(profile);
        InOrder inOrder = inOrder(grindProfileRepository, grindProfileView);
        inOrder.verify(grindProfileRepository).save(profile);
        inOrder.verify(grindProfileView).grindProfileAdded(profile);
    }

    @Test
    void testNewGrindProfileWhenGrindProfileAndCoffeeAlreadyExist() {
        var coffee = new Coffee("1", "test", "test");
        var existingProfile = new GrindProfile(GRINDPROFILE_FIXTURE_ID, coffee, GRINDPROFILE_FIXTURE_BREW,
                GRINDPROFILE_FIXTURE_BEANS_GRAMS, GRINDPROFILE_FIXTURE_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_CLICKS);
        var profileToAdd = new GrindProfile(GRINDPROFILE_FIXTURE_ID, coffee, "espresso", 10, 100, 30);
        when(grindProfileRepository.findById(GRINDPROFILE_FIXTURE_ID)).thenReturn(Optional.of(existingProfile));
        when(coffeeRepository.findById("1")).thenReturn(Optional.of(coffee));
        grindProfileController.newGrindProfile(profileToAdd);
        verify(grindProfileView).showExistingGrindProfileError(existingProfile);
        verifyNoMoreInteractions(ignoreStubs(grindProfileRepository, coffeeRepository));
    }

    @Test
    void testNewGrindProfileWhenCoffeeDoesntAlreadyExist() {
        var coffee = new Coffee("1", "test", "test");
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_ID, coffee, GRINDPROFILE_FIXTURE_BREW,
                GRINDPROFILE_FIXTURE_BEANS_GRAMS, GRINDPROFILE_FIXTURE_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_CLICKS);
        when(coffeeRepository.findById("1")).thenReturn(Optional.empty());
        grindProfileController.newGrindProfile(profile);
        verify(grindProfileView).showCoffeeNotFoundError("1");
        verifyNoMoreInteractions(ignoreStubs(coffeeRepository));
    }

}
