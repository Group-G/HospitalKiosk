package groupg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
    private Button cancelBtn, logoutBtn, addBtn, changeNameBtn;
    @FXML
    private TextField addNameField, addXYField, oldNameField, newNameField;
    @FXML
    private MenuButton floorDD, buildingDD, neighborDD;
    @FXML
    private GridPane canvasWrapper;

    //Selected items on add screen
    private String selectedFloor = "", selectedBuilding = "", selectedNeighbor = "";
    //Node selected from selNodeDD
    private String nodeToBeRemoved = "";

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

        floors.forEach(elem ->
                       {
                           MenuItem item = new MenuItem(elem);
                           item.setOnAction(actionEvent ->
                                            {
                                                selectedFloor = item.getText();
                                                floorDD.setText(selectedFloor);
                                            });
                           floorDD.getItems().add(new MenuItem(elem));
                       });

        buildings.forEach(elem ->
                          {
                              MenuItem item = new MenuItem(elem);
                              item.setOnAction(actionEvent ->
                                               {
                                                   selectedBuilding = item.getText();
                                                   buildingDD.setText(selectedBuilding);
                                               });
                              buildingDD.getItems().add(new MenuItem(elem));
                          });

        nodes.forEach(elem ->
                      {
                          MenuItem item = new MenuItem(elem);
                          item.setOnAction(actionEvent ->
                                           {
                                               selectedNeighbor = item.getText();
                                               neighborDD.setText(selectedNeighbor);
                                           });
                          neighborDD.getItems().add(new MenuItem(elem));
                      });

        ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.EDIT_NODES_CANVAS);
        Circle node1 = NodeFactory.drawNode(30, 30);
        Circle node2 = NodeFactory.drawNode(40, 40);
        MouseGestures.getInstance().makeDraggable(node1, node2);
        Pane overlay = new Pane();
        overlay.getChildren().addAll(node1, node2);
        canvasWrapper.getChildren().addAll(canvas, overlay);
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
            ResourceManager.getInstance().loadFXMLIntoScene("/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent actionEvent)
    {
        //Add node

    }

    public void onChangeName(ActionEvent actionEvent)
    {
        //Change coord name from oldNameField, newNameField
    }
}
