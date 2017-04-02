package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class EditCategoryController implements Initializable
{
    @FXML
    private Button cancelBtn, newBtn, deleteBtn;
    @FXML
    private ListView<String> catList;

    private ObservableList<String> cats = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cats.clear();
        cats.add("Test cat 1");
        cats.add("Test cat 2");
        catList.getItems().setAll(cats); //TODO: Get cats from DB
    }

    public void onCancel(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/adminMain.fxml", "Admin Main", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editCategoryAdd.fxml", "Add New Category", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        //TODO: Remove cat from DB
        catList.getItems().remove(catList.getSelectionModel().getSelectedItem());
    }
}
