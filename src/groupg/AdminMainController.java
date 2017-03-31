package groupg;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminMainController
{
    @FXML
    private Button editCatBtn, editCoordBtn, logoutBtn;

    public void onEditCat(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/editCategory.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)editCatBtn.getScene().getWindow();
                                  stage.setTitle("Edit Category");
                                  stage.setScene(new Scene(root, 610, 400));
                                  stage.show();
                              });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onEditCoord(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/editCoordinates.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)editCoordBtn.getScene().getWindow();
                                  stage.setTitle("Edit Coordinates");
                                  stage.setScene(new Scene(root, 610, 400));
                                  stage.show();
                              });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onLogout(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)logoutBtn.getScene().getWindow();
                                  stage.setTitle("Welcome");
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
