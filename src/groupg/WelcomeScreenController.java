package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Ryan Benasutti
 * @since 2017-03-23
 */
public class WelcomeScreenController
{
    @FXML
    private Button adminBtn;
    @FXML
    private TextField searchField;
    @FXML
    private MenuButton catDropdown;

    public void onAdminLogin(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/adminLogin.fxml", "Admin Login", adminBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onCatSelect(ActionEvent actionEvent)
    {
        //Do something with catDropdown
    }
}
