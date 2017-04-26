package groupg;

import groupg.database.HospitalData;
import groupg.database.JavaDBExample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Ryan Benasutti
 * @since 2017-03-23
 */
public class Main extends Application
{
    private static JavaDBExample dbExample = new JavaDBExample();
    public static HospitalData h;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/welcomeScreen.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 1404, 800));
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> Main.h.publishDB());
    }

    public static void main(String[] args)
    {


        dbExample.connectDB();

        Path path = Paths.get("HospitalDatabase");
        if (!Files.exists(path))
        {
            //Dummy data
            dbExample.createTables();
            dbExample.insertTables();
            System.out.println("-------------------------------------");
            System.out.println("GENERATED NEW DATABASE");
            System.out.println("-------------------------------------");
        }

        h = new HospitalData(dbExample);
        h.pullDB();


        launch(args);
    }
}
