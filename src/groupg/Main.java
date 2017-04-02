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
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
//        HospitalData h = new HospitalData();
//        JavaDBExample dbExample = new JavaDBExample();
//        dbExample.connectDB();
//        dbExample.createTables();
//        dbExample.insertTables();
        
        launch(args);
    }
}
