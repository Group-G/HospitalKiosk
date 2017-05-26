package groupg.controller;

import groupg.Main;
import groupg.database.Category;
import groupg.database.Location;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static groupg.Main.h;

/**
 * @author Sam Comeau
 */
public class EditCategoryEditController implements Initializable
{
    @FXML
    private Button cancelBtn, submitBtn;
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
    private Category category;
    private  String nCategory = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    public void onSubmit(ActionEvent event)
    {
        if(h.checkString(catNameField.getText()) && !(!catNameField.getText().equals("Stairs") && category.getCategory().equals("Stairs")) && !(!catNameField.getText().equals("Elevator") && category.getCategory().equals("Elevator"))){
            nCategory = catNameField.getText();
            if (radioprivate.isSelected() && quickSearchable.isSelected()) {
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 1, colorField.getValue().toString(), 1));
            } else if (radiopublic.isSelected() && quickSearchable.isSelected()) {
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 0, colorField.getValue().toString(), 1));
            } else if (radioprivate.isSelected() && !quickSearchable.isSelected()){
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 1, colorField.getValue().toString(), 0));
            } else if (radiopublic.isSelected() && !quickSearchable.isSelected()) {
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 0, colorField.getValue().toString(), 0));
            }
            else {
                //default public
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 0, colorField.getValue().toString(), 0));
            }
            try
            {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", submitBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            for (Location l: Main.h.getAllLocations()){
                if(l.getCategory().equals(category)){
                    for(Category c : Main.h.getAllCategories()){
                        if (c.getCategory().equals(nCategory)){
                            l.setCategory(c);
                        }
                    }
                }
            }

        }
        else{
            errorText.setText("You cannot edit stairs or elevators title!");
            errorText.setVisible(true);
        }
    }

    public void setCategory(Category c){
        this.category = c;
        catNameField.setText(c.getCategory());
        if(c.getPermission() == 1){
            radiopublic.setSelected(true);
        } else {
            radioprivate.setSelected(true);
        }

        if(c.getQuicksearchOn() == 1){
            quickSearchable.setSelected(true);
        } else {
            quickSearchable.setSelected(false);
        }

        colorField.setValue(Color.web(c.getColor()));

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
