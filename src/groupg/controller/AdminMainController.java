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
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AdminMainController implements Initializable
{
    @FXML
    private Button logoutBtn, addNodeBtn, editCatBtn, editPersBtn;

    @FXML
    private GridPane canvasWrapper;
    public static Pane nodeOverlay, lineOverlay, imageViewPane;
    private ImageView imageView;
    public static ObservableList<UniqueNode> displayedNodes = FXCollections.observableArrayList();
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        //Change listener for removed nodes
        displayedNodes.addListener((ListChangeListener<UniqueNode>) c -> nodeOverlay.getChildren().setAll(displayedNodes));

        //Change listener for removed connections
        displayedLines.addListener((ListChangeListener<UniqueLine>) c -> {
            System.out.println("change listener");
            lineOverlay.getChildren().setAll(displayedLines); //For some reason, this never gets called (except twice when I don't change the lines...)
        });

        nodeOverlay = new Pane();
        nodeOverlay.setPickOnBounds(false);
        lineOverlay = new Pane();
        lineOverlay.setPickOnBounds(false);
        imageViewPane = new Pane();
        imageViewPane.setPickOnBounds(false);
        imageViewPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            displayedNodes.forEach(elem -> {
//                elem.getLocation().setX((int)(elem.getLocation().getX() + (newValue.doubleValue()-oldValue.doubleValue())/oldValue.doubleValue()));
//                elem.getLocation().setX((int)(elem.getLocation().getX() - (2265)/(newValue.doubleValue() - oldValue.doubleValue())));
//                elem.setCenterX(elem.getLocation().getX());
            });
        });
        imageView = ImageViewFactory.getImageView(new Image("/image/faulkner_4_cropped.png", 2265, 1290, true, true), imageViewPane);

        //Fill list with nodes from DB
        displayedNodes.clear();
        for (Location l : HospitalData.getAllLocations())
        {
            displayedNodes.add(NodeFactory.getNode(l));
        }
        nodeOverlay.getChildren().setAll(displayedNodes);

        //Add layers
        imageViewPane.getChildren().add(imageView);
        Group zoomGroup = new Group(imageView, nodeOverlay, lineOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.addEventFilter(ScrollEvent.ANY, scrollEvent ->
        {
            if (scrollEvent.getDeltaY() > 0)
            {
                //
            }
            scrollEvent.consume();
        });
        pane.setPannable(true);
        canvasWrapper.getChildren().addAll(pane);
    }

    public static void drawConnections(UniqueNode node)
    {
        //Draw lines to the neighbors
        displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesFromLocation(node.getLocation(), node.getLocation().getNeighbors()));
        lineOverlay.getChildren().setAll(displayedLines);
    }

    public void onLogout(ActionEvent actionEvent)
    {
        HospitalData.publishDB(); //Save changes to disk

        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Welcome", logoutBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onAddNode(ActionEvent actionEvent)
    {
        UniqueNode node = NodeFactory.getNode(100, 100);
        HospitalData.setLocation(node.getLocation().getID(), node.getLocation());
        displayedNodes.add(node);
        nodeOverlay.getChildren().setAll(displayedNodes);
    }

    public void onEditCat(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editCategory.fxml", "Edit Categories", editCatBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onEditPers(ActionEvent event)
    {
        try
        {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/editPers.fxml", "Edit Personnel", editCatBtn.getScene());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
