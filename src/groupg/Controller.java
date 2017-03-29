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
 * @version 0.0
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

    //adminLogin
    @FXML
    private Button adminLoginCloseBtn, adminLoginLoginBtn;
    @FXML
    private TextField adminLoginUsernameField, adminLoginPasswordField;

    //adminMain
    @FXML
    private Button adminMainEditCategoryBtn, adminMainEditCoordBtn, adminMainLogoutBtn;

    //editCategory
    @FXML
    private Button editCategoryLogoutBtn, editCategoryCancelBtn, editCategoryEnterBtn, editCategoryRemBtn;
    @FXML
    private TextField editCategoryCategoryNameField, editCategoryCoordNameField, editCategoryRemoveField;

    //editCoordinates
    @FXML
    private Button editCoordAddCoordBtn, editCoordRemBtn, editCoordChangeNameBtn, editCoordCancelBtn, editCoordLogoutBtn;
    @FXML
    private TextField editCoordNameField, editCoordCoordField, editCoordNeighborField, editCoordRemoveField, editCoordOldNameField, editCoordNewNameField;

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

    public void adminLoginClose(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) adminLoginCloseBtn.getScene().getWindow();
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

    public void onLogin(ActionEvent actionEvent)
    {
        //Check credentials: adminLoginUsernameField, adminLoginPasswordField

        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/adminMain.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) adminLoginLoginBtn.getScene().getWindow();
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

    public void editCategoryOnCancel(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/adminMain.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) editCategoryCancelBtn.getScene().getWindow();
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

    public void editCategoryOnLogout(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage) editCategoryLogoutBtn.getScene().getWindow();
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

    public void editCategoryOnAdd(ActionEvent actionEvent)
    {
        //Add category from editCategoryCategoryNameField, editCategoryCoordNameField
    }

    public void editCategoryOnRem(ActionEvent actionEvent)
    {
        //Remove category from editCategoryRemoveField
    }

    public void editCoordOnAddCoord(ActionEvent actionEvent)
    {
        //Add coord from editCoordNameField, editCoordCoordField, editCoordNeighborField
    }

    public void editCoordOnRemCoord(ActionEvent actionEvent)
    {
        //Remove coord from editCoordRemoveField
    }

    public void editCoordOnChangeName(ActionEvent actionEvent)
    {
        //Change coord name from editCoordOldNameField, editCoordNewNameField
    }

    public void editCoordOnCancel(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/adminMain.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)editCoordCancelBtn.getScene().getWindow();
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

    public void editCoordOnLogout(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("/welcomeScreen.fxml"));
            Platform.runLater(() ->
                              {
                                  Stage stage = (Stage)editCoordLogoutBtn.getScene().getWindow();
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
