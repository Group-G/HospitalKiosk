package groupg.controller;

import static groupg.Main.h;
import groupg.database.Location;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-04-09
 */
public class EditIFCController implements Initializable {
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<Location> ifcList;
    @FXML
    private AnchorPane time;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Location> potential = new ArrayList<>();
        potential.addAll(h.getLocationsByCategory("Elevator"));
        potential.addAll(h.getLocationsByCategory("Stairs"));
        ifcList.getItems().addAll(potential);
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (!isNowMoving) {
                //System.out.println("Mouse stopped!");
            }
        });
        pause.setOnFinished(e -> onLogout());
        time.setOnMouseClicked(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseDragged(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseMoved(e -> {
            mouseMoving.set(true);
            pause.playFromStart();
            // System.out.println("Mouse move!");
        });
    }
    public void onCancel(ActionEvent event) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminMain.fxml", "Admin Main", cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAdd(ActionEvent event) {
        try {
            ResourceManager.getInstance().<EditIFCAddController>loadFXMLIntoScene("/view/editIFCAdd.fxml", "Add Connections", newBtn.getScene(), (controller) -> {
                if (ifcList.getSelectionModel().getSelectedItem() != null) {
                    controller.setInputLocation(ifcList.getSelectionModel().getSelectedItem());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onLogout() {
        mouseMoving.set(false);
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml","Welcome",cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
