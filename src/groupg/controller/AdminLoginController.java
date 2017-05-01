package groupg.controller;

import groupg.Main;
import groupg.database.Admin;
import groupg.jfx.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    private Timer inactiveTimeOut;
    private int cursorX;
    private int cursorY;
    private int seconds = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorText.setVisible(false);
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

    private void isInactive() {
        inactiveTimeOut.schedule(new TimerTask() {
            public void run() {
               // System.out.println("time is up");
                try {
                    ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", cancelBtn.getScene());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inactiveTimeOut.cancel();
            }
        }, seconds * 1000);
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
}
