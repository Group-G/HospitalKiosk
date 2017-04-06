package groupg.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ryan on 4/3/17.
 */
public class ChangeNameDialogController implements Initializable
{
    @FXML
    private TextField nameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void onCancel(ActionEvent actionEvent)
    {

    }

    public void onConfirm(ActionEvent actionEvent)
    {

    }

    void setExistingText(String s)
    {
        nameField.setText(s);
    }
}
