package groupg.controller;

import static groupg.Main.h;

import groupg.Main;
import groupg.jfx.ResourceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class EditCategoryAddController implements Initializable
{
    @FXML
    private Button cancelBtn, addBtn;
    @FXML
    private TextField catNameField;
    @FXML
    private ColorPicker colorField;

    @FXML
    private RadioButton radiopublic, radioprivate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    public void onCancel(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", cancelBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAdd(ActionEvent event)
    {
        if(radioprivate.isArmed()) {
            Main.h.addCategory(catNameField.getText(), 1, colorField.getValue().toString());
        }
        else if(radiopublic.isArmed()){
            Main.h.addCategory(catNameField.getText(), 0, colorField.getValue().toString());
        }
        else{
            Main.h.addCategory(catNameField.getText(), 0, colorField.getValue().toString());
        }
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", addBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
