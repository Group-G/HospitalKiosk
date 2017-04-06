package groupg.controller;

import groupg.database.HospitalData;
import groupg.jfx.ResourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class EditCategoryController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<String> catList;

    private ObservableList<String> cats = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cats.clear();
        catList.getItems().setAll(HospitalData.getAllCategories());
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategoryAdd.fxml", "Add New Category", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        HospitalData.removeCategory(catList.getSelectionModel().getSelectedItem());
        catList.getItems().remove(catList.getSelectionModel().getSelectedItem());
    }
}
