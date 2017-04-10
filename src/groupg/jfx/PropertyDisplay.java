package groupg.jfx;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-06
 */
public class PropertyDisplay extends Pane {
    private List<String> keys, vals;
    private String longestVal;
    private Rectangle window;
    private StackPane pane;
    private VBox textVBox;
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;

    public PropertyDisplay() {
        longestVal = "";
        window = new Rectangle(10, 10, Color.GRAY.deriveColor(1, 1, 1, 0.7));
        window.setStroke(Color.BLACK);
        keys = new ArrayList<>();
        vals = new ArrayList<>();
        pane = new StackPane();
        setPickOnBounds(false);
        textVBox = new VBox(5);
        pane.getChildren().add(textVBox);

        //Listeners
        setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                orgSceneX = event.getSceneX();
                orgSceneY = event.getSceneY();

                if (event.getSource() instanceof PropertyDisplay) {
                    PropertyDisplay pd = (PropertyDisplay) event.getSource();
                    orgTranslateX = pd.getTranslateX();
                    orgTranslateY = pd.getTranslateY();
                }
            }
        });

        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double offsetX = event.getSceneX() - orgSceneX,
                        offsetY = event.getSceneY() - orgSceneY;

                double newTranslateX = orgTranslateX + offsetX,
                        newTranslateY = orgTranslateY + offsetY;

                if (event.getSource() instanceof PropertyDisplay) {
                    PropertyDisplay pd = (PropertyDisplay) event.getSource();
                    pd.setTranslateX(newTranslateX);
                    pd.setTranslateY(newTranslateY);
                }
            }
        });
    }

    public void setProperty(String key, String val) {
        if (keys.contains(key)) {
            vals.set(keys.indexOf(key), val);
            if (val.length() > longestVal.length())
                longestVal = val;
        } else {
            keys.add(key);
            vals.add(val);
            if (val.length() > longestVal.length())
                longestVal = val;
        }
        window.setHeight(22 * keys.size());
        window.setWidth(longestVal.length() * 25);
        updateDisplay();
    }

    public void removeProperty(String key) {
        if (keys.contains(key)) {
            vals.remove(keys.indexOf(key));
            keys.remove(key);
        }
        updateDisplay();
    }

    private void updateDisplay() {
        textVBox.getChildren().clear();
        keys.forEach(elem -> {
            Text text = new Text(elem + ": " + vals.get(keys.indexOf(elem)));
            text.setFont(Font.font(null, FontWeight.BOLD, 14));
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.setFill(Color.WHITE);
            text.setStrokeWidth(0.1);
            text.setStroke(Color.BLACK);
            text.setTranslateX(4);
            textVBox.getChildren().add(text);
        });
        if (textVBox.getWidth() > pane.getWidth()) {
            pane.setPrefWidth(textVBox.getWidth() + 10);
        }
        getChildren().setAll(window, pane);
    }

    public List<String> getPropertyNames() {
        return keys;
    }
}
