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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class EditCoordController implements Initializable
{
    @FXML
    private Button logoutBtn, addNodeBtn, editCatBtn;

    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.EDIT_NODES_CANVAS);
    private Pane overlay = new Pane();
    private ObservableList<UniqueNode> nodes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Listener to remove nodes when they are right-click deleted
        nodes.addListener((ListChangeListener.Change<? extends UniqueNode> in) -> {
            canvasWrapper.getChildren().clear();
            canvasWrapper.getChildren().add(canvas);
            overlay.getChildren().setAll(nodes);
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
        canvasWrapper.getChildren().remove(overlay);
        nodes.add(node);
        MouseGestures.setNodes(nodes);
        overlay.getChildren().setAll(nodes);
    }

    public void onEditCat(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/editCategory.fxml", "Edit Categories", logoutBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
