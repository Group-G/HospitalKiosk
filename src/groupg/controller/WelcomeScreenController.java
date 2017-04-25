package groupg.controller;

import groupg.Main;
import groupg.database.Floor;
import groupg.jfx.UniqueFloor;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    Scale scale = new Scale();
    ImageView newmap = new ImageView();
    double WIDNDOW_WIDTH = 0;
    List<UniqueFloor> FaulknerFloors = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //imageViewBase.setImage(new Image("/image/"));
        //Pane imagePane = new Pane();
        //imageViewBase = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage("/image/faulkner_1_cropped.png"), imagePane);
        anchorPane.setPickOnBounds(false);
        imageViewBase.setPickOnBounds(true);
        //imageViewBase.fitWidthProperty().bind(mapPane.widthProperty());
        imageViewBase.setImage(new Image("/image/FaulknerMaps/Ground.png"));

        List<Floor> floors = Main.h.getAllFloors();
        for (int i = 0; i < floors.size(); i ++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 544+i*7, 342+i*15, 544+i*15, -600);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked(event -> {
                    event.consume();

                    double scaleVal = mapPane.getWidth()/uf.getImageView().getImage().getWidth();
                    scaleImage(uf.getImageView().getX(), uf.getImageView().getY(), scaleVal).play();

                });
            }
        }

        newmap.setImage(new Image("/image/FaulknerMaps/Faulkner1.png"));
        newmap.setX(-544);
        newmap.setY(342);
        //imageViewBase.setViewport(new Rectangle2D(0, 0, 1000, 1000));
        //scale.setX(.15);
        //scale.setY(.15);
        //mapGroup.setTranslateX(-800);
        mapGroup.getChildren().add(newmap);
        mapGroup.getTransforms().add(scale);
        System.out.println(mapPane.getWidth());
        mapPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        mapPane.setMaxWidth(anchorPane.getMaxWidth());
        mapPane.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("clicked the pane!!!!");

            resetZoom(WIDNDOW_WIDTH);
            //moveImage(newmap, -800, 0).play();


            //System.out.println(mapPane.getWidth());
            //System.out.println(imageViewBase.getWidth());
        });

        mapPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            WIDNDOW_WIDTH = (double)newValue;
            resetZoom(WIDNDOW_WIDTH);
            System.out.println("scale.getMxx() = " + scale.getMxx());
        });

        searchButton.setOnAction(event -> {
            for (int i = 0; i < FaulknerFloors.size(); i ++) {
                moveImage(FaulknerFloors.get(i).getImageView(), FaulknerFloors.get(i).getOnX(), FaulknerFloors.get(i).getOnY()).play();
            }
        });

        newmap.setOnMouseClicked(event -> {
            event.consume();

            double scaleVal = mapPane.getWidth()/newmap.getImage().getWidth();
            scaleImage(newmap.getX(), newmap.getY(), scaleVal).play();

        });



        //mapGroup.getChildren().add(imagePane);

    }

    private Animation moveImage(ImageView image, double x, double y) {

        double curX = image.getX();
        double curY = image.getY();


        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(2500));
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

    private Animation scaleImage(double x, double y, double scaleIn) {

//        if(scale.getMxx() == scaleIn){
//            System.out.println("dont transform");
//            return new Transition() {
//                @Override
//                protected void interpolate(double frac) {
//
//                }
//            };
//        }

        mapGroup.getTransforms().clear();
        double curScale = scale.getMxx();
        double curX = -mapGroup.getTranslateX()/curScale;
        double curY = -mapGroup.getTranslateY()/curScale;





        System.out.println("Starts at:");
        System.out.println("scaleIn = " + curScale);
        System.out.println("curX = " + curX);
        System.out.println("curY = " + curY);
        System.out.println("x = " + x*curScale);
        System.out.println("y = " + y*curScale);
        System.out.println();

        mapGroup.getTransforms().add(scale);
        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(2500));
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

//                System.out.println("x = " + x);
//                System.out.println("curX = " + curX);
            }
        };

        expandPanel.setOnFinished(e-> {
            System.out.println("Ends at:");
            System.out.println("scaleIn = " + scale.getMxx());
            System.out.println("curX = " + -mapGroup.getTranslateX());
            System.out.println("curY = " + -mapGroup.getTranslateY());
            System.out.println();
        });
        return expandPanel;
    }


    public void resetZoom(double width){
        double newScale = width / imageViewBase.getImage().getWidth();
        scaleImage(00, 00, newScale).play();
    }

}


