package groupg.jfx;

import groupg.controller.WelcomeScreenController;
import groupg.database.Floor;
import groupg.database.Location;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 4/23/17.
 */
public class UniqueFloor {
    private final WelcomeScreenController welcomeScreenController;
    private Floor floor;
    private ImageView imageView = new ImageView();
    private double onX;
    private double onY;
    private double offX;
    private double offY;
    private int index;
    private boolean onScreen = true;
    //private static Pane nodePane = new Pane();
    private Group group = new Group();
    private List<UFuniqueNode> points;
    private Group lines = new Group();
    private double nodeOffset = 1.285;
    private List<Location> path = new ArrayList<>();


    public UniqueFloor(Floor floor, Group mapGroup, double onX, double onY, double offX, double offY, int index, WelcomeScreenController welcomeScreenController){
        this.welcomeScreenController = welcomeScreenController;
        points = new ArrayList<>();
        this.floor = floor;
        this.onX = onX;
        this.onY = onY;
        this.offX = offX;
        this.offY = offY;
        this.index = index;
        imageView.setImage(new Image(floor.getFilename()));
        group.setTranslateX(onX);
        group.setTranslateY(onY);
        if (floor.getBuildingID() == 1) {
            //ffaulker
            nodeOffset = 1.285;
        }
        if (floor.getBuildingID() == 0) {
            nodeOffset = .56;
        }
        //imageView.setX(onX);
        //imageView.setY(onY);
        for (Location l : floor.getLocations()){
            if(!l.getCategory().getCategory().equals("")  && !l.getCategory().getCategory().equals("Hall")){
                System.out.println(l.getCategory().getCategory());
                UFuniqueNode c = new UFuniqueNode(10, l, nodeOffset);

                c.getCircle().setOnMouseEntered(event -> {
                    if(!welcomeScreenController.getSearched()) {
                        displayNodeInfo(c);
                        event.consume();
                    }
                });
                points.add(c);
            }
        }
        //nodePane.getTransforms().add(new Scale(.7, .7));
        //nodePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(20))));
        //nodePane.setPickOnBounds(false);
        group.getChildren().add(imageView);
        group.getChildren().addAll(points);
        group.getChildren().add(lines);
        mapGroup.getChildren().add(group);

    }

    public void displayNodeInfo(UFuniqueNode c) {
        welcomeScreenController.unhighLightNodes();
        c.setHighlighted(true);
        welcomeScreenController.setMenuFill(c.makeDialog());
    }

    public double getNodeOffset() {
        return nodeOffset;
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

    public int getFloorIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean onScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean o) {
        this.onScreen = o;
    }

    public List<UFuniqueNode> getPoints() {
        return this.points;
    }

    public Group getLines() {
        return lines;
    }

    public void setLines(Group lines) {
        this.lines = lines;
    }

    public double nodeOffset() {
        return nodeOffset;
    }

    public List<Location> getPath() {
        return path;
    }
    public void setPath(List<Location> path) {
        this.path = path;
    }

    public void adjustPath(){
        List<Location> copies = new ArrayList<>();
        if (path.size() != 0){
            for (Location l : path){
                Location copy = l.makeCopy();
                copy.setX((int)(copy.getX()*nodeOffset));
                copy.setY((int)(copy.getY()*nodeOffset));
                copies.add(copy);
            }
            path = copies;
        }
    }

    public void unhighlightNodes() {
        for(UFuniqueNode c : points){
            c.setHighlighted(false);
        }
    }
}
