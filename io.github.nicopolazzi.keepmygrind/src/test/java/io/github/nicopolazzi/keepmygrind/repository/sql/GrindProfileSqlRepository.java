package io.github.nicopolazzi.keepmygrind.repository.sql;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

public class GrindProfileSqlRepository implements GrindProfileRepository {

    private Session session;

    public GrindProfileSqlRepository(Session session) {
        this.session = session;
    }

    @Override
    public List<GrindProfile> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<GrindProfile> findById(String id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void save(GrindProfile profile) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

}
