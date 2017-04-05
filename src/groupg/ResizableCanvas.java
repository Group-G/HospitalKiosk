package groupg;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class ResizableCanvas extends Pane
{
    static final int DRAW_FLOOR_4 = 1;

    private final int id;
    private final Canvas canvas = new Canvas();

    private double mouseX, mouseY;

    ResizableCanvas(int id)
    {
        this.id = id;
        canvas.setOnMouseMoved(event ->
                               {
                                   mouseX = event.getX();
                                   mouseY = event.getY();
                               });
        getChildren().add(canvas);
    }

    @Override
    protected void layoutChildren()
    {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int w = (int) getWidth() - left - right;
        final int h = (int) getHeight() - top - bottom;

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);

        if (w != canvas.getWidth() || h != canvas.getHeight())
        {
            canvas.setWidth(w);
            canvas.setHeight(h);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, w, h);

            switch (id)
            {
                case DRAW_FLOOR_4:
                    gc.drawImage(new Image("/faulkner_4_cropped.png", w, h, false, true), 0, 0);
                    break;
            }
        }
    }

    public double getMouseX()
    {
        return mouseX;
    }

    public double getMouseY()
    {
        return mouseY;
    }
}
