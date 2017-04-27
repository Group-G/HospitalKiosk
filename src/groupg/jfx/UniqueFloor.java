package groupg.jfx;

import groupg.database.Floor;
import groupg.database.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 4/23/17.
 */
public class UniqueFloor {
    private Floor floor;
    private ImageView imageView = new ImageView();
    private double onX;
    private double onY;
    private double offX;
    private double offY;
    private int timeDelay;
    private boolean onScreen = true;
    //private static Pane nodePane = new Pane();
    private Group group = new Group();
    private List<Circle> points;


    public UniqueFloor(Floor floor, Group mapGroup, double onX, double onY, double offX, double offY, int timeDelay){

        points = new ArrayList<>();
        this.floor = floor;
        this.onX = onX;
        this.onY = onY;
        this.offX = offX;
        this.offY = offY;
        this.timeDelay = timeDelay;
        imageView.setImage(new Image(floor.getFilename()));
        group.setTranslateX(onX);
        group.setTranslateY(onY);
        double scale = 1.275;
        for (Location l : floor.getLocations()){
            if(!l.getCategory().getCategory().equals("")  && !l.getCategory().getCategory().equals("Hall")){
                System.out.println(l.getCategory().getCategory());
                Circle c = new Circle(l.getX()*scale, l.getY()*scale, 20);
                points.add(c);
            }
        }
        //nodePane.getTransforms().add(new Scale(.7, .7));
        //nodePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(20))));
        //nodePane.setPickOnBounds(false);
        group.getChildren().add(imageView);
        group.getChildren().addAll(points);
        mapGroup.getChildren().add(group);

    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public double getOnX() {
        return onX;
    }

    public void setOnX(double onX) {
        this.onX = onX;
    }

    public double getOnY() {
        return onY;
    }

    public void setOnY(double onY) {
        this.onY = onY;
    }

    public double getOffX() {
        return offX;
    }

    public void setOffX(double offX) {
        this.offX = offX;
    }

    public double getOffY() {
        return offY;
    }

    public void setOffY(double offY) {
        this.offY = offY;
    }

    public ImageView getImageView() {
       return this.imageView;
    }

    public Group getGroup() {
        return this.group;
    }

    public int getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public boolean onScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean o) {
        this.onScreen = o;
    }
}
