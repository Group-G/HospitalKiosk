package groupg.controller;

import groupg.database.Category;
import groupg.jfx.ResourceManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static groupg.Main.h;

/**
 * @author Sam Comeau
 */
public class EditCategoryEditController implements Initializable
{
    @FXML
    private Button cancelBtn, submitBtn;
    @FXML
    private TextField catNameField;
    @FXML
    private ColorPicker colorField;
    @FXML
    private CheckBox quickSearchable;
    @FXML
    private RadioButton radiopublic, radioprivate;
    @FXML
    private Text errorText;
    List<Category> quickSearch = new ArrayList<>();

    private Category category;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        errorText.setVisible(false);
        final ToggleGroup group = new ToggleGroup();
        radiopublic.setToggleGroup(group);
        radioprivate.setToggleGroup(group);
        quickSearchable.setAllowIndeterminate(false);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    //System.out.println(group.getSelectedToggle().getUserData().toString());
                }
            }
        });
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



    public void onSubmit(ActionEvent event)
    {
        if(h.checkString(catNameField.getText())){

            if (radioprivate.isSelected() && quickSearchable.isSelected()) {
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 1, colorField.getValue().toString(), 1));
            } else if (radiopublic.isSelected() && quickSearchable.isSelected()) {
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 0, colorField.getValue().toString(), 1));
            } else if (radioprivate.isSelected() && !quickSearchable.isSelected()){
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 1, colorField.getValue().toString(), 0));
            } else if (radiopublic.isSelected() && !quickSearchable.isSelected()) {
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 0, colorField.getValue().toString(), 0));
            }
            else {
                //default public
                h.setCategory(category.getCategory(), new Category(catNameField.getText(), 0, colorField.getValue().toString(), 0));
            }
            try
            {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", submitBtn.getScene());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        else{
            errorText.setText(h.getErrorMessage());
            errorText.setVisible(true);
        }
    }

    public void setCategory(Category c){
        this.category = c;
        catNameField.setText(c.getCategory());
        if(c.getPermission() == 1){
            radiopublic.setSelected(true);
        } else {
            radioprivate.setSelected(true);
        }

        if(c.getQuicksearchOn() == 1){
            quickSearchable.setSelected(true);
        } else {
            quickSearchable.setSelected(false);
        }

        colorField.setValue(Color.web(c.getColor()));

    }
}
