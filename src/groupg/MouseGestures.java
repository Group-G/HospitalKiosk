package groupg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class MouseGestures
{
    private static MouseGestures ourInstance = new MouseGestures();
    private static List<UniqueNode> nodes = new ArrayList<>();

    static MouseGestures getInstance()
    {
        return ourInstance;
    }

    static void setNodes(List<UniqueNode> nodeList)
    {
       nodes = nodeList;
    }

    private MouseGestures()
    {
    }

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private UniqueNode currentSelection = null;
    private double mouseX, mouseY;

    /**
     * Makes Nodes draggable with a mouse listener
     *
     * @param nodes Nodes to make draggable
     */
    void makeDraggable(UniqueNode... nodes)
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

            Menu changeType = new Menu("Change Type");
            ObservableList<MenuItem> types = FXCollections.observableArrayList();
            List<String> tempStringsFromDB = new ArrayList<>(); //TODO: Grab items from DB
            tempStringsFromDB.add("Type 1");
            tempStringsFromDB.add("Type 2");
            tempStringsFromDB.forEach(s -> {
                MenuItem item = new MenuItem(s);
                item.setOnAction(e ->
                                 {
                                     System.out.println("Changed type for node " + currentSelection.getID() + " to " + s);
                                 });
                changeType.getItems().add(item);
            });
            changeType.getItems().addAll(types);

            MenuItem changeCat = new MenuItem("Change Category");
            MenuItem remove = new MenuItem("Remove Node");

            contextMenu.getItems().addAll(changeType, changeCat, remove);

            remove.setOnAction(event1 -> {
                nodes.remove(currentSelection);
                System.out.println("Removed node with ID: " + currentSelection.getID());
            });

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

            if (t.getSource() instanceof UniqueNode)
            {
                UniqueNode p = ((UniqueNode) (t.getSource()));
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

            if (t.getSource() instanceof UniqueNode)
            {
                UniqueNode p = ((UniqueNode) (t.getSource()));
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
