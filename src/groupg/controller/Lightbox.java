package groupg.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


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


        public void Closelightbox(MouseEvent event) {
            parent.getChildren().remove(BasePane);
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
