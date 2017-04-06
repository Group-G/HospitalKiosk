package groupg.controller;

import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.jfx.AutoCompleteTextField;
import groupg.jfx.ResourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-23
 */
public class WelcomeScreenController implements Initializable
{
    @FXML
    private Button adminBtn, searchBtn;
    @FXML
    private HBox topHBox;
    @FXML
    private MenuButton catDropdown;
    static Location requested;
    private AutoCompleteTextField textField = new AutoCompleteTextField();
    private String selectedCat;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        textField.setMinWidth(200);
        textField.setPromptText("Search for something...");
        textField.getEntries().addAll(HospitalData.getAllLocations());

        ObservableList<Node> children = FXCollections.observableArrayList(topHBox.getChildren());
        children.add(textField);
        Collections.swap(children, 1, 2);
        topHBox.getChildren().setAll(children);

        HospitalData.getAllCategories().forEach(elem ->
                                                {
                                                    MenuItem item = new MenuItem(elem);
                                                    item.setOnAction(actionEvent ->
                                                                     {
                                                                         selectedCat = elem;
                                                                         catDropdown.setText(elem);
                                                                         textField.getEntries().clear();
                                                                         textField.getEntries().addAll(HospitalData.getLocationsByCategory(elem));
                                                                         HospitalData.getLocationsByCategory(elem).forEach(System.out::println);
                                                                     });
                                                    catDropdown.getItems().add(item);
                                                });
    }

    public void onAdminLogin(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminLogin.fxml", "Admin Login", adminBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onSearch(ActionEvent actionEvent)
    {
        requested = textField.getCurrentSelection();

        if (requested != null)
        {
            DirectionScreenController controller = new DirectionScreenController(requested);
            ResourceManager.getInstance()
                           .loadFXMLIntoSceneWithController("/view/directionScreen.fxml",
                                                            "Your Directions",
                                                            searchBtn.getScene(),
                                                            controller);
        }
    }
}
