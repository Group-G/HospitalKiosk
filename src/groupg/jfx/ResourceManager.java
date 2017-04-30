package groupg.jfx;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * Created by ryan on 3/31/17.
 */
public class ResourceManager {
    private static ResourceManager ourInstance = new ResourceManager();
    private static ConcurrentMap<String, Image> images;

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    private ResourceManager() {
        images = new ConcurrentHashMap<>(31);
        List<String> imageNames = new ArrayList<>(31);

        imageNames.add("/image/FaulknerMaps/Belkin1.png");
        imageNames.add("/image/FaulknerMaps/Belkin2.png");
        imageNames.add("/image/FaulknerMaps/Belkin3.png");
        imageNames.add("/image/FaulknerMaps/Belkin4.png");
        imageNames.add("/image/FaulknerMaps/BelkinR.png");
        imageNames.add("/image/FaulknerMaps/Faulkner1.png");
        imageNames.add("/image/FaulknerMaps/Faulkner2.png");
        imageNames.add("/image/FaulknerMaps/Faulkner3.png");
        imageNames.add("/image/FaulknerMaps/Faulkner4.png");
        imageNames.add("/image/FaulknerMaps/Faulkner5.png");
        imageNames.add("/image/FaulknerMaps/Faulkner6.png");
        imageNames.add("/image/FaulknerMaps/Faulkner7.png");
        imageNames.add("/image/FaulknerMaps/FaulknerR.png");
        imageNames.add("/image/FaulknerMaps/Ground.png");

        imageNames.add("/image/Headshots/Alazar.png");
        imageNames.add("/image/Headshots/Andrew.png");
        imageNames.add("/image/Headshots/Daniel.png");
        imageNames.add("/image/Headshots/Dylan.png");
        imageNames.add("/image/Headshots/Hunter.png");
        imageNames.add("/image/Headshots/Ryan.png");
        imageNames.add("/image/Headshots/Sam.png");
        imageNames.add("/image/Headshots/Saul.png");
        imageNames.add("/image/Headshots/Will.png");
        imageNames.add("/image/Headshots/Wong.png");

        imageNames.add("/image/Icons/admin.png");
        imageNames.add("/image/Icons/america.png");
        imageNames.add("/image/Icons/china.png");
        imageNames.add("/image/Icons/close.png");
        imageNames.add("/image/Icons/info.png");
        imageNames.add("/image/Icons/location.png");
        imageNames.add("/image/Icons/menu.png");
        imageNames.add("/image/Icons/pin.png");
        imageNames.add("/image/Icons/portugal.png");
        imageNames.add("/image/Icons/search.png");
        imageNames.add("/image/Icons/swap.png");
        imageNames.add("/image/Icons/spain.png");
        imageNames.add("/image/Icons/zoom_in.png");
        imageNames.add("/image/Icons/zoom_out.png");

        new Thread(() -> imageNames.parallelStream().forEach(elem -> images.put(elem, loadImage(elem)))).start();
    }

    /**
     * Loads an FXML file into a Scene
     *
     * @param fileName Relative path for FXML file
     * @param title    Title for Scene
     * @param scene    Scene to load into
     * @throws IOException          FXMLLoader.load may fail to retrieve file
     * @throws NullPointerException FXMLLoader.load may fail to retrieve file
     */
    public <T> T loadFXMLIntoScene(String fileName, String title, Scene scene, Consumer<T> consumer) throws IOException, NullPointerException {
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
     * @throws IOException          FXMLLoader.load may fail to retrieve file
     * @throws NullPointerException FXMLLoader.load may fail to retrieve file
     */
    public <T> T loadFXMLIntoScene(String fileName, String title, Scene scene) throws IOException, NullPointerException {
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
    public void loadFXMLIntoSceneWithController(String fileName, String title, Scene scene, Initializable controller) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        loader.setController(controller);
        Platform.runLater(() ->
                          {
                              try {
                                  Stage stage = (Stage) scene.getWindow();
                                  stage.setTitle(title);
                                  stage.setScene(new Scene(loader.load(), scene.getWidth(), scene.getHeight()));
                                  stage.show();
                              } catch (IOException e) {
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
     * @throws IOException          FXMLLoader.load may fail to retrieve file
     * @throws NullPointerException FXMLLoader.load may fail to retrieve file
     */
    public <T> T loadFXMLIntoDialog(String fileName, String title, Scene scene, double width, double height) throws IOException, NullPointerException {
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

    public Image reloadImage(String fileName, int width, int height, boolean preserveRatio, boolean smooth) {
        images.put(fileName, new Image(fileName, width, height, preserveRatio, smooth));
        return images.get(fileName);
    }

    public Image loadImage(String fileName, int width, int height, boolean preserveRatio, boolean smooth) {
        if (images.containsKey(fileName))
            return images.get(fileName);

        Image image = new Image(fileName, width, height, preserveRatio, smooth);
        images.put(fileName, image);
        return image;
    }

    public Image loadImage(String fileName, int width, int height) {
        return loadImage(fileName, width, height, true, true);
    }

    public Image loadImage(String fileName) {
        return loadImage(fileName, 2265, 1290);
    }

    public Image loadImageNatural(String fileName) {
        if (images.containsKey(fileName))
            return images.get(fileName);

        Image image = new Image(fileName);
        images.put(fileName, image);
        return image;
    }
}
