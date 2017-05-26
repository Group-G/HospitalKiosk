package groupg.controller;

import groupg.database.Admin;
import groupg.Main;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static groupg.Main.h;

/**
 * @author Sam Comeau
 * @since 2017-04-18
 */
public class EditAdminController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<Admin> addList;
    @FXML
    private BorderPane time;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    private ObservableList<String> cats = FXCollections.observableArrayList();
    private static int numOfAdmins = 2;
    @FXML
    private Text errorText;

    public int getNumOfAdmins() {
        return numOfAdmins;
    }

    public void setNumOfAdmins(int numOfAdmins) {
        this.numOfAdmins = numOfAdmins;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        errorText.setVisible(false);
        cats.clear();
        addList.getItems().setAll(h.getAllAdmins());
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (!isNowMoving) {
                //System.out.println("Mouse stopped!");
            }
        });
        pause.setOnFinished(e -> onLogout());
        time.setOnMouseClicked(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseDragged(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseMoved(e -> {
            mouseMoving.set(true);
            pause.playFromStart();
            // System.out.println("Mouse move!");
        });
    }

    public void onCancel(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminMain.fxml", "Admin Main", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent actionEvent)
    {

        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdminAdd.fxml", "Add New Admin", newBtn.getScene());
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        if(getNumOfAdmins() == 2){
            errorText.setText("There must be at least 2 Admins");
            errorText.setVisible(true);
            return;
        }
        if(addList.getSelectionModel().getSelectedItem() == h.getCurrentAdmin()){
            errorText.setText("You cannot delete yourself!");
            errorText.setVisible(true);
        } else {
            h.removeAdmin(addList.getSelectionModel().getSelectedItem());
            addList.getItems().remove(addList.getSelectionModel().getSelectedItem());
            setNumOfAdmins(getNumOfAdmins()-1);
        }

    }

    public void onEdit(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdminEdit.fxml", "Edit Admin", newBtn.getScene());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onLogout() {
        mouseMoving.set(false);
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml","Welcome",cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
