package groupg;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Ryan Benasutti
 * @since 2017-03-23
 */
public class Controller
{
    //welcomeScreen
    @FXML
    private Button welcomeAdminLoginBtn;
    @FXML
    private TextField welcomeScreenSearchField;

    public void moveToAdminWindow(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/adminLogin.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) welcomeAdminLoginBtn.getScene().getWindow();
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
}
