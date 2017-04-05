
package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class DirectionScreenController implements Initializable
{
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button cancelBtn, searchBtn;
    @FXML
    private ListView<String> locList;
    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.DRAW_FLOOR_4);
    private Pane overlay = new Pane();
    private static ObservableList<Shape> displayedShapes = FXCollections.observableArrayList();

    private AutoCompleteTextField<Location> startLocField, destField;

    private Astar astar;
    private LinkedList<Location> locations = new LinkedList<>();

    public DirectionScreenController()
    {
        startLocField = new AutoCompleteTextField<>();
        startLocField.setCurrentSelection(new EmptyLocation());
        destField = new AutoCompleteTextField<>();
        destField.setCurrentSelection(new EmptyLocation());

        List<Location> kioskLocs = HospitalData.getLocationsByCategory("Kiosk");
        if (kioskLocs.size() > 0)
        {
            startLocField.setCurrentSelection(kioskLocs.get(0));
            startLocField.setText(kioskLocs.get(0).getName());
        }
    }

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

        //Add locations from DB
        locations.addAll(HospitalData.getAllLocations());
        locations.forEach(elem -> System.out.println(elem.getNeighbors().size()));
        startLocField.getEntries().addAll(locations);

        drawPath();
    }

    private void drawPath()
    {
        if (startLocField.getCurrentSelection() != null && destField.getCurrentSelection() != null)
        {
            LinkedList<Location> locsIn = new LinkedList<>();
            locsIn.addAll(HospitalData.getAllLocations());

            astar = new Astar(locsIn);

            List<Location> output = new ArrayList<>();
            output.addAll(astar.run(startLocField.getCurrentSelection(), destField.getCurrentSelection()));

            displayedShapes.clear();
            DrawLines.drawLinesInOrder(output, displayedShapes);
        }
    }

    void setDestination(Location destination)
    {
        destField.setCurrentSelection(destination);
        destField.setText(destination.getName());
        destField.getEntries().addAll(locations);

        ObservableList<Node> children = FXCollections.observableArrayList(toolBar.getItems());
        children.addAll(startLocField, destField);
        Collections.swap(children, 3, 4);
        toolBar.getItems().setAll(children);
    }

    private void generateTextDirections(LinkedList<Location> locations)
    {
        //Write directions to locList
    }

    public void onCancel(ActionEvent actionEvent)
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
        drawPath();
    }
}
