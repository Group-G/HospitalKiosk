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
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
        //Clear shapes and fill from DB
        displayedShapes.clear();
        for (Location l : HospitalData.getAllLocations())
        {
            displayedShapes.add(NodeFactory.getNode(l));
        }
        overlay.getChildren().setAll(displayedShapes);

        //Listener to remove displayedShapes when they are right-click deleted
        displayedShapes.addListener((ListChangeListener.Change<? extends Shape> in) ->
                                    {
                                        canvasWrapper.getChildren().clear();
                                        canvasWrapper.getChildren().add(canvas);
                                        overlay.getChildren().setAll(displayedShapes);
                                        canvasWrapper.getChildren().add(overlay);
                                    });

        //Add initial elements
        canvasWrapper.getChildren().add(canvas);
        canvasWrapper.getChildren().add(overlay);
    }

    static List<Location> drawConnections(UniqueNode node, ObservableList<Shape> nodes)
    {
        nodes.sort((n1, n2) ->
                   {
                       if (n1 instanceof UniqueNode && n2 instanceof UniqueNode)
                       {
                           double dist1 = node.getLocation().lengthTo(((UniqueNode) n1).getLocation());
                           double dist2 = node.getLocation().lengthTo((((UniqueNode) n2).getLocation()));
                           return Double.compare(dist1, dist2);
                       }
                       return 0;
                   });

        //Collect UniqueNodes into list
        List<Location> out = new ArrayList<>();
        int numUN = 0;
        for (int i = 0; i < nodes.size(); i++)
        {
            if (numUN > 4)
                break;

            if (nodes.get(i) instanceof UniqueNode)
            {
                if (i != 0)
                {
                    out.add(((UniqueNode) nodes.get(i)).getLocation());
                    numUN++;
                }
            }
        }

        //Clear lines
        displayedShapes.setAll(displayedShapes.stream()
                                              .filter(elem -> !(elem instanceof Line))
                                              .collect(Collectors.toList()));

        //Draw lines between the nodes
        for (int i = 0; i < out.size() - 1; i++)
        {
            Location cur = out.get(i);
            Location next = out.get(i + 1);
            Line line = new Line(cur.getX(), cur.getY(), next.getX(), next.getY());
            displayedShapes.add(line);
        }

        return out;
    }

    public void onLogout(ActionEvent actionEvent)
    {
        HospitalData.publishDB(); //Save changes to disk

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
