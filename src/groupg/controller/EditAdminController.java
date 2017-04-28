package groupg.controller;

import groupg.database.Admin;
import groupg.Main;
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

import static groupg.Main.h;

/**
 * @author Sam Comeau
 * @since 2017-04-18
 */
public class EditAdminController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<Admin> addList;

    private ObservableList<String> cats = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cats.clear();
        addList.getItems().setAll(h.getAllAdmins());
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdminAdd.fxml", "Add New Admin", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        h.removeAdmin(addList.getSelectionModel().getSelectedItem());
        addList.getItems().remove(addList.getSelectionModel().getSelectedItem());
    }

    public void onEdit(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdminEdit.fxml", "Edit Admin", newBtn.getScene());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
