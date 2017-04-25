package groupg.controller;

import groupg.Main;
import groupg.database.Floor;
import groupg.jfx.ImageViewFactory;
import groupg.jfx.ResourceManager;
import groupg.jfx.UniqueFloor;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {

    @FXML
    private Group mapGroup;
    @FXML
    private ImageView imageViewBase;
    @FXML
    private Pane mapPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button searchButton;
    @FXML
    //private Pane dropDown;


    Scale scale = new Scale();
    //ImageView newmap = new ImageView();
    double WIDNDOW_WIDTH = 0;
    List<UniqueFloor> FaulknerFloors = new ArrayList<>();
    boolean onScreen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //anchorPane.getChildren().add(dropDown);
        //dropDown.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        anchorPane.setPickOnBounds(false);
        imageViewBase.setPickOnBounds(true);
        imageViewBase.setImage(new Image("/image/FaulknerMaps/Ground.png"));

        List<Floor> floors = Main.h.getAllFloors();
        for (int i = 0; i < floors.size(); i ++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 544+i*30, 342-i*30, 544+i*30, -600, i);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked( event -> {
                    event.consume();
                    double scaleVal = mapPane.getWidth()/uf.getImageView().getImage().getWidth();
                    scaleImage(uf.getImageView().getX(), uf.getImageView().getY(), scaleVal, 1250).play();
                });
            }
        }

        mapGroup.getTransforms().add(scale);
        System.out.println(mapPane.getWidth());
        mapPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        mapPane.setMaxWidth(anchorPane.getMaxWidth());
        mapPane.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("clicked the pane!!!!");

            resetZoom(WIDNDOW_WIDTH, 1250);
        });

        mapPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            WIDNDOW_WIDTH = (double)newValue;
            resetZoom(WIDNDOW_WIDTH, 1);

            //dropDown.setPrefWidth(80);
            //dropDown.setPrefHeight(mapPane.getHeight()*.869329);
        });

        searchButton.setOnAction(event -> {
            if (onScreen == false) {
                for (UniqueFloor uf: FaulknerFloors) {
                    moveImage(uf.getImageView(), uf.getOnX(), uf.getOnY(), 1250+uf.getTimeDelay()*100).play();
                }
                onScreen = true;
            } else {
                for (UniqueFloor uf: FaulknerFloors) {
                    moveImage(uf.getImageView(), uf.getOffX(), uf.getOffY(), 1750-uf.getTimeDelay()*100).play();
                }
                onScreen = false;
            }
        });
    }

    private Animation moveImage(ImageView image, double x, double y, double time) {

        double curX = image.getX();
        double curY = image.getY();


        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                double newX = curX+fraction*(x-curX);
                image.setX(newX);
                double newY = curY+fraction*(y-curY);
                image.setY(newY);
            }
        };

        expandPanel.setOnFinished(e-> {

        });
        return expandPanel;
    }

    private Animation scaleImage(double x, double y, double scaleIn, double time) {

        mapGroup.getTransforms().clear();
        double curScale = scale.getMxx();
        double curX = -mapGroup.getTranslateX()/curScale;
        double curY = -mapGroup.getTranslateY()/curScale;


        mapGroup.getTransforms().add(scale);
        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                mapGroup.getTransforms().clear();
                mapGroup.getTransforms().add(scale);

                double newScale = curScale + fraction*(scaleIn-curScale);

                scale.setX(newScale);
                scale.setY(newScale);

                double newX = curX+fraction*(x-curX);
                mapGroup.setTranslateX(-newX*newScale);
                double newY = curY+fraction*(y-curY);
                mapGroup.setTranslateY(-newY*newScale);
            }
        };
        expandPanel.setOnFinished(e-> {
        });

        return expandPanel;
    }


    public void resetZoom(double width, double time){
        double newScale = width / imageViewBase.getImage().getWidth();
        scaleImage(00, 00, newScale, time).play();
    }

}


