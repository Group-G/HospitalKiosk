package groupg.controller;

import groupg.database.HospitalData;
import groupg.database.Person;
import groupg.jfx.ResourceManager;
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminMain.fxml", "Admin Main", cancelBtn.getScene());
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editPersAdd.fxml", "Add New Person", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        if (persList.getSelectionModel().getSelectedItem() != null)
        {
            HospitalData.removePersonById(persList.getSelectionModel().getSelectedItem().getId());
            persList.getItems().remove(persList.getSelectionModel().getSelectedIndex());
        }
    }

    public void onEdit(ActionEvent event)
    {
        Person out = persList.getSelectionModel().getSelectedItem();
        if (out != null)
        {
            try
            {
                ResourceManager.getInstance()
                        .<EditPersFineController>loadFXMLIntoScene("/view/editPersFine.fxml", "Edit Person",
                                                                   editBtn.getScene(),
                                                                   (controllerIn) -> controllerIn.setPerson(out));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
