package io.github.nicopolazzi.keepmygrind.repository.mongo;

import java.util.List;
import java.util.Optional;

import com.mongodb.MongoClient;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

public class GrindProfileMongoRepository implements GrindProfileRepository {
    public static final String KEEPMYGRIND_DB_NAME = "keepmygrind";
    public static final String GRINDPROFILE_COLLECTION_NAME = "grindprofile";

    private MongoClient client;

    public GrindProfileMongoRepository(MongoClient client) {
        this.client = client;
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
