package groupg.jfx;

import groupg.database.Floor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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


    public UniqueFloor(Floor floor, Group mapGroup, double onX, double onY, double offX, double offY){

        this.floor = floor;
        this.onX = onX;
        this.onY = onY;
        this.offX = offX;
        this.offY = offY;
        imageView.setImage(new Image(floor.getFilename()));
        imageView.setX(offX);
        imageView.setY(offY);
        mapGroup.getChildren().add(imageView);
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
        return imageView;
    }
}
