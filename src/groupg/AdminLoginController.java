package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminLoginController implements Initializable
{
    @FXML
    private Button cancelBtn, loginBtn;
    @FXML
    private TextField usernameField, passField;
    @FXML
    private Text errorText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        errorText.setVisible(false);
    }

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
        attemptLogin(actionEvent);
    }

    public void onEnterKey(ActionEvent actionEvent)
    {
        attemptLogin(actionEvent);
    }

    private void attemptLogin(ActionEvent actionEvent)
    {
        if (usernameField.getText().equals(HospitalData.login[0]) &&
            passField.getText().equals(HospitalData.login[1]))
        {
            errorText.setVisible(false);

            try
            {
                ResourceManager.getInstance().loadFXMLIntoScene("/adminMain.fxml", "Admin Main", cancelBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            errorText.setVisible(true);
        }
    }
}
