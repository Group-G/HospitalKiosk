package groupg.controller;

import groupg.algorithm.NavigationAlgorithm;
import groupg.database.Floor;
import groupg.jfx.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static groupg.Main.h;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminMainController implements Initializable{
    @FXML
    private Button logoutBtn, addNodeBtn, editCatBtn, editPersBtn, editIFCBtn, showAllCons, editAdminBtn, saveBtn;
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane canvasWrapper;
    @FXML
    private MenuButton changeAlgorithmDD;
    @FXML
    private AnchorPane mainpane;
    private static Pane imageViewPane;
    private static Pane nodeOverlay;
    public static Pane lineOverlay;
    public static Pane infoOverlay;
    public static ObservableList<UniqueNode> displayedNodes = FXCollections.observableArrayList();
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    private static ObservableList<PropertyDisplay> displayedPanels = FXCollections.observableArrayList();
    private ImageView imageView;
    private static Floor currentFloor;
    private static Tab selectedTab;
    public static NavigationAlgorithm selectedAlgorithm;
    private static int scale = 1;
    private static int xdif = 0;
    private static Group zoomGroupGlobal;
    private double mouseX = 0, mouseY = 0;
    private final long MIN_STATIONARY_TIME = 50000 ;
    PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME));
    BooleanProperty mouseMoving = new SimpleBooleanProperty();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Change listener for removed node=
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (!isNowMoving) {
            //    System.out.println("Mouse stopped!");
            }
        });
        pause.setOnFinished(e -> onLogout());
        mainpane.setOnMouseClicked(e ->{
            mouseMoving.set(true);
           // System.out.println("Mouse move!");
            pause.playFromStart();
        });
        mainpane.setOnMouseDragged(e ->{
            mouseMoving.set(true);
          //  System.out.println("Mouse move!");
            pause.playFromStart();
        });
        mainpane.setOnMouseMoved(e -> {
            mouseMoving.set(true);
            pause.playFromStart();
          //  System.out.println("Mouse move!");
        });

        displayedNodes.addListener((ListChangeListener<UniqueNode>) c -> nodeOverlay.getChildren().setAll(displayedNodes));
        nodeOverlay = new Pane();

        nodeOverlay.setPickOnBounds(false);
        lineOverlay = new Pane();
        lineOverlay.setPickOnBounds(false);
        infoOverlay = new Pane();
        infoOverlay.setPickOnBounds(false);
        //mainpane.setPickOnBounds(false);
        //Default current floor to first floor available
        if (currentFloor == null)
            currentFloor = h.getAllFloors().get(0);
        imageView = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage(currentFloor.getFilename()), imageViewPane);
        //Add tabs for each floor
        h.getAllFloors().forEach(floor -> {
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
        Group zoomGroup = new Group(imageView, lineOverlay, nodeOverlay);
        zoomGroupGlobal = zoomGroup;
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setPannable(true);
        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE &&
                !(event.getButton() == MouseButton.PRIMARY && event.isControlDown()))
                event.consume();
        });

        zoomGroup.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();

        });

        saveBtn.setOnAction(event -> {
            h.publishDB();
            try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Lightbox.fxml"));
            loader.load();
            Lightbox controller = loader.getController();
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(500),
                        ae -> controller.showin(mainpane)));
                timeline.play();
                 //Save changes to disk
            }
            catch (IOException ex) {
                Logger.getLogger(AdminMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        pane.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getText()) {
                case "a":
                    UniqueNode prevSel = NodeListenerFactory.currentSelection; //Save selection

                    //Make new node
                    UniqueNode node = NodeFactory.getNode(mouseX, mouseY, currentFloor.getID());
                    h.setLocation(node.getLocation().getID(), node.getLocation());
                    displayedNodes.add(node);
                    nodeOverlay.getChildren().setAll(displayedNodes);

                    //Connect to prev selection
                    if (prevSel != null)
                    {
                        node.getLocation().getNeighbors().add(prevSel.getLocation());
                        prevSel.getLocation().getNeighbors().add(node.getLocation());
                    }

                    //Show new connection
                    drawConnections(node);
                    updateNodePD();

                    //Select new node and prompt for edit
                    NodeListenerFactory.updateSelection(node);
                    NodeListenerFactory.editNodeName();
                    updateNodePD();
                    break;

                case "d":
                    if (NodeListenerFactory.currentSelection != null)
                        NodeListenerFactory.deleteCurrentSelection();
                    break;
            }
        });

        canvasWrapper.getChildren().addAll(pane, infoOverlay);

        //Default algorithm
        if (selectedAlgorithm == null)
            selectedAlgorithm = NavigationAlgorithm.A_STAR;

        for (NavigationAlgorithm val : NavigationAlgorithm.values()) {
            MenuItem item = new MenuItem(val.toString());
            item.setOnAction(actionEvent -> onChangeAlgorithm(val));
            changeAlgorithmDD.getItems().add(item);
//            System.out.println("we are here!!!");
        }
        changeAlgorithmDD.setText("Pathfinding Algorithm | "+selectedAlgorithm.toString());
    }

    /**
     * Draws connections between a node and its neighbors
     *
     * @param node Node to draw from
     */
    public static void drawConnections(UniqueNode node) {
        //Draw lines to the neighbors
        displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesFromLocation(node.getLocation(),
                                                                                           node.getLocation().getNeighborsSameFloor(node.getLocation().getFloorID())));
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
            pd.setProperty("Category", NodeListenerFactory.currentSelection.getLocation().getCategory().getCategory());
            pd.setProperty("# of Neighbors", NodeListenerFactory.currentSelection.getLocation().getNeighbors().size() + "");
            pd.setProperty("ID", "" + NodeListenerFactory.currentSelection.getLocation().getID());
            pd.setProperty("Permissions", "" + NodeListenerFactory.currentSelection.getLocation().getCategory().getPermission() + "");
            displayedPanels.set(0, pd);
            AdminMainController.infoOverlay.getChildren().clear();
            AdminMainController.infoOverlay.getChildren().addAll(displayedPanels);
        }
    }

    public void onLogout() {
        mouseMoving.set(false);
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", logoutBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAdminEdit(ActionEvent actionEvent) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editAdmin.fxml", "Edit Admins", editAdminBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddNode(ActionEvent actionEvent) {

       // System.out.println(zoomGroupGlobal.getScaleX() + ", " + zoomGroupGlobal.getScaleY() + ", " + zoomGroupGlobal.getScaleZ());
        UniqueNode node = NodeFactory.getNode(imageView.getImage().widthProperty().doubleValue() / 2.0,
                                              imageView.getImage().heightProperty().doubleValue() / 2.0,
                                              currentFloor.getID());
        h.setLocation(node.getLocation().getID(), node.getLocation());
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

    private void onChangeAlgorithm(NavigationAlgorithm algorithm) {
       // System.out.println("Changed Algorithm to " + algorithm.toString());
        selectedAlgorithm = algorithm;
        changeAlgorithmDD.setText("Pathfinding Algorithm | " + algorithm.toString());
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

    public void onShowAllCons(ActionEvent actionEvent) {
        displayedLines.clear();
        displayedNodes.forEach(node -> displayedLines.addAll(DrawLines.drawLinesFromLocation(node.getLocation(),
                                                                                             node.getLocation().getNeighborsSameFloor(node.getLocation().getFloorID()))));
        lineOverlay.getChildren().setAll(displayedLines);
    }
}
