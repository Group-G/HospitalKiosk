package groupg.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by dannm on 4/25/2017.
 */
public class Lightbox {
    @FXML
    private AnchorPane BasePane; // FXML のルート要素
    private AnchorPane parent; // 親のルート要素

    @FXML
    public ImageView qrcodelightbox;
    @FXML
    public Text savetxt;

        public void Closelightbox(MouseEvent event) {
            parent.getChildren().remove(BasePane);
        }
    public void Closelightbox() {
        parent.getChildren().remove(BasePane);
    }

    void showin(AnchorPane parent) {
        this.parent = parent;

        BasePane.prefHeightProperty().bind(parent.heightProperty());
        BasePane.prefWidthProperty().bind(parent.widthProperty());
        //qrcodelightbox.setImage(qrcode.getImage());
        savetxt.setText("Changes have been Saved");
        savetxt.setFont(Font.font ("Verdana", 20));
        savetxt.setFill(Color.WHITE);
        parent.getChildren().add(BasePane);

Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(2500),
                        ae -> Closelightbox()));
                timeline.play();

    }

    void showOn(AnchorPane parent,ImageView qrcode) {
        this.parent = parent;
        // 親のルートとサイズを合わせておく
        BasePane.prefHeightProperty().bind(parent.heightProperty());
        BasePane.prefWidthProperty().bind(parent.widthProperty());
        qrcodelightbox.setImage(qrcode.getImage());
        parent.getChildren().add(BasePane);
    }
}
