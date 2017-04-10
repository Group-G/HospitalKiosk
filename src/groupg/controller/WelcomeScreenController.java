
package groupg.controller;

import groupg.algorithm.Astar;
import groupg.database.Category;
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
    private Pane lineOverlay;
    private ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    private Location closestLocToClick;
    private ImageView imageView;

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
        Pane imageViewPane = new Pane();
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

        imageView = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage("/image/faulkner_1.png"), imageViewPane);
        Group zoomGroup = new Group(imageView, lineOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setPannable(true);
        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE &&
                !(event.getButton() == MouseButton.PRIMARY && event.isControlDown()))
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


    public static void testTextDirections(){
        LinkedList<Location> locations = new LinkedList<Location>();
        String name = "a";
        double x = 50.0;
        double y = 50.0;
        int permission = 0;
        String category = "Bathroom";
        Category a = new Category(category, permission);
        float b = (float) 2.0;
        int id = 1;
        int floor = 1;
        int building = 1;
        Location c = new Location(name, x, y, locations, a, b, id, floor, building);
        locations.add(c);
        String name2 = "b";
        double x2 = 77.0;
        double y2 = 119.0;
        int permission2 = 0;
        String category2 = "Bathroom";
        Category a2 = new Category(category2, permission2);
        float b2 = (float) 2.0;
        int id2 = 1;
        int floor2 = 1;
        int building2 = 1;
        Location c2 = new Location(name2, x2, y2, locations, a2, b2, id2,floor2, building2);
        locations.add(c2);
        generateTextDirections(locations);
    }



    public static void generateTextDirections(LinkedList<Location> locations) {
        LinkedList<Double> angles = new LinkedList<>();
        for(int i = 0; i < locations.size()-1; i++) {
            double lengthBetweenXs = Math.abs(locations.get(i).getX() - locations.get(i+1).getX());
            double lengthBetweenYs = Math.abs(locations.get(i).getY() - locations.get(i+1).getY());
            double angle = Math.atan2(lengthBetweenXs, lengthBetweenYs);
            angles.add(angle);
            System.out.print(angle);
            System.out.print(angles);
        }
        for(Double angle1: angles){
            if(angle1 < .900 && angle1 > .450){
                System.out.print("You will have to take a right turn at the next node\n");
            }
            if(angle1 < .450 && angle1 > .010){
                System.out.println("You will have to take a slight right at the next node\n");
            }
            if(angle1 > .900 && angle1 < 1.350){
                System.out.println("You will have to take a slight left at the next node\n");
            }
            if(angle1 > 1.350 && angle1 < 1.800){
                System.out.println("You will have to take a left at the next node\n");
            }
            if(angle1 == 2.700){
                System.out.println("You will have to go backwards to reach the next node\n");
            }
            if(angle1 == null)
                break;
            else
                System.out.print("You will have to keep going straight for the next node\n");

        }
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
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_1.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor2(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_2.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor3(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_3.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor4(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_4.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor5(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_5.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor6(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_6.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor7(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_7.png"));
        } catch (NullPointerException e) {
        }
    }
}
