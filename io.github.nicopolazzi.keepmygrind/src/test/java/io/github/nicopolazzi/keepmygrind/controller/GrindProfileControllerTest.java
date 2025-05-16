package io.github.nicopolazzi.keepmygrind.controller;

import static java.util.Arrays.asList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
