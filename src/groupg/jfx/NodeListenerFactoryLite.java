package groupg.jfx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class NodeListenerFactoryLite {
    private static double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    public static UniqueNode currentSelection = null;
    private static double mouseX, mouseY;

    /**
     * Makes Nodes draggable and clickable with mouse listeners
     *
     * @param nodes Nodes to make draggable
     */
    public static void attachListeners(UniqueNode... nodes) {
        Arrays.stream(nodes).forEach(node ->
        {
            node.setOnMousePressed(mousePressedHandler);

        });
    }



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

                    //--AdminMainController.drawConnections(currentSelection);
                    //WelcomeScreenController.updateNodePD();

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


    private static void updateSelection(UniqueNode node) {
        //Clear highlight
        if (currentSelection != null)
            currentSelection.setUnhighlighted();

        currentSelection = node;
        currentSelection.setHighlighted();
        //AdminMainController.drawConnections(currentSelection);
        //WelcomeScreenController.updateNodePD();
    }
}
