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
public class EditIFCController implements Initializable {
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<Location> ifcList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Location> potential = new ArrayList<>();
        potential.addAll(HospitalData.getLocationsByCategory("Elevator"));
        potential.addAll(HospitalData.getLocationsByCategory("Stairs"));
        ifcList.getItems().addAll(potential);
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
}
