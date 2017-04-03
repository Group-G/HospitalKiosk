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
        locList.getItems().setAll(pers.getLocations().stream()
                                      .map(HospitalData::getLocationById)
                                      .collect(Collectors.toList())); //Add person's locs from DB

        locField.setMinWidth(200);
        locField.setPromptText("Location");
        locField.getEntries().addAll(HospitalData.getAllLocations()); //Grab all locs from DB
        ObservableList<Node> children = FXCollections.observableArrayList(locHBox.getChildren());
        children.add(locField);
        Collections.swap(children, 0, 1);
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
                                       //TODO: Push data to DB
                                       ResourceManager.getInstance().loadFXMLIntoScene("/editPers.fxml", "Edit Personnel", confirmBtn.getScene());
                                   }
                                   catch (IOException e)
                                   {
                                       e.printStackTrace();
                                   }
                               });

        addLocBtn.setOnAction(event ->
                              {
                                  int numMatches = 0;
                                  for (Location loc : locList.getItems())
                                  {
                                      if (loc.getName().equals(locField.getText()))
                                      {
                                          numMatches++;
                                      }
                                  }

                                  if (numMatches == 0 && possibleLocs.contains(locField.getText()))
                                  {
                                      locList.getItems().add(locField.getCurrentSelection()); //TODO: Update locs for this person in DB
                                  }
                              });
    }
}
