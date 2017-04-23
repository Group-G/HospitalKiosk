package groupg.controller;

import groupg.jfx.ImageViewFactory;
import groupg.jfx.ResourceManager;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.EventListener;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //imageViewBase.setImage(new Image("/image/"));
        //Pane imagePane = new Pane();
        //imageViewBase = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage("/image/faulkner_1_cropped.png"), imagePane);
        anchorPane.setPickOnBounds(false);
        imageViewBase.setPickOnBounds(true);
        //imageViewBase.fitWidthProperty().bind(mapPane.widthProperty());
        imageViewBase.setImage(new Image("/image/FaulknerMaps/Ground.png"));
        ImageView newmap = new ImageView();
        newmap.setImage(new Image(("/image/faulknerMaps/Faulkner1.png")));
        newmap.setX(-544);
        newmap.setY(342);
        //imageViewBase.setViewport(new Rectangle2D(0, 0, 1000, 1000));
        Scale scale = new Scale();
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

            moveImage(newmap, 544, 342).play();
            moveImage(newmap, -800, 0).play();


            //System.out.println(mapPane.getWidth());
            //System.out.println(imageViewBase.getWidth());
        });

        mapPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            scale.setX((double)newValue / imageViewBase.getImage().getWidth());
            scale.setY((double)newValue / imageViewBase.getImage().getWidth());
            //newmap.setX(940/(1+scale.getMxx()));
            //newmap.setY(593/(1+scale.getMxx()));
            System.out.println("scale.getMxx() = " + scale.getMxx());
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
}
