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
        return session.createSelectionQuery("from GrindProfile", GrindProfile.class).getResultList();
    }

    @Override
    public Optional<GrindProfile> findById(String id) {
        return Optional.ofNullable(session.find(GrindProfile.class, id));
    }

    @Override
    public void save(GrindProfile profile) {
        session.persist(profile);
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

}
