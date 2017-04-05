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
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class EditPersFineController implements Initializable
{
    @FXML
    private TextField nameField, titleField;
    @FXML
    private Button cancelBtn, confirmBtn, addLocBtn;
    @FXML
    private ListView<Location> locList;
    @FXML
    private HBox locHBox;

    private Person pers;
    private AutoCompleteTextField<Location> locField = new AutoCompleteTextField<>();
    private Set<Location> possibleLocs = new HashSet<>();

    EditPersFineController(Person pers)
    {
        this.pers = pers;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        nameField.setText(pers.getName());
        titleField.setText(pers.getTitle());
        locList.getItems().setAll(pers.getLocations().stream()
                                      .map(HospitalData::getLocationById)
                                      .collect(Collectors.toList())); //Add person's locs from DB
        locField.setMinWidth(200);
        locField.setPromptText("Location");
        possibleLocs.addAll(HospitalData.getAllLocations()); //Grab all locs from DB
        locField.getEntries().addAll(possibleLocs);
        ObservableList<Node> children = FXCollections.observableArrayList(locHBox.getChildren());
        children.add(locField);
        locHBox.getChildren().setAll(children);

        cancelBtn.setOnAction(event ->
                              {
                                  try
                                  {
                                      ResourceManager.getInstance().loadFXMLIntoScene("/editPers.fxml", "Edit Personnel", cancelBtn.getScene());
                                  }
                                  catch (IOException e)
                                  {
                                      e.printStackTrace();
                                  }
                              });

        confirmBtn.setOnAction(event ->
                               {
                                   try
                                   {
                                       ResourceManager.getInstance().loadFXMLIntoScene("/editPers.fxml", "Edit Personnel", confirmBtn.getScene());
                                       List<Location> locations = new ArrayList<>();
                                       locations.addAll(locList.getItems());
                                       HospitalData.setPerson(pers.getId(),
                                                              new Person(nameField.getText(),
                                                                         titleField.getText(),
                                                                         locations.stream()
                                                                                  .map(Location::getID)
                                                                                  .collect(Collectors.toList()), 0));
                                   }
                                   catch (IOException e)
                                   {
                                       e.printStackTrace();
                                   }
                               });

        addLocBtn.setOnAction(event ->
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
                              });
    }

    void setPerson(Person p)
    {
        this.pers = p;
    }
}
