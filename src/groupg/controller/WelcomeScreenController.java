
package groupg.controller;

import groupg.algorithm.Astar;
import groupg.database.EmptyLocation;
import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.jfx.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
public class WelcomeScreenController implements Initializable {
    @FXML
    private Button loginBtn, searchBtn;
    @FXML
    private HBox startFieldHBox, endFieldHBox;
    @FXML
    private TextArea dirList;
    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas();
    public static Pane imageViewPane, lineOverlay;
    private ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    private Location closestLocToClick;

    private AutoCompleteTextField startField, endField;

    private Astar astar;
    private LinkedList<Location> locations = new LinkedList<>();

    public WelcomeScreenController() {
        startField = new AutoCompleteTextField();
        startField.setCurrentSelection(new EmptyLocation());
        endField = new AutoCompleteTextField();
        endField.setCurrentSelection(new EmptyLocation());

        List<Location> kioskLocs = HospitalData.getLocationsByCategory("Kiosk");
        if (kioskLocs.size() > 0) {
            startField.setCurrentSelection(kioskLocs.get(0));
            startField.setText(kioskLocs.get(0).getName());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageViewPane = new Pane();
        imageViewPane.setPickOnBounds(true);
        lineOverlay = new Pane();
        lineOverlay.setPickOnBounds(true);

        startFieldHBox.getChildren().add(startField);
        endFieldHBox.getChildren().add(endField);

        //Find closest location
        imageViewPane.setOnMouseClicked(event -> {
            double shortest = Double.MAX_VALUE;
            for (Location l : HospitalData.getAllLocations()) {
                if (closestLocToClick == null) {
                    closestLocToClick = l;
                }
                else {
                    Double newShortest = l.lengthTo(new EmptyLocation(event.getX(), event.getY()));
                    if (newShortest < shortest) {
                        shortest = newShortest;
                        closestLocToClick = l;
                    }
                }
            }
        });

        ImageView imageView = ImageViewFactory.getImageView(new Image("/image/faulkner_4.png", 2265, 1290, true, true), imageViewPane);
        Group zoomGroup = new Group(imageView, lineOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setPannable(true);
        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE)
                event.consume();
        });
        canvasWrapper.getChildren().addAll(pane);

//        canvasWrapper.add(canvas, 0, 0);
//        canvasWrapper.add(overlay, 0, 0);

        //Add locations from DB
        locations.addAll(HospitalData.getAllLocations());
        locations.forEach(elem -> System.out.println(elem.getName() + "has" + elem.getNeighbors().size() + "neighbors"));
        startField.getEntries().addAll(locations);
        endField.getEntries().addAll(locations);

        drawPath();
    }

    private void drawPath() {
        if (startField.getCurrentSelection() != null && endField.getCurrentSelection() != null) {
            LinkedList<Location> locsIn = new LinkedList<>();
            locsIn.addAll(HospitalData.getAllLocations());

            astar = new Astar(locsIn);

            List<Location> output = new ArrayList<>();
            output.addAll(astar.run(startField.getCurrentSelection(), endField.getCurrentSelection()));
            displayedLines.clear();
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(output));
            lineOverlay.getChildren().setAll(displayedLines);
        }
    }

    private void generateTextDirections(LinkedList<Location> locations) {
        //Write directions to locList
    }

    public void onLogin(ActionEvent actionEvent) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminLogin.fxml", "Login", loginBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSearch(ActionEvent actionEvent) {
        drawPath();
    }

    public void onFloor1(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_1)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_1);
            canvas.redrawBackground();
        }
    }

    public void onFloor2(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_2)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_2);
            canvas.redrawBackground();
        }
    }

    public void onFloor3(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_3)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_3);
            canvas.redrawBackground();
        }
    }

    public void onFloor4(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_4)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_4);
            canvas.redrawBackground();
        }
    }

    public void onFloor5(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_5)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_5);
            canvas.redrawBackground();
        }
    }

    public void onFloor6(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_6)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_6);
            canvas.redrawBackground();
        }
    }

    public void onFloor7(Event event) {
        if (canvas.getID() != ResizableCanvas.DRAW_FLOOR_7)
        {
            canvas.setID(ResizableCanvas.DRAW_FLOOR_7);
            canvas.redrawBackground();
        }
    }
}
