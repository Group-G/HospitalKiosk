package groupg;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Arrays;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class MouseGestures
{
    private static MouseGestures ourInstance = new MouseGestures();

    static MouseGestures getInstance()
    {
        return ourInstance;
    }

    private MouseGestures()
    {
    }

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private Circle currentSelection = null;
    private double mouseX, mouseY;

    /**
     * Makes Nodes draggable with a mouse listener
     *
     * @param nodes Nodes to make draggable
     */
    void makeDraggable(Node... nodes)
    {
        Arrays.stream(nodes).forEach(node ->
                                     {
                                         node.setOnMousePressed(mousePressedHandler);
                                         node.setOnMouseDragged(mouseDraggedHandler);
                                         node.setOnContextMenuRequested(showContextMenu);
                                         node.setOnMouseMoved(trackMouseCoordinates);
                                     });
    }

    private EventHandler<ContextMenuEvent> showContextMenu = new EventHandler<ContextMenuEvent>()
    {
        @Override
        public void handle(ContextMenuEvent event)
        {
            final ContextMenu contextMenu = new ContextMenu();
            MenuItem changeType = new MenuItem("Change Type");
            MenuItem changeCat = new MenuItem("Change Category");
            contextMenu.getItems().addAll(changeType, changeCat);
            changeType.setOnAction(event1 -> System.out.println("TYPE CHANGED"));
            changeCat.setOnAction(event1 -> System.out.println("CAT CHANGED"));
            contextMenu.show(currentSelection, mouseX, mouseY);
        }
    };

    private EventHandler<MouseEvent> trackMouseCoordinates = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            mouseX = event.getScreenX();
            mouseY = event.getScreenY();
        }
    };

    private EventHandler<MouseEvent> mousePressedHandler = new EventHandler<MouseEvent>()
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

                //Clear current highlight
                if (currentSelection != null)
                {
                    currentSelection.setFill(Color.BLACK);
                }

                //Set new highlight
                currentSelection = p;
                p.setFill(Color.RED);
            }
            else
            {
                Node p = ((Node) (t.getSource()));
                orgTranslateX = p.getTranslateX();
                orgTranslateY = p.getTranslateY();
            }
        }
    };

    private EventHandler<MouseEvent> mouseDraggedHandler = new EventHandler<MouseEvent>()
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
