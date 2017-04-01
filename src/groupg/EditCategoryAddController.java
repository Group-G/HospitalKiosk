package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class EditCategoryAddController
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField catNameField;

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editCategory.fxml", "Edit Categories", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {
        //TODO: Add cat to DB
        System.out.println("Added category: " + catNameField.getText());
    }
}
