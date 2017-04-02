package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.DRAW_FLOOR_4);
    private Pane overlay = new Pane();
    static ObservableList<Shape> displayedShapes = FXCollections.observableArrayList();

    private Astar astar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        displayedShapes.addListener((ListChangeListener.Change<? extends Shape> in) -> {
            canvasWrapper.getChildren().removeIf(elem -> elem instanceof Pane && !(elem instanceof ResizableCanvas));
            //canvasWrapper.getChildren().add(canvas);
            overlay.getChildren().setAll(displayedShapes);
            canvasWrapper.getChildren().add(overlay);
        });

        canvasWrapper.getChildren().add(canvas);
        canvasWrapper.getChildren().add(overlay);

        LinkedList<Location> locations = new LinkedList<>();
        LinkedList<Integer> loc1N = new LinkedList<>(), loc2N = new LinkedList<>(), loc3N = new LinkedList<>();
        Location location1 = new Location("test 1", 10, 10, loc1N, "", 0, 1, "", ""),
                 location2 = new Location("test 2", 100, 100, loc2N, "", 0, 2, "", ""),
                 location3 =new Location("test 3", 250, 100, loc3N, "", 0, 3, "", "");
        loc1N.add(2);
        loc2N.add(3);
        loc3N.add(1);
        locations.add(location1);
        locations.add(location2);
        locations.add(location3);
        astar = new Astar(locations);
        LinkedList<Location> output = astar.run(location1, location3);
        List<Location> locs = new ArrayList<>();
        locs.addAll(output);

        //Draw locations
        for (int i = 0; i < locs.size() - 1; i++)
        {
            Location loc1 = locs.get(i);
            Location loc2 = locs.get(i+1);
            Line line = new Line(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
            displayedShapes.add(line);
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
