
package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class DirectionScreenController implements Initializable, Controller
{
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button cancelBtn, searchBtn;
    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.DRAW_FLOOR_4);
    private Pane overlay = new Pane();
    static ObservableList<Shape> displayedShapes = FXCollections.observableArrayList();

    private AutoCompleteTextField<Location> startLocField = new AutoCompleteTextField<>(), destField = new AutoCompleteTextField<>();

    private Astar astar;
    private LinkedList<Location> locations = new LinkedList<>();
    private LinkedList<Integer> loc1N = new LinkedList<>(), loc2N = new LinkedList<>(), loc3N = new LinkedList<>();
    private Location location1 = new Location("test 1", 10, 10, loc1N, "", 0, 1, "", ""),
            location2 = new Location("test 2", 100, 100, loc2N, "", 0, 2, "", ""),
            location3 = new Location("test 3", 250, 100, loc3N, "", 0, 3, "", "");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        displayedShapes.addListener((ListChangeListener.Change<? extends Shape> in) ->
                                    {
                                        canvasWrapper.getChildren().clear();
                                        canvasWrapper.add(canvas, 0, 0);
                                        overlay.getChildren().setAll(displayedShapes);
                                        canvasWrapper.add(overlay, 0, 0);
                                    });

        canvasWrapper.add(canvas, 0, 0);
        canvasWrapper.add(overlay, 0, 0);

        //Add neighbors for testing
        loc1N.add(2);
        loc2N.add(3);
        loc3N.add(1);
        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        startLocField.getEntries().add(location1); //TODO: Pull locs from DB
        startLocField.getEntries().add(location2);
        startLocField.getEntries().add(location3);
        destField.getEntries().add(location1);
        destField.getEntries().add(location2);
        destField.getEntries().add(location3);

        ObservableList<Node> children = FXCollections.observableArrayList(toolBar.getItems());
        children.addAll(startLocField, destField);
        Collections.swap(children, 3, 4);
        toolBar.getItems().setAll(children);
    }

    public void onCancel(ActionEvent event)
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

    public void onSearch(ActionEvent actionEvent)
    {
        if (startLocField.getCurrentSelection() != null && destField.getCurrentSelection() != null)
        {
            astar = new Astar(locations);
            LinkedList<Location> output = astar.run(startLocField.getCurrentSelection(), destField.getCurrentSelection());
            List<Location> locs = new ArrayList<>();
            locs.addAll(output);

            //Draw locations
            displayedShapes.clear();
            for (int i = 0; i < locs.size() - 1; i++)
            {
                Location loc1 = locs.get(i);
                Location loc2 = locs.get(i + 1);
                Line line = new Line(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
                displayedShapes.add(line);
            }
        }
    }
}
