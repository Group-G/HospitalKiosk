package groupg.controller;

import groupg.Main;
import groupg.database.Category;
import groupg.database.Floor;
import groupg.database.Location;
import groupg.jfx.ResourceManager;
import groupg.jfx.UniqueFloor;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import static groupg.Main.h;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {


    @FXML
    private Group mapGroup, menuGroup;
    @FXML
    private ImageView imageViewBase;
    @FXML
    private Pane mapPane;
    @FXML
    private Button searchBtn;
    @FXML
    private Button menuBtn;
    @FXML
    private Button loginBtn, aboutBtn;
    @FXML
    private AnchorPane LayerA,LayerB,LayerC,LayerD;
    @FXML
    private Button upButton, downButton, viewButton, menuExitBtn;
    @FXML
    private Pane menuPane;
    @FXML
    private Pane fadePane, searchPane;
    @FXML
    private MenuButton language;
    @FXML
    private MenuItem english,spanish,portugues,chinese;
    @FXML
    private Accordion acccordionDropDown;
    //private static int permission = 0;

    Scale scale = new Scale();
    double WIDNDOW_WIDTH = 0;
    List<UniqueFloor> FaulknerFloors = new ArrayList<>();
    boolean onScreen = false;
    private static int permission = 0;
    int currentFloor = 7;
    private boolean menuOpen = false;


    public static void setPermission(int p){
        permission = p;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //menuPaneVBox.getChildren().add(acccordionDropDown);
        acccordionDropDown.getPanes().clear();

        File qrcode = new File("qrcode.jpg");
        if(qrcode.exists()){
            qrcode.delete();
        }


        LayerC.setPickOnBounds(false);
        LayerA.setPickOnBounds(false);
        LayerB.setPickOnBounds(false);
        LayerD.setPickOnBounds(false);

        imageViewBase.setPickOnBounds(true);
        imageViewBase.setImage(new Image("/image/FaulknerMaps/Ground.png"));

//        menuPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        menuPane.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
        //menuPane.getChildren().add(acccordionDropDown);
        menuPane.setVisible(false);
//        menuPane


        for (Category category : Main.h.getAllCategories()) {
            if (category.getPermission() <= permission) {
                ListView<Location> locByCat = new ListView();
                locByCat.getItems().addAll(h.getLocationsByCategory(category.getCategory()));
                locByCat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                acccordionDropDown.getPanes().addAll(new TitledPane(category.getCategory() + " "/* +category.getPermission()*/, locByCat));
               /* locByCat.setOnMouseClicked((MouseEvent event) -> {
                    //System.out.println(startField.getText().trim().isEmpty() && endField.getText().isEmpty());

                    if(event.getClickCount() == 2 && startField.getText().isEmpty() && endField.getText().isEmpty()){
                        startField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                    }
                    else if(event.getClickCount() == 2 && startField.getText().isEmpty() && !(endField.getText().isEmpty())){
                        startField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                    }
                    else if(event.getClickCount() == 2) {
                        endField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                    }
                }); */
            }
        }






        fadePane.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5); -fx-background-radius: 10;");
        fadePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        fadePane.setVisible(false);

        searchPane.setStyle("-fx-background-color: rgba(255, 255, 255); ");
        searchPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        searchPane.setVisible(true);

        menuBtn.setOnAction(event -> {
            if(!menuOpen){
                menuOpen = true;
                menuPane.setVisible(true);
                menuPane.setPickOnBounds(true);
                fadePane.setVisible(true);
                moveMenu(true, 25).play();
            }
        });

        menuExitBtn.setOnAction(event ->{
            if(menuOpen){
//                menuPane.setVisible(false);
                menuPane.setPickOnBounds(false);
                menuOpen = false;
                fadePane.setVisible(false);
                moveMenu(false, 25).play();

            }
        });

        List<Floor> floors = Main.h.getAllFloors();
        for (int i = 0; i < floors.size(); i ++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 544+i*5, 342-i*12, 544+i*5, -600, i);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked( event -> {

                    event.consume();
                    flipToFloor(uf.getTimeDelay() + 1);
                    zoomFloor(uf);

                });
            }
        }

        mapGroup.getTransforms().add(scale);
//        System.out.println(mapPane.getWidth());
        mapPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //mapPane.setMaxWidth(anchorPane.getMaxWidth());
        mapPane.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("clicked the pane!!!!");
            resetZoom(WIDNDOW_WIDTH, 1250);
//            zoomFloor(FaulknerFloors.get(0));
            flipToFloor(7);
        });

        mapPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            WIDNDOW_WIDTH = (double)newValue;
            resetZoom(WIDNDOW_WIDTH, 1);
            fadePane.setPrefWidth((double)newValue);
