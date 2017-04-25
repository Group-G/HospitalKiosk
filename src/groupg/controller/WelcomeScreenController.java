package groupg.controller;

import groupg.Main;
import groupg.algorithm.NavigationFacade;
import groupg.database.Category;
import groupg.database.EmptyLocation;
import groupg.database.Floor;
import groupg.database.Location;
import groupg.jfx.AutoCompleteTextField;
import groupg.jfx.ImageViewFactory;
import groupg.jfx.ResourceManager;
import groupg.jfx.UniqueFloor;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.ResourceBundle;

import static groupg.Main.h;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {


    @FXML
    private Group mapGroup;
    @FXML
    private ImageView imageViewBase;
    @FXML
    private Pane mapPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button searchBtn;
    //@FXML
    //private Pane dropDown;
    @FXML
    private AnchorPane LayerA,LayerB,LayerC;
    @FXML
    private Accordion acccordionDropDown;
    @FXML
    private HBox startFieldHBox,endFieldHBox;
    @FXML
    private Button upButton, downButton, viewButton;
    Scale scale = new Scale();
    //ImageView newmap = new ImageView();
    double WIDNDOW_WIDTH = 0;
    List<UniqueFloor> FaulknerFloors = new ArrayList<>();
    boolean onScreen = false;
    private static int permission = 0;
    private AutoCompleteTextField startField, endField;
    private NavigationFacade navigation;
    int topFloor = 0;

    public WelcomeScreenController() {
        startField = new AutoCompleteTextField();
        startField.setCurrentSelection(new EmptyLocation());
        endField = new AutoCompleteTextField();
        endField.setCurrentSelection(new EmptyLocation());

        List<Location> kioskLocs = h.getLocationsByCategory("Kiosk");
        if (kioskLocs.size() > 0)
            startField.setCurrentSelection(kioskLocs.get(0));

        navigation = new NavigationFacade();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //anchorPane.getChildren().add(dropDown);
        //dropDown.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        startFieldHBox.getChildren().add(startField);
        endFieldHBox.getChildren().add(endField);
        //Application.setUserAgentStylesheet(getClass().getResource("/view/welcomescreen.css").toExternalForm());
        Application.setUserAgentStylesheet(getClass().getResource("/view/welcomescreen.css").toExternalForm());
        startField.getStyleClass().add("startfield");
        endField.getStyleClass().add("endfield");

        acccordionDropDown.getPanes().clear();
        for (Category category : h.getAllCategories()) {
            if (category.getPermission() <= permission) {
                ListView<Location> locByCat = new ListView();
                locByCat.getItems().addAll(h.getLocationsByCategory(category.getCategory()));
                locByCat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                acccordionDropDown.getPanes().addAll(new TitledPane(category.getCategory() + " "/* +category.getPermission()*/, locByCat));
                locByCat.setOnMouseClicked((MouseEvent event) -> {
                    if (event.getClickCount() == 2)
                        endField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                });
            }
        }

        File qrcode = new File("qrcode.jpg");
        boolean exists = qrcode.exists();
        if(exists){
            qrcode.delete();
        }


        LayerC.setPickOnBounds(false);
        LayerA.setPickOnBounds(false);
        LayerB.setPickOnBounds(false);
        //anchorPane.setPickOnBounds(false);
        imageViewBase.setPickOnBounds(true);
        imageViewBase.setImage(new Image("/image/FaulknerMaps/Ground.png"));

        List<Floor> floors = Main.h.getAllFloors();
        for (int i = 0; i < floors.size(); i ++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 544+i*5, 342-i*14, 544+i*5, -600, i);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked( event -> {

                    event.consume();

                    flipToFloor(uf.getTimeDelay());
                    double scaleVal = mapPane.getWidth()/uf.getImageView().getImage().getWidth();
                    scaleImage(uf.getImageView().getX(), uf.getImageView().getY(), scaleVal, 1250).play();
                });
            }
        }

        mapGroup.getTransforms().add(scale);
        System.out.println(mapPane.getWidth());
        mapPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //mapPane.setMaxWidth(anchorPane.getMaxWidth());
        mapPane.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("clicked the pane!!!!");

            resetZoom(WIDNDOW_WIDTH, 1250);
        });

        mapPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            WIDNDOW_WIDTH = (double)newValue;
            resetZoom(WIDNDOW_WIDTH, 1);

            //dropDown.setPrefWidth(80);
            //dropDown.setPrefHeight(mapPane.getHeight()*.869329);
        });

        viewButton.setOnAction(event -> {
            if (onScreen == false) {
                for (UniqueFloor uf: FaulknerFloors) {
                    if(!uf.onScreen()) {
                        moveImage(uf.getImageView(), uf.getOnX(), uf.getOnY(), 1250 + uf.getTimeDelay() * 100).play();
                        uf.setOnScreen(true);
                    }

                }
                topFloor = 6;
                onScreen = true;
            } else {
                for (UniqueFloor uf: FaulknerFloors) {
                    if(uf.onScreen()) {
                        moveImage(uf.getImageView(), uf.getOffX(), uf.getOffY(), 1750 - uf.getTimeDelay() * 100).play();
                        uf.setOnScreen(false);
                    }
                }
                onScreen = false;
                topFloor = 0;
            }
        });
        upButton.setOnAction(event -> {
            topFloor++;
            if(topFloor>6){
                topFloor = 6;
            }
            System.out.println("Flipping to floor " + topFloor);
            flipToFloor(topFloor);

        });
        downButton.setOnAction(event -> {
            topFloor--;
            if(topFloor<0){
                topFloor = 0;
            }
            System.out.println("Flipping to floor " + topFloor);
            flipToFloor(topFloor);
        });
    }
    private void flipToFloor(int index){
        for(int j = 0; j < index; j++){
            UniqueFloor u = FaulknerFloors.get(j);

            if(!u.onScreen()) {
                moveImage(u.getImageView(), u.getOnX(), u.getOnY(), 1250 + u.getTimeDelay() * 100).play();
                u.setOnScreen(true);
            }

        }
        for(int j = index; j < FaulknerFloors.size(); j++){
            UniqueFloor u = FaulknerFloors.get(j);
            if(u.onScreen()) {
                moveImage(u.getImageView(), u.getOffX(), u.getOffY(), 1750 - u.getTimeDelay() * 100).play();
                u.setOnScreen(false);
            }
            onScreen = false;
        }
    }

    private Animation moveImage(ImageView image, double x, double y, double time) {

        double curX = image.getX();
        double curY = image.getY();


        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                double newX = curX+fraction*(x-curX);
                image.setX(newX);
                double newY = curY+fraction*(y-curY);
                image.setY(newY);
            }
        };

        expandPanel.setOnFinished(e-> {

        });
        return expandPanel;
    }

    private Animation scaleImage(double x, double y, double scaleIn, double time) {

        mapGroup.getTransforms().clear();
        double curScale = scale.getMxx();
        double curX = -mapGroup.getTranslateX()/curScale;
        double curY = -mapGroup.getTranslateY()/curScale;


        mapGroup.getTransforms().add(scale);
        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                mapGroup.getTransforms().clear();
                mapGroup.getTransforms().add(scale);

                double newScale = curScale + fraction*(scaleIn-curScale);

                scale.setX(newScale);
                scale.setY(newScale);

                double newX = curX+fraction*(x-curX);
                mapGroup.setTranslateX(-newX*newScale);
                double newY = curY+fraction*(y-curY);
                mapGroup.setTranslateY(-newY*newScale);
            }
        };
        expandPanel.setOnFinished(e-> {
        });

        return expandPanel;
    }


    public void resetZoom(double width, double time){
        double newScale = width / imageViewBase.getImage().getWidth();
        scaleImage(00, 00, newScale, time).play();
    }

}


