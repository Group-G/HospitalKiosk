package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminMainController implements Initializable
{
    @FXML
    private Button logoutBtn, addNodeBtn, editCatBtn, editPersBtn, editLocsBtn;

    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.DRAW_FLOOR_4);
    private Pane overlay = new Pane();
    static ObservableList<Shape> displayedShapes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Listener to remove displayedShapes when they are right-click deleted
        displayedShapes.addListener((ListChangeListener.Change<? extends Shape> in) -> {
            canvasWrapper.getChildren().clear();
            canvasWrapper.getChildren().add(canvas);
            overlay.getChildren().setAll(displayedShapes);
            canvasWrapper.getChildren().add(overlay);
        });

        canvasWrapper.getChildren().add(canvas);
        canvasWrapper.getChildren().add(overlay);
    }

    public void onLogout(ActionEvent actionEvent)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/welcomeScreen.fxml", "Welcome", logoutBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAddNode(ActionEvent actionEvent)
    {
        UniqueNode node = NodeFactory.getNode(100, 100);
        displayedShapes.add(node);
    }

    public void onEditCat(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editCategory.fxml", "Edit Categories", editCatBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onEditPers(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editPers.fxml", "Edit Personnel", editCatBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onEditLocs(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editLocs.fxml", "Edit Locations", editLocsBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
