package groupg.controller;

import groupg.jfx.ImageViewFactory;
import groupg.jfx.ResourceManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {

    @FXML
    private Group mapGroup;
    @FXML
    private ImageView imageViewBase;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //imageViewBase.setImage(new Image("/image/"));
        Pane imagePane = new Pane();
        //imageViewBase = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage("/image/faulkner_1_cropped.png"), imagePane);
        imageViewBase.setImage(new Image("/image/faulkner_1_cropped.png"));
        //mapGroup.getChildren().add(imagePane);
        }

    }
