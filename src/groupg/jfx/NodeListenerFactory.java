package groupg.jfx;

import groupg.algorithm.NodeNeighbors;
import groupg.controller.AdminMainController;
import groupg.database.HospitalData;
import groupg.database.Location;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class NodeListenerFactory
{
    private static double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    public static UniqueNode currentSelection = null;
    private static double mouseX, mouseY;

    /**
     * Makes Nodes draggable and clickable with mouse listeners
     *
     * @param nodes Nodes to make draggable
     */
    public static void attachListeners(UniqueNode... nodes)
    {
        Arrays.stream(nodes).forEach(node ->
                                     {
                                         node.setOnMousePressed(mousePressedHandler);
                                         node.setOnMouseDragged(mouseDraggedHandler);
                                         node.setOnContextMenuRequested(showContextMenu);
                                         node.setOnMouseMoved(trackMouseCoordinates);
                                     });
    }

    private static EventHandler<ContextMenuEvent> showContextMenu = new EventHandler<ContextMenuEvent>()
    {
        @Override
        public void handle(ContextMenuEvent event)
        {
            ContextMenu contextMenu = new ContextMenu();

            MenuItem changeName = new MenuItem("Change Name");
            changeName.setOnAction(s ->
                                   {
                                       TextInputDialog dialog = new TextInputDialog(currentSelection.getLocation().getName());
                                       dialog.setTitle("Change Name");
                                       dialog.setGraphic(null);
                                       dialog.setHeaderText("Change Name");
                                       dialog.setContentText("Please enter a new name:");
                                       dialog.showAndWait()
                                             .filter(result -> !result.equals(""))
                                             .ifPresent(result -> currentSelection.getLocation().setName(result));
                                   });

            Menu changeCat = new Menu("Change Category");
            List<String> catsFromDB = HospitalData.getAllCategories();
            catsFromDB.forEach(s ->
                               {
                                   MenuItem item = new MenuItem(s);
                                   item.setOnAction(e -> currentSelection.getLocation().setCategory(s));
                                   changeCat.getItems().add(item);
                               });

            MenuItem remove = new MenuItem("Remove Node");
            remove.setOnAction(event1 ->
                               {
                                   HospitalData.removeLocationById(currentSelection.getLocation().getID());
                                   AdminMainController.displayedNodes.remove(currentSelection);
                               });

            MenuItem autogen = new MenuItem("Generate Connections");
            autogen.setOnAction(event1 -> {
                //Generate neighbors for this node
                List<Location> neighbors = NodeNeighbors.generateNeighbors(currentSelection, AdminMainController.displayedNodes);

                //Clear all of this node's neighbors and save new ones
                currentSelection.getLocation().setNeighbors(neighbors);

                HospitalData.setLocation(currentSelection.getLocation().getID(), currentSelection.getLocation());
                AdminMainController.drawConnections(currentSelection);
            });

            contextMenu.getItems().addAll(changeName, changeCat, autogen, remove);

            contextMenu.show(currentSelection, mouseX, mouseY);
        }
    };

    private static EventHandler<MouseEvent> trackMouseCoordinates = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            mouseX = event.getScreenX();
            mouseY = event.getScreenY();
        }
    };

    private static EventHandler<MouseEvent> mousePressedHandler = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t)
        {
            if (t.getButton() == MouseButton.PRIMARY)
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
                        currentSelection.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.3));
                    }

                    //Set new highlight
                    currentSelection = p;
                    p.setFill(Color.RED.deriveColor(1, 1, 1, 0.3));

                    //Draw new connections
                    AdminMainController.drawConnections(currentSelection);
                }
                else
                {
                    Node p = ((Node) (t.getSource()));
                    orgTranslateX = p.getTranslateX();
                    orgTranslateY = p.getTranslateY();
                }
            }
            else if (t.getSource() instanceof UniqueNode)
            {
                //Clear current highlight
                if (currentSelection != null)
                {
                    currentSelection.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.3));
                }

                //Set new highlight
                currentSelection = ((UniqueNode) (t.getSource()));
                currentSelection.setFill(Color.RED.deriveColor(1, 1, 1, 0.3));
            }
        }
    };

    private static EventHandler<MouseEvent> mouseDraggedHandler = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t)
        {
            if (t.getButton() == MouseButton.PRIMARY)
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
                    p.getLocation().setX((int) newTranslateX);
                    p.getLocation().setY((int) newTranslateY);

                    AdminMainController.drawConnections(currentSelection);
                }
                else
                {
                    Node p = ((Node) (t.getSource()));
                    p.setTranslateX(newTranslateX);
                    p.setTranslateY(newTranslateY);
                }
            }
        }
    };
}
