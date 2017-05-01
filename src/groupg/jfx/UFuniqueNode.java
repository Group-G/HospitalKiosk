package groupg.jfx;

import com.sun.javafx.scene.control.skin.LabeledImpl;
import groupg.Main;
import groupg.database.Location;
import groupg.database.Person;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.Objects;

/**
 * Created by will on 4/28/17.
 */
public class UFuniqueNode extends Group{

    private String color;
    private Location location;
    private double nodeOffset;
    private Group nodeOverlay;
    private Circle c;


    public UFuniqueNode(double radius, Location location, double nodeOffset) {
        c = new Circle(location.getX()*nodeOffset, location.getY()*nodeOffset,radius);
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

    public void makeDialog(){
        Pane p = new Pane();
        Label name = new Label(location.getName());

        VBox vbox = new VBox();

        for(Person per : Main.h.getAllPeople()){
            for (Integer l : per.getLocations()){
                if(location.equals(Main.h.getLocationById(l))){
                    Label label = new Label(per.getName()+ ", " + per.getTitle());
                    vbox.getChildren().add(label);
                }
            }
        }

        vbox.setPrefHeight(300);
        //vbox.setScaleX(nodeOffset*2);
        //vbox.setScaleY(nodeOffset*2);
        p.setPrefHeight(300);
        //Label personel = new Label(Person Main.h.getAllPeople())
        vbox.getChildren().add(name);
        p.getChildren().add(vbox);
        p.setLayoutX(location.getX()*nodeOffset);
        p.setLayoutY(location.getY()*nodeOffset);
        p.setStyle("-fx-background-color: blueviolet;");
        this.getChildren().add(p);
    }

    public Circle getCircle(){
        return this.c;
    }
}
