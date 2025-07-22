package io.github.nicopolazzi.keepmygrind.factory;

import com.mongodb.MongoClient;

import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository;

public class MongoRepositoryFactory implements RepositoryFactory {

    private MongoClient mongoClient;

    public MongoRepositoryFactory(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public CoffeeRepository makeCoffeeRepository() {
        return new CoffeeMongoRepository(mongoClient);
    }

    @Override
    public GrindProfileRepository makeGrindProfileRepository() {
        return new GrindProfileMongoRepository(mongoClient);
    }

}
