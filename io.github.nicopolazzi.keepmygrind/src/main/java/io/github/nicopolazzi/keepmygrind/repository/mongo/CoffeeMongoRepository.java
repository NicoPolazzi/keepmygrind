package io.github.nicopolazzi.keepmygrind.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

public class CoffeeMongoRepository implements CoffeeRepository {

    public static final String KEEPMYGRIND_DB_NAME = "keepmygrind";
    public static final String COFFEE_COLLECTION_NAME = "coffee";
    private MongoCollection<Document> coffeeCollection;

    public CoffeeMongoRepository(MongoClient client) {
        coffeeCollection = client.getDatabase(KEEPMYGRIND_DB_NAME).getCollection(COFFEE_COLLECTION_NAME);
    }

    @Override
    public List<Coffee> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Coffee> findById(String string) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void save(Coffee coffee) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Coffee coffee) {
        // TODO Auto-generated method stub

    }

}
