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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class DirectionScreenController implements Initializable, Controller
{
    @FXML
    private Button cancelBtn;
    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.DRAW_FLOOR_4);
    private Pane overlay = new Pane();
    static ObservableList<Shape> displayedShapes = FXCollections.observableArrayList();

    private Astar astar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Listener to remove displayedShapes when they are right-click deleted
        displayedShapes.addListener((ListChangeListener.Change<? extends Shape> in) -> {
            canvasWrapper.getChildren().clear();
            canvasWrapper.add(canvas, 0, 1);
            overlay.getChildren().setAll(displayedShapes);
            canvasWrapper.add(overlay, 0, 1);
        });

        canvasWrapper.add(canvas, 0, 1);
        canvasWrapper.add(overlay, 0, 1);

        LinkedList<Location> locations = new LinkedList<>();
        Location location1 = new Location("test 1", 10, 10, new LinkedList<>(), "", 0, 1, "", "");
        Location location2 = new Location("test 2", 10, 10, new LinkedList<>(), "", 0, 2, "", "");
        location1.getNeighbors().add(2);
        location2.getNeighbors().add(1);
        astar = new Astar(locations);
        LinkedList<Location> output = astar.run(location1, location2);
        List<Location> locs = new ArrayList<>();
        locs.addAll(output);
        for (int i = 0; i < locs.size() - 1; i++)
        {
            Location loc1 = locs.get(i);
            Location loc2 =locs.get(i+1);
            displayedShapes.add(new Line(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY()));
        }
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
}
