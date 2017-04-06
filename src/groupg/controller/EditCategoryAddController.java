package groupg.controller;

import groupg.database.HospitalData;
import groupg.jfx.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class EditCategoryAddController implements Initializable
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField catNameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {
        HospitalData.addCategory(catNameField.getText());

        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", addBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
