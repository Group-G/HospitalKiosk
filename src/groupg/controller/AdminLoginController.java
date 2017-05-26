package groupg.controller;

import groupg.Main;
import groupg.database.Admin;
import groupg.jfx.ResourceManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static groupg.Main.h;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminLoginController implements Initializable {
    @FXML
    private Button cancelBtn, loginBtn;
    @FXML
    private TextField usernameField, passField;
    @FXML
    private Text errorText;
    @FXML
    private AnchorPane time;
    private Timer inactiveTimeOut;
    private int cursorX;
    private int cursorY;
    private int seconds = 10;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorText.setVisible(false);
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (!isNowMoving) {
                //System.out.println("Mouse stopped!");
            }
        });
        pause.setOnFinished(e -> onLogout());
        time.setOnMouseClicked(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseDragged(e ->{
            mouseMoving.set(true);
            // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        time.setOnMouseMoved(e -> {
            mouseMoving.set(true);
            pause.playFromStart();
            // System.out.println("Mouse move!");
        });
        //Application.setUserAgentStylesheet(getClass().getResource("").toExternalForm());
        /*
        cancelBtn.getScene().setOnMouseMoved(event -> {
            System.out.println("mouse event triggered");
            inactiveTimeOut.cancel();
            isInactive();
        });

        cancelBtn.getScene().setOnKeyPressed(event -> {
            System.out.println("key event triggered");
            inactiveTimeOut.cancel();
            isInactive();
        });

        isInactive();
        */

        cancelBtn.setOnAction(event -> {
            System.out.println("mouse event triggered");
            try {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    public void onCancel(ActionEvent actionEvent) {
        try {
            h.setCurrentAdmin(new Admin("", "", 99));
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onLogin(ActionEvent actionEvent) {
        attemptLogin(actionEvent);
    }

    public void onEnterKey(ActionEvent actionEvent) {
        attemptLogin(actionEvent);
    }

    private void attemptLogin(ActionEvent actionEvent) {
        if(!h.getCheckUsername(usernameField.getText())){
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);
        } else if(!h.checkEmptyString(passField.getText().trim())){
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);
        } else if(!h.checkEmptyString(usernameField.getText().trim())){
           // System.out.println("err");
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);

        } else {
            Admin admin = Main.h.getAdminByUsername(usernameField.getText());
            BigInteger m = new BigInteger(passField.getText().getBytes());
            BigInteger hashed = m.modPow(h.key.publicKey, h.key.modulus);

            h.setCurrentAdmin(admin);

            if (admin != null && admin.login(hashed)) {
                errorText.setVisible(false);
                try {
                    if(admin.getType().equals("Admin")){
                        ResourceManager.getInstance().loadFXMLIntoScene("/view/adminMain.fxml", "Admin Main", cancelBtn.getScene());
                       // System.out.println("Logged into User");
                    }
                    else{
                        WelcomeScreenController.setPermission(1);
                        ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Admin Main", cancelBtn.getScene());
                       // System.out.println("Logged into User");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                errorText.setText("Invalid password.");
                errorText.setVisible(true);
            }
        }


    }
    public void onLogout() {
        mouseMoving.set(false);
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml","Welcome",cancelBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
