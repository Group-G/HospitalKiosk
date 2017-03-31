package groupg;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/adminLogin.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) adminBtn.getScene().getWindow();
                                  stage.setTitle("Admin Login");
                                  stage.setScene(new Scene(root, 610, 400));
                                  stage.show();
                              });
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
