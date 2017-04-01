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
    static final int EDIT_NODES_CANVAS = 1;

    private final int id;
    private final Canvas canvas = new Canvas();

    ResizableCanvas(int id)
    {
        this.id = id;
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
                case EDIT_NODES_CANVAS:
                    gc.drawImage(new Image("/faulkner_1.png", w, h, false, false), 0, 0);
                    break;
            }
        }
    }
}
