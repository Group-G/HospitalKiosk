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
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer inactiveTimeOut;
    private int cursorX;
    private int cursorY;
    private int seconds = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        errorText.setVisible(false);
        /*
        cancelBtn.getScene().setOnMouseMoved(event -> {
            System.out.println("mouse event triggered");
            inactiveTimeOut.cancel();
            isInactive();
        });

        cancelBtn.getScene().setOnKeyPressed(event -> {
            System.out.println("key event triggered");
            inactiveTimeOut.cancel();
            isInactive();
        });

        isInactive();
        */
    }

    private void isInactive()
    {
        inactiveTimeOut.schedule(new TimerTask()
        {
            public void run()
            {
                System.out.println("time is up");
                try
                {
                    ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                inactiveTimeOut.cancel();
            }
        }, seconds * 1000);
    }

    public void onCancel(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
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
                ResourceManager.getInstance().loadFXMLIntoScene("/view/adminMain.fxml", "Admin Main", cancelBtn.getScene());
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
