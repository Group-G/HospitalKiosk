package groupg.controller;

import com.sun.org.apache.xpath.internal.SourceTree;
import groupg.Main;
import groupg.database.*;
import groupg.jfx.AutoCompleteTextField;
import groupg.jfx.ResourceManager;
import groupg.jfx.UniqueFloor;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.application.Application;
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

import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static groupg.Main.h;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {

    @FXML
    private GridPane Floorselectgrid;
    @FXML
    private Group mapGroup, menuGroup,textFieldGroup;
    @FXML
    private ImageView imageViewBase;
    @FXML
    private Pane mapPane, menuPane,fadePane, searchPane, FloorSelectPane;
    @FXML
    private VBox VBoxSelectPane;
    @FXML
    private Button menuBtn,loginBtn, aboutBtn,searchBtn,upButton, downButton, viewButton, menuExitBtn;
    @FXML
    private AnchorPane LayerA,LayerB,LayerC,LayerD;
    @FXML
    private MenuButton language;
    @FXML
    private MenuItem english,spanish,portugues,chinese;
    @FXML
    private Accordion acccordionDropDown;
    private AutoCompleteTextField searchField;
    //private static int permission = 0;
    Scale scale = new Scale();
    double WIDNDOW_WIDTH = 0;
    List<UniqueFloor> FaulknerFloors = new ArrayList<>();
    boolean onScreen = false;
    private static int permission = 0;
    int currentFloor = 7;
    private boolean menuOpen = false;
    List<Floor> floors = Main.h.getAllFloors();

    public static void setPermission(int p){
        permission = p;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //String css = this.getClass().getResource("/view/welcomescreen.css").toExternalForm();
        Application.setUserAgentStylesheet(getClass().getResource("/view/welcomescreen.css").toExternalForm());
        FloorSelectPane.setVisible(false);
        //menuPaneVBox.getChildren().add(acccordionDropDown);
        searchField = new AutoCompleteTextField();
        searchField.setCurrentSelection(new EmptyLocation());
        searchField.getEntries().addAll(h.getAllLocations());
        searchField.setPrefHeight(50);

        textFieldGroup.getChildren().add(searchField);
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
        menuPane.setPickOnBounds(false);

//        menuPane


        for (Category category : Main.h.getAllCategories()) {
            if (category.getPermission() <= permission) {
                ListView<Location> locByCat = new ListView();
                locByCat.getItems().addAll(h.getLocationsByCategory(category.getCategory()));
                locByCat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                acccordionDropDown.getPanes().addAll(new TitledPane(category.getCategory() + " ", locByCat));
            }
        }






        fadePane.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5); -fx-background-radius: 10;");
        fadePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        fadePane.setVisible(false);

        searchPane.setStyle("-fx-background-color: rgba(255, 255, 255); ");
        searchPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        searchPane.setVisible(true);

        searchBtn.setOnMouseClicked((MouseEvent event) -> {
            // go to the node
            // dispaly the node

        });

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
        int fa = 0, b=0;
        for (int i = 0; i < floors.size(); i ++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                fa++;
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 1325, 1185, 1325, -1600, fa);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked( event -> {
                    System.out.println("Faulkner!");
                    removefloors();
                    event.consume();
                    flipToFloor(uf.getFloorIndex());
                    zoomFloor(uf);
                  getfloors(f.getBuildingID());
                });
            }
            else if (f.getBuildingID() == 0) {
                b++;
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 1065+i*12, 435-i*25, 1065+i*12, -700, b);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked( event -> {
                    System.out.println("BELKIN!");
                    removefloors();
                    event.consume();
                    flipToFloor(uf.getFloorIndex());
                    zoomFloor(uf);
                    getfloors(f.getBuildingID());
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
            FloorSelectPane.setVisible(false);
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
            zoomFloor(FaulknerFloors.get(4));
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
//        double scaleValH = mapPane.getWidth()/uf.getImageView().getImage().getWidth()*.7;
        double scaleValV = mapPane.getHeight()/uf.getImageView().getImage().getHeight()*.9;
        double scaleVal = scaleValV;
//        if(scaleValV>scaleValH){
//            scaleVal = scaleValV;
//        }
        double xoffset = mapPane.getWidth()*0.3;
        double yoffset = mapPane.getHeight()*0.15;
        xoffset = mapPane.getWidth()/4;
        yoffset = mapPane.getHeight()/6;
        System.out.println();
        System.out.println("scaleVal = " + scaleVal);
        System.out.println("yoffset = " + yoffset);
        System.out.println("xoffset = " + xoffset);

        scaleImage(uf.getGroup().getTranslateX() - xoffset, uf.getGroup().getTranslateY() - yoffset,scaleVal,  1250).play();
    }

    private void setHighlight(int floorindex){

    }

    private void getfloors(int buildingID){
        List<Floor> floors = Main.h.getBuildingById(buildingID).getFloorList();
        for(int j = 0; j< floors.size(); j++){

            Button button = new Button(Integer.toString(j+1));
            //VBoxSelectPane.getChildren().add(j+1, button);



            Floorselectgrid.add(button, 0, j+1 );
            button.setOnMouseClicked((MouseEvent event) -> {
                       flipToFloor(Integer.parseInt(button.getText()));
                    });
            //button.
        }
    }
private void removefloors(){
        Floorselectgrid.getChildren().clear();
}

    private void flipToFloor(int index){
        FloorSelectPane.setVisible(true);
        if(index <= 7 && index >= 0) {
            System.out.println("Flipping to floor " + index);
            currentFloor = index;
        }

//        System.out.println("Keeping floors " + (currentFloor) +" down");
        for(int j = 0;  j < FaulknerFloors.size(); j++){
            UniqueFloor u = FaulknerFloors.get(j);

            if(u.getFloorIndex() <= index && !u.onScreen()) {
                //moveMiniMap(u.getGroup(), u.getOnX(), u.getOnY(), 1250 + u.getFloorIndex() * 100).play();
                fadeGroup(u, true, 250).play();
                u.setOnScreen(true);
            }
            else if(u.getFloorIndex() > index && u.onScreen()) {
               // moveMiniMap(u.getGroup(), u.getOffX(), u.getOffY(), 1750 - u.getFloorIndex() * 100).play();
                fadeGroup(u, false, 250).play();
                u.setOnScreen(false);
            }

        }
    }





    private Animation moveMiniMap(Group group, double x, double y, double time) {

        double curX = group.getTranslateX();
        double curY = group.getTranslateY();


        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {
                double newX = curX+fraction*(x-curX);
                group.setTranslateX(newX);
                double newY = curY+fraction*(y-curY);
                group.setTranslateY(newY);
            }
        };

        expandPanel.setOnFinished(e-> {

        });
        return expandPanel;
    }

    private Animation fadeGroup(UniqueFloor uf,boolean fadeIn, double time) {

        double start, end;
        if(fadeIn){
            uf.getGroup().setTranslateX(uf.getOnX());
            uf.getGroup().setTranslateY(uf.getOnY());
            start = 0;
            end = 1;
        }
        else{
            start = 1;
            end = 0;
        }


        final FadeTransition ft =new FadeTransition(Duration.millis(time), uf.getGroup());
        ft.setFromValue(start);
        ft.setToValue(end);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ft.setOnFinished(event -> {
            if(!fadeIn) {
                uf.getGroup().setTranslateX(uf.getOffX());
                uf.getGroup().setTranslateY(uf.getOffY());
            }
        });

        return ft;
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

    public void onSearch(){

    }

}


