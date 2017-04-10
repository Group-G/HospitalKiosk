package groupg.controller;

import groupg.database.Floor;
import groupg.database.HospitalData;
import groupg.jfx.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminMainController implements Initializable {
    @FXML
    private Button logoutBtn, addNodeBtn, editCatBtn, editPersBtn, editIFCBtn;
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane canvasWrapper;
    public static Pane imageViewPane, nodeOverlay, lineOverlay, infoOverlay;
    public static ObservableList<UniqueNode> displayedNodes = FXCollections.observableArrayList();
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    public static ObservableList<PropertyDisplay> displayedPanels = FXCollections.observableArrayList();
    private ImageView imageView;
    private static Floor currentFloor;
    private static Tab selectedTab;
    public static double paneScale = 1;

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

        //Default current floor to first floor available
        if (currentFloor == null)
            currentFloor = HospitalData.getAllFloors().get(0);

        imageView = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage(currentFloor.getFilename()), imageViewPane);

        //Add tabs for each floor
        HospitalData.getAllFloors().forEach(floor -> {
            Tab tab = new Tab(floor.getFloorNum());
            tab.setOnSelectionChanged(event -> {
                imageView.setImage(ResourceManager.getInstance().loadImage(floor.getFilename()));
                currentFloor = floor;

                //Set new nodes for this floor
                displayedNodes.clear();
                displayedNodes.addAll(floor.getLocations().stream().map(NodeFactory::getNode).collect(Collectors.toList()));
                nodeOverlay.getChildren().setAll(displayedNodes);

                //Clear lines
                displayedLines.clear();
                lineOverlay.getChildren().clear();

                //Clear current selection
                NodeListenerFactory.currentSelection = null;
            });
            tabPane.getTabs().add(tab);
        });
        tabPane.setPickOnBounds(false);

        //Listener for tab selection change
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> selectedTab = newTab);

        //Select previous tab
        if (selectedTab != null) {
            for (Tab child : tabPane.getTabs()) {
                if (child.getText().equals(selectedTab.getText())) {
                    tabPane.getSelectionModel().select(child);
                    break;
                }
            }
        }

        //Fill list with nodes from DB
        displayedNodes.clear();
        displayedNodes.addAll(currentFloor.getLocations().stream().map(NodeFactory::getNode).collect(Collectors.toList()));
        nodeOverlay.getChildren().setAll(displayedNodes);

        //Add node pd to list
        displayedPanels.add(new PropertyDisplay());

        //Add layers
        Group zoomGroup = new Group(imageView, nodeOverlay, lineOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setPannable(true);
        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (!(event.getButton() == MouseButton.PRIMARY && event.isControlDown()))
                event.consume();
        });
        pane.addEventFilter(ScrollEvent.ANY, event -> {
            event.consume();

            if (event.getDeltaY() == 0) {
                return;
            }

            double scaleFactor =
                    (event.getDeltaY() > 0)
                    ? 1.1
                    : 1/1.1;

            zoomGroup.setScaleX(zoomGroup.getScaleX() * scaleFactor);
            zoomGroup.setScaleY(zoomGroup.getScaleY() * scaleFactor);
            paneScale *= scaleFactor;
        });
        canvasWrapper.getChildren().addAll(pane, infoOverlay);
    }

    /**
     * Draws connections between a node and its neighbors
     *
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
        if (NodeListenerFactory.currentSelection != null) {
            //Update property display
            PropertyDisplay pd = displayedPanels.get(0);
            pd.setProperty("X value", "" + NodeListenerFactory.currentSelection.getLocation().getX());
            pd.setProperty("Y value", "" + NodeListenerFactory.currentSelection.getLocation().getY());
            pd.setProperty("Name", NodeListenerFactory.currentSelection.getLocation().getName());
            pd.setProperty("ID", "" + NodeListenerFactory.currentSelection.getLocation().getID());
            pd.setProperty("Category", NodeListenerFactory.currentSelection.getLocation().getCategory().getCategory());
            pd.setProperty("# of Neighbors", NodeListenerFactory.currentSelection.getLocation().getNeighbors().size() + "");
            displayedPanels.set(0, pd);
            AdminMainController.infoOverlay.getChildren().clear();
            AdminMainController.infoOverlay.getChildren().addAll(displayedPanels);
        }
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
        UniqueNode node = NodeFactory.getNode(imageView.getImage().widthProperty().doubleValue()/2.0,
                                              imageView.getImage().heightProperty().doubleValue()/2.0,
                                              currentFloor.getID());
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

    public void onEditIFC(ActionEvent event) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editIFC.fxml", "Edit Inter-Floor Connections", editIFCBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
