package groupg.controller;

import groupg.jfx.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by dannm on 4/14/2017.
 */
public class AboutController implements Initializable {
    @FXML
    private Button cancelBtn;
    @FXML
    private ImageView wongHS, andrewHS, danielHS, hunterHS, alazarHS, williamHS, ryanHS, saulHS, dylanHS, samHS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wongHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Wong.png"));
        andrewHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Andrew.png"));
        danielHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Daniel.png"));
        hunterHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Hunter.png"));
        alazarHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Alazar.png"));
        williamHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Will.png"));
        ryanHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Ryan.png"));
        saulHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Saul.png"));
        dylanHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Dylan.png"));
        samHS.setImage(ResourceManager.getInstance().loadImageNatural("/image/Headshots/Sam.png"));
    }

    public void onCancel(ActionEvent actionEvent) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
