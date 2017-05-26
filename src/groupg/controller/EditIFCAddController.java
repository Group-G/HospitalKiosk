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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
public class EditIFCAddController implements Initializable {
    @FXML
    private Button addBtn, remBtn, cancelBtn;
    @FXML
    private ListView<Location> availConList, curConList;
    @FXML
    private VBox titleVBox;
    @FXML
    private AnchorPane time;
    private Location location;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void onAdd(ActionEvent event) {
        List<Location> toAdd = availConList.getSelectionModel().getSelectedItems();
        if (toAdd != null && toAdd.size() > 0) {
            location.addNeighbors(toAdd);
            toAdd.forEach(elem -> elem.getNeighbors().add(location));
            curConList.getItems().addAll(toAdd);
            availConList.getItems().removeAll(toAdd);
        }
    }

    public void onRem(ActionEvent event) {
        List<Location> toRem = curConList.getSelectionModel().getSelectedItems();
        if (toRem != null && toRem.size() > 0) {
            location.removeNeighbors(toRem);
            toRem.forEach(elem -> elem.getNeighbors().remove(location));
            availConList.getItems().addAll(toRem);
            curConList.getItems().removeAll(toRem);
        }
    }

    public void onCancel(ActionEvent event) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editIFC.fxml", "Edit Inter-Floor Connections", cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setInputLocation(Location loc) {
        location = loc;
        titleVBox.getChildren().addAll(new Text(loc.getName()));
        curConList.getItems().setAll(loc.getNeighbors());
        List<Location> potential = new ArrayList<>();
        potential.addAll(h.getLocationsByCategory("Elevator"));
        potential.addAll(h.getLocationsByCategory("Stairs"));
        potential.removeAll(loc.getNeighbors());
        potential.remove(loc);
        availConList.getItems().setAll(potential);
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
