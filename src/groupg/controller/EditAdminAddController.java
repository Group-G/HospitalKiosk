package groupg.controller;

import groupg.database.Admin;
import groupg.Main;
import static groupg.Main.h;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sam Comeau
 * @since 2017-04-18
 */
public class EditAdminAddController implements Initializable
{
    EditAdminController ed = new EditAdminController();
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private RadioButton perm0, perm1;
    @FXML
    private Text errorText;
    @FXML
    private GridPane time;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        final ToggleGroup group = new ToggleGroup();
        perm0.setToggleGroup(group);
        perm1.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    //System.out.println(group.getSelectedToggle().getUserData().toString());
                }
            }
        });
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

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {

       // System.out.println(ed.getNumOfAdmins());
        if(!h.checkEmptyString(usernameField.getText().trim())){
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);
        }else if(!h.checkEmptyString(passwordField.getText().trim())){
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);
        } else {
            if (perm0.isSelected()) {
                if (h.getCheckUsername(usernameField.getText())) {
                    errorText.setText("This username already exists.");
                    errorText.setVisible(true);
                } else {
                    Main.h.addAdmin(new Admin(usernameField.getText(), passwordField.getText(), 0));
                    ed.setNumOfAdmins(ed.getNumOfAdmins()+1);
                    try {
                        ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", addBtn.getScene());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (perm1.isSelected()) {
                if (h.getCheckUsername(usernameField.getText())) {
                    errorText.setText("This username already exists.");
                    errorText.setVisible(true);
                } else {
                    Main.h.addAdmin(new Admin(usernameField.getText(), passwordField.getText(), 1));
                    ed.setNumOfAdmins(ed.getNumOfAdmins()+1);
                    try {
                        ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", addBtn.getScene());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //default less permissions
                if (h.getCheckUsername(usernameField.getText())) {
                    errorText.setText("This username already exists.");
                    errorText.setVisible(true);
                } else {
                    Main.h.addAdmin(new Admin(usernameField.getText(), passwordField.getText(), 0));
                    ed.setNumOfAdmins(ed.getNumOfAdmins()+1);
                    try {
                        ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", addBtn.getScene());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }






            }
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
