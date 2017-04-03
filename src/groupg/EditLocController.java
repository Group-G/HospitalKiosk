package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-04-02
 */
public class EditLocController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<Location> locList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        locList.getItems().addAll(HospitalData.getAllLocations()); //Add all locations from DB to listview
    }

    public void onCancel(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/adminMain.fxml", "Admin Main", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editLocAdd.fxml", "Add New Location", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
//        HospitalData.getAllLocations().remove(locList.getSelectionModel().getSelectedItem());
        locList.getItems().remove(locList.getSelectionModel().getSelectedIndex()); //TODO: Remove loc from DB
    }
}
