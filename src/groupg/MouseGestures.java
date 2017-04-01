package groupg;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.util.Arrays;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class MouseGestures
{
    private static MouseGestures ourInstance = new MouseGestures();

    public static MouseGestures getInstance()
    {
        return ourInstance;
    }

    private MouseGestures()
    {
    }

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;

    /**
     * Makes Nodes draggable with a mouse listener
     * @param nodes Nodes to make draggable
     */
    void makeDraggable(Node... nodes)
    {
        Arrays.stream(nodes).forEach(node -> {
            node.setOnMousePressed(circleOnMousePressedEventHandler);
            node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        });
    }

    private EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>()
    {

        @Override
        public void handle(MouseEvent t)
        {

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            if (t.getSource() instanceof Circle)
            {

                Circle p = ((Circle) (t.getSource()));

                orgTranslateX = p.getCenterX();
                orgTranslateY = p.getCenterY();

            }
            else
            {

                Node p = ((Node) (t.getSource()));

                orgTranslateX = p.getTranslateX();
                orgTranslateY = p.getTranslateY();

            }
        }
    };

    private EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t)
        {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            if (t.getSource() instanceof Circle)
            {
                Circle p = ((Circle) (t.getSource()));
                p.setCenterX(newTranslateX);
                p.setCenterY(newTranslateY);
            }
            else
            {
                Node p = ((Node) (t.getSource()));
                p.setTranslateX(newTranslateX);
                p.setTranslateY(newTranslateY);
            }
        }
    };
}
