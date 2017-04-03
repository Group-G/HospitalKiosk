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
 * @since 2017-03-30
 */
public class EditPersController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, editBtn, deleteBtn;
    @FXML
    private ListView<Person> persList;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        persList.getItems().setAll(HospitalData.getAllPeople()); //Add all people from DB to listview
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
            ResourceManager.getInstance().loadFXMLIntoScene("/editPersAdd.fxml", "Add New Person", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        System.out.println("ID: " + persList.getSelectionModel().getSelectedItem().getId());
        persList.getItems().removeIf(elem -> persList.getSelectionModel().getSelectedItem().getId() == elem.getId());
        HospitalData.getAllPeople().removeIf(elem -> persList.getSelectionModel().getSelectedItem().getId() == elem.getId());
    }

    public void onEdit(ActionEvent event)
    {
        Person out = persList.getSelectionModel().getSelectedItem();
        if (out != null)
        {
            EditPersFineController controller = new EditPersFineController(persList.getSelectionModel().getSelectedItem());
            ResourceManager.getInstance().loadFXMLIntoSceneWithController("/editPersFine.fxml", "Edit Person", editBtn.getScene(), controller);
        }
    }
}
