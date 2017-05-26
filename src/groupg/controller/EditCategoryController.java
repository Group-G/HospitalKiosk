package groupg.controller;

import groupg.database.Category;
import static groupg.Main.h;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

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
    private Button cancelBtn, newBtn, deleteBtn, editBtn;
    @FXML
    private ListView<Category> catList;
    @FXML
    private BorderPane time;
    private ObservableList<String> cats = FXCollections.observableArrayList();
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cats.clear();
        catList.getItems().setAll(h.getAllCategories());

        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (!isNowMoving) {
                //System.out.println("Mouse stopped!");
            }
        });
        pause.setOnFinished(e -> onLogout());
        time.setOnMouseClicked(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseDragged(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseMoved(e -> {
            mouseMoving.set(true);
            pause.playFromStart();
            // System.out.println("Mouse move!");
        });
    }

    public void onCancel(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminMain.fxml", "Admin Main", cancelBtn.getScene());
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategoryAdd.fxml", "Add New Category", newBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        h.removeCategory(catList.getSelectionModel().getSelectedItem());
        catList.getItems().remove(catList.getSelectionModel().getSelectedItem());
    }

    public void onEdit(ActionEvent event) {
        Category out = catList.getSelectionModel().getSelectedItem();
        if (out != null) {
            try {
                ResourceManager.getInstance()
                        .<EditCategoryEditController>loadFXMLIntoScene("/view/editCategoryEdit.fxml", "Edit Category",
                                editBtn.getScene(),
                                (controllerIn) -> controllerIn.setCategory(out));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void onLogout() {
        mouseMoving.set(false);
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml","Welcome",cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
