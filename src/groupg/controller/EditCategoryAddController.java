package groupg.controller;

import static groupg.Main.h;

import groupg.Main;
import groupg.database.Category;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */

public class EditCategoryAddController implements Initializable
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField catNameField;
    @FXML
    private ColorPicker colorField;
    @FXML
    private CheckBox quickSearchable;
    @FXML
    private RadioButton radiopublic, radioprivate;
    @FXML
    private Text errorText;
    @FXML
    private AnchorPane time;
    List<Category> quickSearch = new ArrayList<>();
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

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

        errorText.setVisible(false);
        final ToggleGroup group = new ToggleGroup();
        radiopublic.setToggleGroup(group);
        radioprivate.setToggleGroup(group);
        quickSearchable.setAllowIndeterminate(false);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    //System.out.println(group.getSelectedToggle().getUserData().toString());
                }
            }
        });
    }

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {
        if(h.checkString(catNameField.getText())){

            if (radioprivate.isSelected() && quickSearchable.isSelected()) {
                h.addCategory(catNameField.getText(), 1, colorField.getValue().toString(), 1);
            } else if (radiopublic.isSelected() && quickSearchable.isSelected()) {
                h.addCategory(catNameField.getText(), 0, colorField.getValue().toString(), 1);
            } else if (radioprivate.isSelected() && !quickSearchable.isSelected()){
                h.addCategory(catNameField.getText(), 1, colorField.getValue().toString(), 0);
            } else if (radiopublic.isSelected() && !quickSearchable.isSelected()) {
                h.addCategory(catNameField.getText(), 0, colorField.getValue().toString(), 0);
            }
            else {
                //default public
                h.addCategory(catNameField.getText(), 0, colorField.getValue().toString(), 0);
            }
            try
            {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", addBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        else{
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);
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
