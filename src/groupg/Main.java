package groupg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Ryan Benasutti
 * @since 2017-03-23
 */
public class Main extends Application
{
    private static HospitalData h = new HospitalData();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> HospitalData.publishDB());
    }

    public static void main(String[] args)
    {
        JavaDBExample dbExample = new JavaDBExample();
        dbExample.connectDB();
//        dbExample.createTables();
//        dbExample.insertTables();
        HospitalData.publishDB();

        launch(args);
    }
}
