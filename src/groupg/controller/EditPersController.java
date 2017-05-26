package groupg.controller;

import static groupg.Main.h;
import groupg.database.Person;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class EditPersController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, editBtn, deleteBtn;
    @FXML
    private ListView<Person> persList;
    @FXML
    private BorderPane time;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
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
        persList.getItems().setAll(h.getAllPeople()); //Add all people from DB to listview
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editPersAdd.fxml", "Add New Person", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        if (persList.getSelectionModel().getSelectedItem() != null)
        {
            h.removePersonById(persList.getSelectionModel().getSelectedItem().getId());
            persList.getItems().remove(persList.getSelectionModel().getSelectedIndex());
        }
    }

    public void onEdit(ActionEvent event)
    {
        Person out = persList.getSelectionModel().getSelectedItem();
        if (out != null)
        {
            try
            {
                ResourceManager.getInstance()
                        .<EditPersFineController>loadFXMLIntoScene("/view/editPersFine.fxml", "Edit Person",
                                                                   editBtn.getScene(),
                                                                   (controllerIn) -> controllerIn.setPerson(out));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onLogout() {
        mouseMoving.set(false);
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml","Welcome",newBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
