package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class EditCoordController implements Initializable
{
    @FXML
    private Button cancelBtn, logoutBtn;

    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.EDIT_NODES_CANVAS);
    private Pane overlay = new Pane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        List<String> floors = new ArrayList<>(); //TODO: Populate this with all floors
        floors.add("TEST FLOOR 1");
        floors.add("TEST FLOOR 2");
        List<String> buildings = new ArrayList<>(); //TODO: Populate this with all buildings
        buildings.add("TEST BUILDING 1");
        buildings.add("TEST BUILDING 2");
        List<String> nodes = new ArrayList<>(); //TODO: Populate this with all nodes
        nodes.add("TEST NODE 1");
        nodes.add("TEST NODE 2");

        Circle node1 = NodeFactory.drawNode(30, 30);
        Circle node2 = NodeFactory.drawNode(40, 40);
        MouseGestures.getInstance().makeDraggable(node1, node2);

        overlay.getChildren().addAll(node1, node2);
        canvasWrapper.getChildren().add(canvas);
        canvasWrapper.getChildren().add(overlay);
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

    public void onAdd(ActionEvent actionEvent)
    {
        Circle node = NodeFactory.drawNode(100, 100);
        canvasWrapper.getChildren().remove(overlay);
        overlay.getChildren().add(node);
        canvasWrapper.getChildren().add(overlay);
    }
}
