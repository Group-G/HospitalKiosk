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
public class AdminLoginController
{
    @FXML
    private Button cancelBtn, loginBtn;
    @FXML
    private TextField usernameField, passField;

    public void onCancel(ActionEvent actionEvent)
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

    public void onLogin(ActionEvent actionEvent)
    {
        //Check credentials: usernameField, passField

        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editCoordinates.fxml", "Edit Coordinates", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
