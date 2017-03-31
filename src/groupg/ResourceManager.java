package groupg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by ryan on 3/31/17.
 */
class ResourceManager
{
    private static ResourceManager ourInstance = new ResourceManager();

    static ResourceManager getInstance()
    {
        return ourInstance;
    }

    private ResourceManager()
    {
    }

    /**
     * Loads an FXML file into a Scene
     * @param fileName Relative path for FXML file
     * @param title    Title for Scene
     * @param scene    Scene to load into
     * @throws IOException FXMLLoader.load may fail to retrieve file
     */
    void loadFXMLIntoScene(String fileName, String title, Scene scene) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        Platform.runLater(() ->
                          {
                              Stage stage = (Stage) scene.getWindow();
                              stage.setTitle(title);
                              stage.setScene(new Scene(root, 600, 400));
                              stage.show();
                          });
    }
}
