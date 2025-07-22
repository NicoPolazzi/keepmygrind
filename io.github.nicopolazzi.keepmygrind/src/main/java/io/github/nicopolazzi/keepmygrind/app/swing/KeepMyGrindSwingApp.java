package io.github.nicopolazzi.keepmygrind.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.hibernate.tool.schema.Action;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.factory.MongoRepositoryFactory;
import io.github.nicopolazzi.keepmygrind.factory.RepositoryFactory;
import io.github.nicopolazzi.keepmygrind.factory.SqlRepositoryFactory;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.view.swing.CoffeeSwingView;
import io.github.nicopolazzi.keepmygrind.view.swing.GrindProfileSwingView;
import io.github.nicopolazzi.keepmygrind.view.swing.KeepMyGrindSwingView;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class KeepMyGrindSwingApp implements Callable<Void> {

    @Option(names = { "--database" }, description = "Database name")
    private String databaseName = "mongo";

    @Option(names = { "--host" }, description = "Database host address")
    private String host = "localhost";

    @Option(names = { "--port" }, description = "Database host port")
    private int port = 27017;

    public static void main(String[] args) {
        new picocli.CommandLine(new KeepMyGrindSwingApp()).execute(args);
    }

    @Override
    public Void call() throws Exception {
        EventQueue.invokeLater(() -> {
            try {
                RepositoryFactory factory;
                CoffeeRepository coffeeRepository;
                GrindProfileRepository grindProfileRepository;

                if (databaseName.equals("mysql")) {
                    SessionFactory sessionFactory = new Configuration().addAnnotatedClass(GrindProfile.class)
                            .addAnnotatedClass(Coffee.class)
                            .setProperty(JdbcSettings.JAKARTA_JDBC_URL,
                                    String.format("jdbc:mysql://%s:%d/keepmygrind", host, port))
                            .setProperty(JdbcSettings.JAKARTA_JDBC_USER, "root")
                            .setProperty(JdbcSettings.JAKARTA_JDBC_PASSWORD, "")
                            .setProperty(SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.ACTION_UPDATE)
                            .buildSessionFactory();
                    factory = new SqlRepositoryFactory(sessionFactory);
                    coffeeRepository = factory.makeCoffeeRepository();
                    grindProfileRepository = factory.makeGrindProfileRepository();
                } else {
                    MongoClient mongoClient = new MongoClient(new ServerAddress(host, port));
                    factory = new MongoRepositoryFactory(mongoClient);
                    coffeeRepository = factory.makeCoffeeRepository();
                    grindProfileRepository = factory.makeGrindProfileRepository();
                }

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
