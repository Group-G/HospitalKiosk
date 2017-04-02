package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-23
 */
public class WelcomeScreenController implements Initializable, Controller
{
    @FXML
    private Button adminBtn;
    @FXML
    private HBox topHBox;
    @FXML
    private MenuButton catDropdown;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        AutoCompleteTextField textField = new AutoCompleteTextField();
        textField.setMinWidth(200);
        textField.setPromptText("Search for something...");
        textField.getEntries().add("TEST 1"); //TODO: Populate this from DB

        ObservableList<Node> children = FXCollections.observableArrayList(topHBox.getChildren());
        children.add(textField);
        Collections.swap(children, 0, 1);
        topHBox.getChildren().setAll(children);
    }

    public void onAdminLogin(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/adminLogin.fxml", "Admin Login", adminBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
