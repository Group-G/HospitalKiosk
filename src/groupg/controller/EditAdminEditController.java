package groupg.controller;

import groupg.Main;
import groupg.database.Admin;
import groupg.jfx.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static groupg.Main.h;

/**
 * @author Sam Comeau
 * NOT FUNCTIONAL OR IMPLEMENTED YET
 * @since 2017-04-18
 */
public class EditAdminEditController implements Initializable
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private RadioButton perm0, perm1;

    private Admin a;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        usernameField.setText("");
    }

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {
        if(usernameField.getText().equals("")){
//throw error
        }
        if(passwordField.getText().equals("")){
//throw error
        }
        if(perm0.isSelected()) {
            if(h.getCheckUsername(usernameField.getText())){
                //throw error already exists
            } else {
                Main.h.addAdmin(new Admin(usernameField.getText(), passwordField.getText(), 0));
            }
        }
        else if(perm1.isSelected()){
            if(h.getCheckUsername(usernameField.getText())){
                //throw error already exists
            } else {
                Main.h.addAdmin(new Admin(usernameField.getText(), passwordField.getText(), 1));
            }
        }
        else{
            //default less permissions
            if(h.getCheckUsername(usernameField.getText())){
                //throw error already exists
            } else {
                Main.h.addAdmin(new Admin(usernameField.getText(), passwordField.getText(), 0));
            }
        }



        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", addBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    //not going to be implemented couldn't decide whether to show the admin password or not
    public void setAdmin(Admin a){
        this.a = a;
        usernameField.setText(a.getUsername());

    }
}
