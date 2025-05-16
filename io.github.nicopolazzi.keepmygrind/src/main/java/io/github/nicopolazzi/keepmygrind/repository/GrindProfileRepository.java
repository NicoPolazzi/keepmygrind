package io.github.nicopolazzi.keepmygrind.repository;

import java.util.List;
import java.util.Optional;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;

public interface GrindProfileRepository {

    List<GrindProfile> findAll();

    Optional<GrindProfile> findById(String id);

    void save(GrindProfile profile);

}
