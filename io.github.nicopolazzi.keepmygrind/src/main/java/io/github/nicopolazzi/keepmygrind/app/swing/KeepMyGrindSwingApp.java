package io.github.nicopolazzi.keepmygrind.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository;
import io.github.nicopolazzi.keepmygrind.view.swing.CoffeeSwingView;
import io.github.nicopolazzi.keepmygrind.view.swing.GrindProfileSwingView;
import io.github.nicopolazzi.keepmygrind.view.swing.KeepMyGrindSwingView;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class KeepMyGrindSwingApp implements Callable<Void> {

    @Option(names = { "--mongo-host" }, description = "MongoDB host address")
    private String mongoHost = "localhost";

    @Option(names = { "--mongo-port" }, description = "MongoDB host port")
    private int mongoPort = 27017;

    public static void main(String[] args) {
        new picocli.CommandLine(new KeepMyGrindSwingApp()).execute(args);
    }

    @Override
    public Void call() throws Exception {
        EventQueue.invokeLater(() -> {
            try {
                MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
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

        return null;
    }
}
