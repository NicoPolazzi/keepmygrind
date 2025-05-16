package io.github.nicopolazzi.keepmygrind.repository;

import java.util.List;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;

public interface GrindProfileRepository {

    List<GrindProfile> findAll();

}
