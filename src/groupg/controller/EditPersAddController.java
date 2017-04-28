package groupg.controller;

import static groupg.Main.h;
import groupg.database.Location;
import groupg.database.Person;
import groupg.jfx.AutoCompleteTextField;
import groupg.jfx.ResourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class EditPersAddController implements Initializable
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

    private AutoCompleteTextField locField = new AutoCompleteTextField();
    private Set<Location> possibleLocs = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        locField.setMinWidth(200);
        locField.setPromptText("Location");
        possibleLocs.addAll(h.getAllLocations()); //Grab all locs from DB
        locField.getEntries().addAll(possibleLocs);
        ObservableList<Node> children = FXCollections.observableArrayList(locHBox.getChildren());
        children.add(locField);
        locHBox.getChildren().setAll(children);
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
            h.getAllPeople().add(new Person(nameField.getText(),
                    titleField.getText(),
                    locList.getItems()
                            .stream()
                            .map(Location::getID)
                            .collect(Collectors.toList())));
            try {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/editPers.fxml", "Edit Personnel", confirmBtn.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddLoc(ActionEvent actionEvent)
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
}
