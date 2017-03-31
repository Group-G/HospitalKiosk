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
 * @since 2017-03-30
 */
public class EditCategoryController
{
    @FXML
    private Button logoutBtn, cancelBtn, addCatBtn, remCatBtn;
    @FXML
    private TextField addCatNameField, remCatNameField;

    public void onCancel(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/adminMain.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) cancelBtn.getScene().getWindow();
                                  stage.setTitle("Admin Main");
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
                                  Stage stage = (Stage) logoutBtn.getScene().getWindow();
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

    public void onAdd(ActionEvent actionEvent)
    {
        //Add category from editCategoryCategoryNameField, editCategoryCoordNameField
    }

    public void onRem(ActionEvent actionEvent)
    {
        //Remove category from editCategoryRemoveField
    }
}
