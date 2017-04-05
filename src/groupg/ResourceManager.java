package groupg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

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
     *
     * @param fileName Relative path for FXML file
     * @param title    Title for Scene
     * @param scene    Scene to load into
     * @throws IOException FXMLLoader.load may fail to retrieve file
     * @throws NullPointerException FXMLLoader.load may fail to retrieve file
     */
    <T> T loadFXMLIntoScene(String fileName, String title, Scene scene, Consumer<T> consumer) throws IOException, NullPointerException
    {
        FXMLLoader loader = new FXMLLoader(ResourceManager.getInstance().getClass().getResource(fileName));
//        loader.setClassLoader(T.class.getClassLoader());
        Parent root = loader.load();
        Platform.runLater(() ->
                          {
                              Stage stage = (Stage) scene.getWindow();
                              stage.setTitle(title);
                              stage.setScene(new Scene(root, scene.getWidth(), scene.getHeight()));
                              stage.show();
                          });
        consumer.accept(loader.getController());
        return loader.getController();
    }

    /**
     * Loads an FXML file into a Scene
     *
     * @param fileName Relative path for FXML file
     * @param title    Title for Scene
     * @param scene    Scene to load into
     * @throws IOException FXMLLoader.load may fail to retrieve file
     * @throws NullPointerException FXMLLoader.load may fail to retrieve file
     */
    <T> T loadFXMLIntoScene(String fileName, String title, Scene scene) throws IOException, NullPointerException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        Platform.runLater(() ->
                          {
                              Stage stage = (Stage) scene.getWindow();
                              stage.setTitle(title);
                              stage.setScene(new Scene(root, scene.getWidth(), scene.getHeight()));
                              stage.show();
                          });
        return loader.getController();
    }

    /**
     * Loads an FXML file into a Scene with a custom Controller
     *
     * @param fileName   Relative path for FXML file
     * @param title      Title for Scene
     * @param scene      Scene to load into
     * @param controller Controller to use
     */
    void loadFXMLIntoSceneWithController(String fileName, String title, Scene scene, Initializable controller)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        loader.setController(controller);
        Platform.runLater(() ->
                          {
                              try
                              {
                                  Stage stage = (Stage) scene.getWindow();
                                  stage.setTitle(title);
                                  stage.setScene(new Scene(loader.load(), scene.getWidth(), scene.getHeight()));
                                  stage.show();
                              }
                              catch (IOException e)
                              {
                                  e.printStackTrace();
                              }
                          });
    }

    /**
     * Loads an FXML file into a Dialog
     *
     * @param fileName Relative path for FXML file
     * @param title    Title for Dialog
     * @param scene    Scene to load into
     * @param width    Width of Dialog
     * @param height   Height of Dialog
     * @throws IOException FXMLLoader.load may fail to retrieve file
     * @throws NullPointerException FXMLLoader.load may fail to retrieve file
     */
    <T> T loadFXMLIntoDialog(String fileName, String title, Scene scene, double width, double height) throws IOException, NullPointerException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        Platform.runLater(() -> {
            Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(scene.getWindow());
            dialog.setScene(new Scene(root, width, height));
            dialog.show();
        });
        return loader.getController();
    }
}
