package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class EditCoordController
{
    @FXML
    private Button cancelBtn, logoutBtn, addBtn, remBtn, changeNameBtn;
    @FXML
    private TextField addNameField, addXYField, remField, oldNameField, newNameField;

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

    public void onLogout(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent actionEvent)
    {
        //Add coord from addNameField, addXYField
    }

    public void onRem(ActionEvent actionEvent)
    {
        //Remove coord from remField
    }

    public void onChangeName(ActionEvent actionEvent)
    {
        //Change coord name from oldNameField, newNameField
    }
}
