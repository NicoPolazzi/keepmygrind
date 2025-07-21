package io.github.nicopolazzi.keepmygrind.app.swing;

import java.awt.EventQueue;

import com.mongodb.MongoClient;

import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository;
import io.github.nicopolazzi.keepmygrind.view.swing.CoffeeSwingView;
import io.github.nicopolazzi.keepmygrind.view.swing.GrindProfileSwingView;
import io.github.nicopolazzi.keepmygrind.view.swing.KeepMyGrindSwingView;

public class KeepMyGrindSwingApp {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MongoClient mongoClient = new MongoClient();
                CoffeeMongoRepository coffeeRepository = new CoffeeMongoRepository(mongoClient);
                GrindProfileMongoRepository grindProfileRepository = new GrindProfileMongoRepository(mongoClient);

                CoffeeSwingView coffeeView = new CoffeeSwingView();
                GrindProfileSwingView grindProfileView = new GrindProfileSwingView();

                CoffeeController coffeeController = new CoffeeController(coffeeRepository, coffeeView,
                        grindProfileView);
                GrindProfileController grindProfileController = new GrindProfileController(grindProfileRepository,
                        grindProfileView, coffeeRepository);
                KeepMyGrindSwingView keepMyGrindView = new KeepMyGrindSwingView(coffeeView, grindProfileView);

                coffeeView.setCoffeeController(coffeeController);
                grindProfileView.setGrindProfileController(grindProfileController);
                keepMyGrindView.setVisible(true);
                coffeeController.allCoffees();
                grindProfileController.allGrindProfiles();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
