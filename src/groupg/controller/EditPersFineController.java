package groupg.controller;

import static groupg.Main.h;

import groupg.Main;
import groupg.database.Location;
import groupg.database.HospitalData;
import groupg.database.Person;
import groupg.jfx.AutoCompleteTextField;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static groupg.Main.h;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class EditPersFineController implements Initializable
{
    @FXML
    private TextField nameField, titleField;
    @FXML
    private Button cancelBtn, confirmBtn, addLocBtn;
    @FXML
    private ListView<Location> locList;
    @FXML
    private HBox locHBox;
    @FXML
    private Text errorText;
    @FXML
    private AnchorPane time;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    private Person pers;
    private AutoCompleteTextField locField = new AutoCompleteTextField();
    private Set<Location> possibleLocs = new HashSet<>();

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
        locField.setMinWidth(200);
        locField.setPromptText("Location");
        possibleLocs.addAll(h.getAllLocations()); //Grab all locs from DB
        locField.getEntries().addAll(possibleLocs);
        ObservableList<Node> children = FXCollections.observableArrayList(locHBox.getChildren());
        children.add(locField);
        locHBox.getChildren().setAll(children);
    }

    public void onAdd(ActionEvent actionEvent)
    {
        if (!locList.getItems()
                    .stream()
                    .map(Location::getName)
                    .collect(Collectors.toList())
                    .contains(locField.getText()) &&
            possibleLocs.stream()
                        .map(Location::getName)
                        .collect(Collectors.toList())
                        .contains(locField.getText()))
        {
            locList.getItems().add(locField.getCurrentSelection());
        }
    }

    public void onCancel(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editPers.fxml", "Edit Personnel", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onConfirm(ActionEvent actionEvent)
    {
        try
        {
            if(!h.checkEmptyString(nameField.getText())){
                errorText.setText("The name field must contain a value.");
                errorText.setVisible(true);
            } else if(!h.checkEmptyString(titleField.getText())){
                errorText.setText("The title field must contain a value. Some examples include 'Dr.', 'Nurse', 'Pediatrician'");
                errorText.setVisible(true);
            } else if(!h.checkString(nameField.getText().trim())){
                errorText.setText("There is an error in the name field.");
                errorText.setVisible(true);
            } else if(!h.checkString(titleField.getText().trim())){
                errorText.setText("There is an error in the title field.");
                errorText.setVisible(true);
            } else {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/editPers.fxml", "Edit Personnel", confirmBtn.getScene());
                List<Location> locations = new ArrayList<>();
                locations.addAll(locList.getItems());
                h.setPerson(pers.getId(),
                        new Person(nameField.getText(),
                                titleField.getText(),
                                locations.stream()
                                        .map(Location::getID)
                                        .collect(Collectors.toList()), 0));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void setPerson(Person p)
    {
        this.pers = p;
        nameField.setText(pers.getName());
        titleField.setText(pers.getTitle());
        locList.getItems().setAll(pers.getLocations().stream()
                                      .map(Main.h::getLocationById)
                                      .collect(Collectors.toList())); //Add person's locs from DB
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
