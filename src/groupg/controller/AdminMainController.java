package groupg.controller;

import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.jfx.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminMainController implements Initializable {
    @FXML
    private Button logoutBtn, addNodeBtn, editCatBtn, editPersBtn;

    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas(ResizableCanvas.DRAW_FLOOR_4);
    public static Pane nodeOverlay, lineOverlay, infoOverlay;
    public static ObservableList<UniqueNode> displayedNodes = FXCollections.observableArrayList();
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    public static ObservableList<PropertyDisplay> displayedPanels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Change listener for removed nodes
        displayedNodes.addListener((ListChangeListener<UniqueNode>) c -> nodeOverlay.getChildren().setAll(displayedNodes));

        nodeOverlay = new Pane();
        nodeOverlay.setPickOnBounds(false);
        lineOverlay = new Pane();
        lineOverlay.setPickOnBounds(false);
        infoOverlay = new Pane();
        infoOverlay.setPickOnBounds(false);

        //Fill list with nodes from DB
        displayedNodes.clear();
        for (Location l : HospitalData.getAllLocations())
            displayedNodes.add(NodeFactory.getNode(l));
        nodeOverlay.getChildren().setAll(displayedNodes);

        //Add node pd to list
        displayedPanels.add(new PropertyDisplay(150, 120));

        //Add layers
        canvasWrapper.getChildren().addAll(canvas, lineOverlay, nodeOverlay, infoOverlay);
    }

    /**
     * Draws connections between a node and its neighbors
     * @param node Node to draw from
     */
    public static void drawConnections(UniqueNode node) {
        //Draw lines to the neighbors
        displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesFromLocation(node.getLocation(), node.getLocation().getNeighbors()));
        lineOverlay.getChildren().setAll(displayedLines);
    }

    /**
     * Updates the node property display
     */
    public static void updateNodePD() {
        //Update property display
        PropertyDisplay pd = displayedPanels.get(0);
        pd.setProperty("X value", "" + NodeListenerFactory.currentSelection.getLocation().getX());
        pd.setProperty("Y value", "" + NodeListenerFactory.currentSelection.getLocation().getY());
        pd.setProperty("Name", NodeListenerFactory.currentSelection.getLocation().getName());
        pd.setProperty("Category", NodeListenerFactory.currentSelection.getLocation().getCategory().getCategory());
        pd.setProperty("# of Neighbors", NodeListenerFactory.currentSelection.getLocation().getNeighbors().size() + "");
        displayedPanels.set(0, pd);
        AdminMainController.infoOverlay.getChildren().clear();
        AdminMainController.infoOverlay.getChildren().addAll(displayedPanels);
    }

    public void onLogout(ActionEvent actionEvent) {
        HospitalData.publishDB(); //Save changes to disk

        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", logoutBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddNode(ActionEvent actionEvent) {
        UniqueNode node = NodeFactory.getNode(300, 300);
        HospitalData.setLocation(node.getLocation().getID(), node.getLocation());
        displayedNodes.add(node);
        nodeOverlay.getChildren().setAll(displayedNodes);
        updateNodePD();
    }

    public void onEditCat(ActionEvent event) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", editCatBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditPers(ActionEvent event) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editPers.fxml", "Edit Personnel", editCatBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
