package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class EditPersFineController implements Initializable, Controller
{
    @FXML
    private TextField nameField, titleField;
    @FXML
    private Button cancelBtn, confirmBtn, addLocBtn;
    @FXML
    private ListView<String> locList;
    @FXML
    private HBox locHBox;

    private Person pers;
    private AutoCompleteTextField<String> locField = new AutoCompleteTextField<>();
    private Set<String> possibleLocs = new HashSet<>();

    EditPersFineController(Person pers)
    {
        this.pers = pers;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        nameField.setText(pers.getName());
        titleField.setText(pers.getTitle());
        locList.getItems().setAll(pers.getLocationsAsStrings());

        locField.setMinWidth(200);
        locField.setPromptText("Location");
        possibleLocs.add("TEST 1"); //TODO: Populate this from DB
        locField.getEntries().addAll(possibleLocs);
        ObservableList<Node> children = FXCollections.observableArrayList(locHBox.getChildren());
        children.add(locField);
        Collections.swap(children, 0, 1);
        locHBox.getChildren().setAll(children);

        cancelBtn.setOnAction(event -> {
            try
            {
                ResourceManager.getInstance().loadFXMLIntoScene("/editPers.fxml", "Edit Personnel", cancelBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        confirmBtn.setOnAction(event -> {
            try
            {
                //TODO: Push data to DB
                ResourceManager.getInstance().loadFXMLIntoScene("/editPers.fxml", "Edit Personnel", confirmBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        addLocBtn.setOnAction(event -> {
            if (!locList.getItems().contains(locField.getText()) && possibleLocs.contains(locField.getText()))
            {
                locList.getItems().add(locField.getText()); //TODO: Update locs for this person in DB
            }
        });
    }
}
