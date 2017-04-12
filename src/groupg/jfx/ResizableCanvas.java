package groupg.jfx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class ResizableCanvas extends Pane {
    public static final int DRAW_FLOOR_1 = 1,
            DRAW_FLOOR_2 = 2,
            DRAW_FLOOR_3 = 3,
            DRAW_FLOOR_4 = 4,
            DRAW_FLOOR_5 = 5,
            DRAW_FLOOR_6 = 6,
            DRAW_FLOOR_7 = 7;

    private int ID = DRAW_FLOOR_1;
    private final Canvas canvas = new Canvas();

    private double mouseX, mouseY;

    public ResizableCanvas() {
        canvas.setOnMouseMoved(event ->
                               {
                                   mouseX = event.getX();
                                   mouseY = event.getY();
                               });
        getChildren().add(canvas);
    }



    @Override
    protected void layoutChildren() {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int w = (int) getWidth() - left - right;
        final int h = (int) getHeight() - top - bottom;

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);

        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            redrawBackground();
        }
    }

    public void redrawBackground() {
        double w = getWidth(), h = getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);

        switch (ID) {
            case DRAW_FLOOR_1:
                gc.drawImage(new Image("/image/faulkner_1_cropped.png", w, h, true, true), 0, 0);
                break;

            case DRAW_FLOOR_2:
                gc.drawImage(new Image("/image/faulkner_2_cropped.png", w, h, true, true), 0, 0);
                break;

            case DRAW_FLOOR_3:
                gc.drawImage(new Image("/image/faulkner_3_cropped.png", w, h, true, true), 0, 0);
                break;

            case DRAW_FLOOR_4:
                gc.drawImage(new Image("/image/faulkner_4_cropped.png", w, h, true, true), 0, 0);
                break;

            case DRAW_FLOOR_5:
                gc.drawImage(new Image("/image/faulkner_5_cropped.png", w, h, true, true), 0, 0);
                break;

            case DRAW_FLOOR_6:
                gc.drawImage(new Image("/image/faulkner_6_cropped.png", w, h, true, true), 0, 0);
                break;

            case DRAW_FLOOR_7:
                gc.drawImage(new Image("/image/faulkner_7_cropped.png", w, h, true, true), 0, 0);
                break;
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
}
