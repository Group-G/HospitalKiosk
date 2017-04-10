package groupg.controller;

import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.jfx.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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
    private Location location;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void onAdd(ActionEvent event) {
        List<Location> toAdd = availConList.getSelectionModel().getSelectedItems();
        if (toAdd != null && toAdd.size() > 0) {
            location.addNeighbors(toAdd);
            curConList.getItems().addAll(toAdd);
            availConList.getItems().removeAll(toAdd);
        }
    }

    public void onRem(ActionEvent event) {
        List<Location> toRem = availConList.getSelectionModel().getSelectedItems();
        if (toRem != null && toRem.size() > 0) {
            location.removeNeighbors(toRem);
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
        curConList.getItems().addAll(loc.getNeighbors());
        List<Location> potential = new ArrayList<>();
        potential.addAll(HospitalData.getLocationsByCategory("Elevator"));
        potential.addAll(HospitalData.getLocationsByCategory("Stairs"));
        availConList.getItems().addAll(potential);
    }
}
