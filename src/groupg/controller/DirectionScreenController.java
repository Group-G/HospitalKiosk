
package groupg.controller;

import groupg.algorithm.Astar;
import groupg.database.EmptyLocation;
import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.jfx.AutoCompleteTextField;
import groupg.jfx.DrawLines;
import groupg.jfx.ResizableCanvas;
import groupg.jfx.ResourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    private ObservableList<Shape> displayedShapes = FXCollections.observableArrayList();
    private Location closestLocToClick;

    private AutoCompleteTextField startLocField, destField;

    private Astar astar;
    private LinkedList<Location> locations = new LinkedList<>();

    public DirectionScreenController(Location destination)
    {
        startLocField = new AutoCompleteTextField();
        startLocField.setCurrentSelection(new EmptyLocation());
        destField = new AutoCompleteTextField();
        destField.setCurrentSelection(new EmptyLocation());

        destField.setCurrentSelection(destination);
        destField.setText(destination.getName());

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

        ObservableList<Node> children = FXCollections.observableArrayList(toolBar.getItems());
        children.addAll(startLocField, destField);
        Collections.swap(children, 3, 4);
        toolBar.getItems().setAll(children);

        //Find closest location
        overlay.setOnMouseClicked(event -> {
            double shortest = Double.MAX_VALUE;
            for (Location l : HospitalData.getAllLocations())
            {
                if (closestLocToClick == null)
                {
                    closestLocToClick = l;
                }
                else
                {
                    Double newShortest = l.lengthTo(new EmptyLocation(event.getX(), event.getY()));
                    if (newShortest < shortest)
                    {
                        shortest = newShortest;
                        closestLocToClick = l;
                    }
                }
            }
        });

        canvasWrapper.add(canvas, 0, 0);
        canvasWrapper.add(overlay, 0, 0);

        //Add locations from DB
        locations.addAll(HospitalData.getAllLocations());
        locations.forEach(elem -> System.out.println(elem.getNeighbors().size()));
        startLocField.getEntries().addAll(locations);
        destField.getEntries().addAll(locations);

        cancelBtn.setOnAction(event -> {
            try
            {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        searchBtn.setOnAction(event -> drawPath());

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

    private void generateTextDirections(LinkedList<Location> locations)
    {
        //Write directions to locList
    }
}
