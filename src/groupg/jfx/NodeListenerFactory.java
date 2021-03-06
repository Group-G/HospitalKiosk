package groupg.jfx;

import groupg.Main;
import groupg.algorithm.NodeNeighbors;
import groupg.controller.AdminMainController;
import groupg.database.Category;
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

import java.util.Arrays;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class    NodeListenerFactory {
    private static double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    public static UniqueNode currentSelection = null;
    public static double mouseX, mouseY;

    /**
     * Makes Nodes draggable and clickable with mouse listeners
     *
     * @param nodes Nodes to make draggable
     */
    public static void attachListeners(UniqueNode... nodes) {
        Arrays.stream(nodes).forEach(node ->
                                     {
                                         node.setOnMousePressed(mousePressedHandler);
                                         node.setOnMouseDragged(mouseDraggedHandler);
                                         node.setOnContextMenuRequested(showContextMenu);
                                         node.setOnMouseMoved(trackMouseCoordinates);
                                     });
    }

    private static EventHandler<ContextMenuEvent> showContextMenu = new EventHandler<ContextMenuEvent>() {
        @Override
        public void handle(ContextMenuEvent event) {
            AdminMainController.updateNodePD();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem changeName = new MenuItem("Change Name");
            changeName.setOnAction(s -> editNodeName());

            Menu changeCat = new Menu("Change Category");
            List<Category> catsFromDB = Main.h.getAllCategories();
            catsFromDB.forEach(s ->
                               {
                                   MenuItem item = new MenuItem(s.getCategory());
                                   item.setOnAction(e -> {
                                       currentSelection.getLocation().setCategory(s);
                                       currentSelection.setHighlighted();
                                       AdminMainController.updateNodePD();
                                   });
                                   changeCat.getItems().add(item);
                               });

            MenuItem remove = new MenuItem("Remove Node");
            remove.setOnAction(event1 -> deleteCurrentSelection());

            MenuItem autogen = new MenuItem("Generate Connections");
            autogen.setOnAction(event1 -> {
                //Generate neighbors for this node
                List<Location> neighbors = NodeNeighbors.generateNeighbors(currentSelection, AdminMainController.displayedNodes);

                currentSelection.getLocation().getNeighbors().clear();
                for (Location neighbor : neighbors) {
                    currentSelection.getLocation().addNeighbor(neighbor);
                    neighbor.addNeighbor(currentSelection.getLocation());
                    Main.h.addConnection(currentSelection.getLocation().getID(), neighbor.getID());
                }

                Main.h.setLocation(currentSelection.getLocation().getID(), currentSelection.getLocation());
                AdminMainController.drawConnections(currentSelection);
                AdminMainController.updateNodePD();
            });

            contextMenu.getItems().addAll(changeName, changeCat, autogen, remove);

            contextMenu.show(currentSelection, mouseX, mouseY);
        }
    };

    private static EventHandler<MouseEvent> mousePressedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getButton() == MouseButton.PRIMARY) {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();

                if (t.getSource() instanceof UniqueNode) {
                    UniqueNode p = ((UniqueNode) (t.getSource()));
                    orgTranslateX = p.getCenterX();
                    orgTranslateY = p.getCenterY();

                    if (t.isShiftDown() && p != currentSelection) {
                        //Add to neighbors
                        currentSelection.getLocation().addNeighbor(p.getLocation());
                        p.getLocation().addNeighbor(currentSelection.getLocation());
                    } else {
                        updateSelection(p);
                    }

                    AdminMainController.drawConnections(currentSelection);
                    AdminMainController.updateNodePD();
                } else {
                    Node p = ((Node) (t.getSource()));
                    orgTranslateX = p.getTranslateX();
                    orgTranslateY = p.getTranslateY();
                }
            } else if (t.getSource() instanceof UniqueNode) {
                updateSelection((UniqueNode) t.getSource());
            }
        }
    };

    private static EventHandler<MouseEvent> mouseDraggedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getButton() == MouseButton.PRIMARY) {
                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;

                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                if (t.getSource() instanceof UniqueNode) {
                    UniqueNode p = ((UniqueNode) (t.getSource()));
                    p.setCenterX(newTranslateX);
                    p.setCenterY(newTranslateY);
                    p.getLocation().setX((int) newTranslateX);
                    p.getLocation().setY((int) newTranslateY);

                    updateSelection(p);
                } else {
                    Node p = ((Node) (t.getSource()));
                    p.setTranslateX(newTranslateX);
                    p.setTranslateY(newTranslateY);
                }
            }
        }
    };

    private static EventHandler<MouseEvent> trackMouseCoordinates = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            mouseX = event.getScreenX();
            mouseY = event.getScreenY();
        }
    };

    /**
     * Updates the current selection; redraws highlight, connections, and node PD
     *
     * @param node New selection
     */
    public static void updateSelection(UniqueNode node) {
        //Clear highlight
        if (currentSelection != null)
            currentSelection.setUnhighlighted();

        currentSelection = node;
        currentSelection.setHighlighted();
        AdminMainController.drawConnections(currentSelection);
        AdminMainController.updateNodePD();
    }

    /**
     * Edits the name of the currently selected node
     */
    public static void editNodeName() {
        TextInputDialog dialog = new TextInputDialog(currentSelection.getLocation().getName());
        dialog.setTitle("Change Name");
        dialog.setGraphic(null);
        dialog.setHeaderText("Change Name");
        dialog.setContentText("Please enter a new name:");
        dialog.showAndWait()
              .filter(result -> !result.equals(""))
              .ifPresent(result -> {
                  if (result.length() >= Main.h.maxStringLength())
                      currentSelection.getLocation().setName(result.substring(0, Main.h.maxStringLength() - 1));
                  else
                      currentSelection.getLocation().setName(result);
              });

        AdminMainController.updateNodePD();
    }

    /**
     * Deletes the currently selected node
     */
    public static void deleteCurrentSelection() {
        currentSelection.getLocation().getNeighbors().forEach(elem -> elem.getNeighbors().remove(currentSelection.getLocation()));
        Main.h.removeLocationById(currentSelection.getLocation().getID());
        AdminMainController.displayedNodes.remove(currentSelection);

        AdminMainController.updateNodePD();
        AdminMainController.lineOverlay.getChildren().clear();
    }
}
