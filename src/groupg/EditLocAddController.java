package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Ryan Benasutti
 * @since 2017-04-02
 */
public class EditLocAddController implements Controller
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField locNameField;

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
