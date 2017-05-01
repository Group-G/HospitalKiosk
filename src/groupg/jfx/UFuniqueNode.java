package groupg.jfx;

import groupg.controller.WelcomeScreenController;
import groupg.database.Location;
import groupg.database.Person;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static groupg.Main.h;

/**
 * Created by will on 4/28/17.
 */
public class UFuniqueNode extends Group {
    private final double radius;
    private String color;
    private Location location;
    private double nodeOffset;
    private Group nodeOverlay;
    private Circle c;
    private boolean highlighted;

    public UFuniqueNode(double radius, Location location, double nodeOffset) {
        this.radius = radius;
        c = new Circle(location.getX() * nodeOffset, location.getY() * nodeOffset, radius);
        this.getChildren().add(c);
        this.nodeOffset = nodeOffset;
        this.location = location;
        color = location.getCategory().getColor();
        c.setFill(Color.web(color));
        c.setStroke(Color.web("0x00000099"));
        c.setStrokeWidth(3);
    }


//    public void setUnhighlighted() {
//        color = location.getCategory().getColor();
//        color = color.substring(0, 8) + "99";
//        setFill(Color.web(color));
//        setStroke(Color.web("0x00000099"));
//        setStrokeWidth(1);
//    }
//
//    public void setHighlighted() {
//
//        color = location.getCategory().getColor();
//        color = color.substring(0, 8) + "ff";
//        setFill(Color.web(color));
//        setStroke(Color.web("0x000000"));
//        setStrokeWidth(3);
//    }

    public Location getLocation() {
        return location;
    }

//    public void setLocation(Location location) {
//        this.location = location;
//    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }

    public VBox makeDialog(WelcomeScreenController controller) {
        VBox vbox = new VBox();

        vbox.setMinHeight(200);
        vbox.setPadding(new Insets(3, 0, 0, 10));
        vbox.setSpacing(5);

        //Add buttons
        Button navTo = new Button("Go to here!");
        navTo.setOnAction(actionEvent -> controller.endField.setCurrentSelection(location));
        Button navFrom = new Button("Start from here!");
        navFrom.setOnAction(actionEvent -> controller.startField.setCurrentSelection(location));
        vbox.getChildren().addAll(navTo, navFrom);

        //Add loc name
        vbox.getChildren().add(new Label("Name: " + location.getName()));

        //Add loc cat
        vbox.getChildren().add(new Label("Category: " + location.getCategory().toString()));

        //Add people at this loc
        List<Label> people = new ArrayList<>();
        for (Person per : h.getAllPeople()) {
            for (Integer l : per.getLocations()) {
                if (location.equals(h.getLocationById(l)))
                    people.add(new Label(per.getName() + ", " + per.getTitle()));
            }
        }

        if (people.size() > 0) {
            vbox.getChildren().add(new Label("People at this location:"));
            vbox.getChildren().addAll(people);
        }

        return vbox;
    }

    public Circle getCircle() {
       // System.out.println("UFuniqueNode.getCircle");
        return c;
    }

    public void setHighlighted(boolean h) {
        this.highlighted = h;
        if (this.highlighted) {
            c.setRadius(this.radius + 10);
            c.setStrokeWidth(5);
        } else {
            c.setRadius(this.radius);
            c.setStrokeWidth(3);
        }
    }
}
