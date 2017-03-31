package groupg;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
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

    //directionScreen
    @FXML
    private Button dirScreenCancelBtn, dirScreenExportBtn;
    @FXML
    private TextField dirScreenStartLocField, dirScreenDestField;
    @FXML
    private ListView dirScreenList;
    @FXML
    private MenuButton dirScreenStartLocCategory, dirScreenDestCategory;

    //adminMain
    @FXML
    private Button adminMainEditCategoryBtn, adminMainEditCoordBtn, adminMainLogoutBtn;

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

    public void adminMainOnEditCategory(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/editCategory.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)adminMainEditCategoryBtn.getScene().getWindow();
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

    public void adminMainOnEditCoords(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/editCoordinates.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)adminMainEditCoordBtn.getScene().getWindow();
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

    public void adminMainOnLogout(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)adminMainLogoutBtn.getScene().getWindow();
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
