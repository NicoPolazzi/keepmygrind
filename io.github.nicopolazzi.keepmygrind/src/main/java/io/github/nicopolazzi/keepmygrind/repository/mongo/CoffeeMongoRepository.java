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

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

public class CoffeeMongoRepository implements CoffeeRepository {

    public static final String KEEPMYGRIND_DB_NAME = "keepmygrind";
    public static final String COFFEE_COLLECTION_NAME = "coffee";
    private MongoCollection<Coffee> coffeeCollection;

    public CoffeeMongoRepository(MongoClient client) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        coffeeCollection = client.getDatabase(KEEPMYGRIND_DB_NAME).withCodecRegistry(pojoCodecRegistry)
                .getCollection(COFFEE_COLLECTION_NAME, Coffee.class);
    }

    @Override
    public List<Coffee> findAll() {
        return coffeeCollection.find().into(new ArrayList<>());
    }

    @Override
    public Optional<Coffee> findById(String id) {
        return Optional.ofNullable(coffeeCollection.find(eq("_id", id)).first());
    }

    @Override
    public void save(Coffee coffee) {
        coffeeCollection.insertOne(coffee);
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

}
