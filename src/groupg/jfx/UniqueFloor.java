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
    private int timeDelay;
    private boolean onScreen;


    public UniqueFloor(Floor floor, Group mapGroup, double onX, double onY, double offX, double offY, int timeDelay){

        this.floor = floor;
        this.onX = onX;
        this.onY = onY;
        this.offX = offX;
        this.offY = offY;
        this.timeDelay = timeDelay;
        imageView.setImage(new Image(floor.getFilename()));
        imageView.setX(onX);
        imageView.setY(onY);
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
       return this.imageView;
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
