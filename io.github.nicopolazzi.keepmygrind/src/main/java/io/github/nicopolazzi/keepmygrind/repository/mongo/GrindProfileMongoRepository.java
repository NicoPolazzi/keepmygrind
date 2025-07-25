package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

public class GrindProfileMongoRepository implements GrindProfileRepository {

    public static final String KEEPMYGRIND_DB_NAME = "keepmygrind";
    public static final String GRINDPROFILE_COLLECTION_NAME = "grindprofile";

    private MongoCollection<GrindProfile> grindProfileCollection;

    public GrindProfileMongoRepository(MongoClient client) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        grindProfileCollection = client.getDatabase(KEEPMYGRIND_DB_NAME).withCodecRegistry(pojoCodecRegistry)
                .getCollection(GRINDPROFILE_COLLECTION_NAME, GrindProfile.class);
    }

    @Override
    public List<GrindProfile> findAll() {
        return grindProfileCollection.find().into(new ArrayList<>());
    }

    @Override
    public Optional<GrindProfile> findById(String id) {
        return Optional.ofNullable(grindProfileCollection.find(eq("_id", id)).first());
    }

    @Override
    public void save(GrindProfile profile) {
        grindProfileCollection.insertOne(profile);
    }

    @Override
    public void delete(String id) {
        grindProfileCollection.deleteOne(eq("_id", id));
    }

}