//            fadeRect.setWidth((double)newValue);

        });

        mapPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            menuPane.setPrefHeight((double)newValue);
            fadePane.setPrefHeight((double)newValue);
//            fadeRect.setHeight((double)newValue);


        });



        viewButton.setOnAction(event -> {
//            zoomFloor(FaulknerFloors.get(0));
            flipToFloor(1);
            zoomFloor(FaulknerFloors.get(0));
        });
        upButton.setOnAction(event -> {

            flipToFloor(currentFloor+1);

        });
        downButton.setOnAction(event -> {

            flipToFloor(currentFloor-1);
        });

        aboutBtn.setOnAction(event -> {
            try {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/aboutscreen.fxml", "Welcome", aboutBtn.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loginBtn.setOnAction(event -> {
            try {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/adminLogin.fxml", "Welcome", aboutBtn.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //set button graphics
        viewButton.setGraphic(new ImageView(new Image("/image/Icons/location.png",30, 30, false, false)));
        upButton.setGraphic(new ImageView(new Image("/image/Icons/zoom_in.png",30, 30, false, false)));
        downButton.setGraphic(new ImageView(new Image("/image/Icons/zoom_out.png",30, 30, false, false)));
        searchBtn.setGraphic(new ImageView(new Image("/image/Icons/search.png",30, 30, false, false)));
        menuBtn.setGraphic(new ImageView(new Image("/image/Icons/menu.png",30, 30, false, false)));
        loginBtn.setGraphic(new ImageView(new Image("/image/Icons/admin.png",30, 30, false, false)));
        aboutBtn.setGraphic(new ImageView(new Image("/image/Icons/info.png",30, 30, false, false)));
        menuExitBtn.setGraphic(new ImageView(new Image("/image/Icons/close.png",30, 30, false, false)));


        language.setGraphic(new ImageView(new Image("/image/Icons/america.png"))); //default as english

        english.setGraphic(new ImageView(new Image("/image/Icons/america.png")));
        spanish.setGraphic(new ImageView(new Image("/image/Icons/spain.png")));
        portugues.setGraphic(new ImageView(new Image("Image/Icons/portugal.png")));
        chinese.setGraphic(new ImageView(new Image("Image/Icons/china.png")));
    }
    private void zoomFloor(UniqueFloor uf){
        double scaleVal = mapPane.getWidth()/uf.getImageView().getImage().getWidth()*.65;
        double xoffset = mapPane.getWidth()*.35;
        double yoffset = mapPane.getHeight()*.35/2;

        scaleImage(uf.getImageView().getX() - xoffset, uf.getImageView().getY() - yoffset, scaleVal, 1250).play();
    }
    private void flipToFloor(int index){

        if(index <= 7 && index >= 1) {
            System.out.println("Flipping to floor " + index);
            currentFloor = index;
        }
//        System.out.println("Keeping floors " + (currentFloor) +" down");
        for(int j = 0; j < currentFloor && j < FaulknerFloors.size(); j++){
            UniqueFloor u = FaulknerFloors.get(j);

            if(!u.onScreen()) {
                moveImage(u.getImageView(), u.getOnX(), u.getOnY(), 1250 + u.getTimeDelay() * 100).play();
                u.setOnScreen(true);
            }

        }
//        System.out.println("Removing floors " + (currentFloor+1) + " up");
        for(int j = currentFloor; j < FaulknerFloors.size(); j++){
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
        double curX = -mapGroup.getTranslateX() / curScale;
        double curY = -mapGroup.getTranslateY() / curScale;


        mapGroup.getTransforms().add(scale);
        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                mapGroup.getTransforms().clear();
                mapGroup.getTransforms().add(scale);

                double newScale = curScale + fraction * (scaleIn - curScale);

                scale.setX(newScale);
                scale.setY(newScale);

                double newX = curX + fraction * (x - curX);
                mapGroup.setTranslateX(-newX * newScale);
                double newY = curY + fraction * (y - curY);
                mapGroup.setTranslateY(-newY * newScale);
            }
        };
        expandPanel.setOnFinished(e -> {
        });
        return expandPanel;
    }

    private Animation moveMenu(boolean on, double time) {



        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                double cx,cy=0,tx,ty=0;
                if(on){
                    tx = 0;
                    cx = -menuPane.getWidth();
                }
                else{
                    tx = -menuPane.getWidth();
                    cx = 0;
                }



                double newX = cx+fraction*(tx-cx);
                menuGroup.setTranslateX(newX);
                double newY = cy+fraction*(ty-cy);
                menuGroup.setTranslateY(newY);
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


