package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-04-02
 */
public class EditLocAddController implements Initializable
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField locNameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editLocs.fxml", "Edit Locations", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {
        //TODO: Add loc to DB
        System.out.println("Added location: " + locNameField.getText());

        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editLocs.fxml", "Edit Locations", addBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
