package groupg;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class NodeListenerFactory
{
    private static double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    private static UniqueNode currentSelection = null;
    private static double mouseX, mouseY;

    /**
     * Makes Nodes draggable and clickable with a mouse listener
     *
     * @param nodes Nodes to make draggable
     */
    static void makeDraggable(UniqueNode... nodes)
    {
        Arrays.stream(nodes).forEach(node ->
                                     {
                                         node.setOnMousePressed(mousePressedHandler);
                                         node.setOnMouseDragged(mouseDraggedHandler);
                                         node.setOnMouseReleased(event -> {
                                             try
                                             {
                                                 HospitalData.getAllLocations().stream()
                                                             .filter(elem -> elem.getID() == currentSelection.getLocation().getID())
                                                             .collect(Collectors.toList())
                                                             .set(0, currentSelection.getLocation());
                                             }
                                             catch (IndexOutOfBoundsException e)
                                             {
                                                 System.out.println("DID NOT FIND LOC IN DB FOR ID: " + currentSelection.getLocation().getID());
                                             }
                                             AdminMainController.drawConnections(currentSelection, AdminMainController.displayedShapes);
                                         });
                                         node.setOnContextMenuRequested(showContextMenu);
                                         node.setOnMouseMoved(trackMouseCoordinates);
                                     });
    }

    private static EventHandler<ContextMenuEvent> showContextMenu = new EventHandler<ContextMenuEvent>()
    {
        @Override
        public void handle(ContextMenuEvent event)
        {
            final ContextMenu contextMenu = new ContextMenu();

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
                                   item.setOnAction(e -> currentSelection.setCategory(s));
                                   changeCat.getItems().add(item);
                               });

//            MenuItem selectNeighbors = new MenuItem("Select Neighbors");
//            selectNeighbors.setOnAction(s ->
//                                        {
//                                            try
//                                            {
//                                                ResourceManager.getInstance().loadFXMLIntoDialog("/selectNeighbors.fxml", "Select Neighbors", currentSelection.getScene(), 150, 300);
//                                            }
//                                            catch (IOException e)
//                                            {
//                                                e.printStackTrace();
//                                            }
//                                        });

            MenuItem remove = new MenuItem("Remove Node");
            remove.setOnAction(event1 ->
                               {
                                   HospitalData.removeLocationById(currentSelection.getLocation().getID());
                                   AdminMainController.displayedShapes.remove(currentSelection);
                               });

            contextMenu.getItems().addAll(changeCat, changeName, remove);

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

    private static EventHandler<MouseEvent> mouseDraggedHandler = new EventHandler<MouseEvent>()
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
                p.getLocation().setX((int) newTranslateX);
                p.getLocation().setY((int) newTranslateY);
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
