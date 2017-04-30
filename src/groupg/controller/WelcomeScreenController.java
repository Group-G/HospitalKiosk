package groupg.controller;

import groupg.Main;
import groupg.algorithm.NavigationFacade;
import groupg.database.Category;
import groupg.database.EmptyLocation;
import groupg.database.Floor;
import groupg.database.Location;
import groupg.jfx.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.*;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static groupg.Main.h;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {

    @FXML
    private GridPane Floorselectgrid;
    @FXML
    private Group mapGroup, menuGroup, textFieldGroup;
    @FXML
    private ImageView imageViewBase;
    @FXML
    private Pane mapPane, menuPane, fadePane, searchPane, FloorSelectPane;
    @FXML
    private VBox VBoxSelectPane, fieldsBox;
    @FXML
    private Button menuBtn, loginBtn, aboutBtn, searchBtn, upButton, downButton, viewButton, menuExitBtn, directionBtn;
    @FXML
    private AnchorPane LayerA, LayerB, LayerC, LayerD;
    @FXML
    private MenuButton language;
    @FXML
    private MenuItem english, spanish, portugues, chinese;
    @FXML
    private Accordion acccordionDropDown;
    private AutoCompleteTextField searchField, startField, endField;
    Scale scale = new Scale();
    double WIDNDOW_WIDTH = 0;
    List<UniqueFloor> FaulknerFloors = new ArrayList<>();
    boolean onScreen = false;
    private static int permission = 0;
    int currentFloor = 7;
    private boolean menuOpen = false;
    List<Floor> floors = Main.h.getAllFloors();
    private String lang = "Eng";
    private ListView<String> dirList;
    private ImageView qrcode;
    @FXML
    private CheckBox handicapped;
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    NavigationFacade navigation;
    private static  LocalTime currentTime = LocalTime.now();
    private static LocalDate currentDate = LocalDate.now();
    private static DayOfWeek dow = currentDate.getDayOfWeek();
    @FXML
    private Text errorText;


    public static void setPermission(int p) {
        permission = p;
    }

    private static boolean isVisitingHrs(){
        String currDay = getDow().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        if(currDay.equals("Monday") || currDay.equals("Tuesday") || currDay.equals("Wednesday") || currDay.equals("Thursday") || currDay.equals("Friday") || currDay.equals("Saturday") || currDay.equals("Sunday"))
        {
            String s = getCurrentTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
            String noon = getCurrentTime().NOON.format(DateTimeFormatter.ISO_LOCAL_TIME);
            LocalTime eight = LocalTime.parse("20:00", DateTimeFormatter.ISO_LOCAL_TIME);
            String eightPm = eight.format(DateTimeFormatter.ISO_LOCAL_TIME);
            StringBuilder sb = new StringBuilder(s);
            int i,j,k;
            i = 0;
            j = 0;
            k = 0;
            while(i < s.length()){
                char ch = s.charAt(i);
                if(ch == ' ' || ch == ':' || ch == '.'){
                    String before = s.substring(0,i);
                    String after = s.substring(i+1);
                    s = before+after;

                }

                else
                    i++;
            }
            StringBuilder timeNow = new StringBuilder();
            String nowTime = new String();
            for(int z = 0; z <s.length()-3;z++){
                char ch = s.charAt(z);
                timeNow.append(ch);
            }
            nowTime = s.substring(0,s.length()-3);

            System.out.println(nowTime);
            while(j< noon.length()){
                char ch = noon.charAt(j);
                if(ch == ' ' || ch == ':' || ch == '.'){
                    String before = noon.substring(0,j);
                    String after = noon.substring(j+1);
                    noon = before+after;

                }

                else
                    j++;
            }
            System.out.println(noon);
            while(k < eightPm.length()){
                char ch = eightPm.charAt(k);
                if(ch == ' ' || ch == ':' || ch == '.'){
                    String before = eightPm.substring(0,k);
                    String after = eightPm.substring(k+1);
                    eightPm = before+after;

                }
                else
                    k++;
            }
            System.out.println(eightPm);
            int currTime = Integer.parseInt(nowTime);
            int noonTime = Integer.parseInt(noon);
            int eightTime = Integer.parseInt(eightPm);

            if(currTime < noonTime){
                return false;
            }
            else if(currTime > noonTime && currTime < eightTime){
                return true;
            }
            else return false;
        }
        else
            return false;
    }

    public static LocalTime getCurrentTime() {
        // TODO Auto-generated method stub
        return currentTime;
    }
    public static DayOfWeek getDow(){
        return dow;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //String css = this.getClass().getResource("/view/welcomescreen.css").toExternalForm();
        //Application.setUserAgentStylesheet(getClass().getResource("/view/developWelcomescreen.css").toExternalForm());
        FloorSelectPane.setVisible(false);
        searchField = new AutoCompleteTextField();
        startField = new AutoCompleteTextField();
        endField = new AutoCompleteTextField();

        searchField.setCurrentSelection(new EmptyLocation());
        startField.setCurrentSelection(new EmptyLocation());
        endField.setCurrentSelection(new EmptyLocation());

        searchField.getEntries().addAll(h.getAllLocations());
        startField.getEntries().addAll(h.getAllLocations());
        endField.getEntries().addAll(h.getAllLocations());

        searchField.setPrefHeight(50);
        startField.setPrefHeight(50);
        endField.setPrefHeight(50);

        List<Location> kioskLocs = h.getLocationsByCategory("Kiosk");
        if (kioskLocs.size() > 0) {
            startField.setCurrentSelection(kioskLocs.get(0));
        }

        searchField.setOnKeyPressed(key -> {

            if (key.getCode() == KeyCode.ENTER) {
                if (searchField.getCurrentSelection().getX() != 0) {
                    onSearch(searchField.getCurrentSelection());
                }
            }
        });

        startField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                if (startField.getCurrentSelection().getX() == 0 || endField.getCurrentSelection().getX() == 0) {
                    return;
                }
                System.out.println("endField = " + endField.getCurrentSelection().getID());
                System.out.println("endField = " + endField.getCurrentSelection().getX());
                System.out.println("get the directions!!!");
            }
        });

        endField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                if (startField.getCurrentSelection().getX() == 0 || endField.getCurrentSelection().getX() == 0) {
                    return;
                }
                System.out.println("endField = " + endField.getCurrentSelection().getID());
                System.out.println("endField = " + endField.getCurrentSelection().getX());
                System.out.println("get the directions!!!");
            }
        });

        searchBtn.setOnAction(event -> {
            if (searchField.getCurrentSelection().getX() != 0) {
                onSearch(searchField.getCurrentSelection());
            }
        });

        directionBtn.setOnAction(event -> {
            if (startField.getCurrentSelection().getX() == 0 || endField.getCurrentSelection().getX() == 0) {
                return;
            }
            System.out.println("endField = " + endField.getCurrentSelection().getID());
            System.out.println("endField = " + endField.getCurrentSelection().getX());
            System.out.println("get the directions!!!");
        });


        textFieldGroup.getChildren().add(searchField);
        fieldsBox.getChildren().add(startField);
        fieldsBox.getChildren().add(endField);


        //acccordionDropDown.getPanes().clear();

        File qrcode = new File("qrcode.jpg");
        if (qrcode.exists()) {
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

        //to show building better
        mapPane.setTranslateY(-200);
//        mapGroup
//        menuPane


        ListView<Category> quickList = new ListView<>();
        for (Category c : Main.h.getAllCategories()) {
            if (c.getQuicksearchOn() == 1 && c.getPermission() <= permission) {
                quickList.getItems().add(c);
            }
            quickList.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    endField.setCurrentSelection(getClosestOfCategory(startField.getCurrentSelection(), quickList.getSelectionModel().getSelectedItem()));
                }
            });
        }


        TitledPane quickSearch = new TitledPane("Quick Search", quickList);
        acccordionDropDown.getPanes().add(quickSearch);

        for (Category category : h.getAllCategories()) {
            if (category.getCategory().contains("Hall") || category.getCategory().contains("Elevator") || category.getCategory().contains("Stairs") || category.getCategory().contains("Kiosk")) {

            } else {
                if (category.getPermission() <= permission) {
                    ListView<Location> locByCat = new ListView();
                    locByCat.getItems().addAll(h.getLocationsByCategory(category.getCategory()));
                    locByCat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    acccordionDropDown.getPanes().addAll(new TitledPane(category.getCategory() + " ", locByCat));
                    locByCat.setOnMouseClicked((MouseEvent event) -> {
                        System.out.println(startField.getText().trim().isEmpty() && endField.getText().isEmpty());
                        if (event.getClickCount() == 2 && startField.getText().isEmpty() && endField.getText().isEmpty()) {
                            startField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                        } else if (event.getClickCount() == 2 && startField.getText().isEmpty() && !(endField.getText().isEmpty())) {
                            startField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                        } else if (event.getClickCount() == 2) {
                            endField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                        }
                    });
                }
            }
        }


        fadePane.setStyle("-fx-background-color: rgba(100, 100, 100, 0.5); -fx-background-radius: 10;");
        fadePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        fadePane.setVisible(false);

        searchPane.setStyle("-fx-background-color: rgba(255, 255, 255); ");
        searchPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        searchPane.setVisible(true);


        menuBtn.setOnAction(event -> {
            if (!menuOpen) {
                menuOpen = true;
                menuPane.setVisible(true);
                menuPane.setPickOnBounds(true);
                fadePane.setVisible(true);
                moveMenu(true, 25).play();
            }
        });

        menuExitBtn.setOnAction(event -> {
            if (menuOpen) {
//                menuPane.setVisible(false);
                menuPane.setPickOnBounds(false);
                menuOpen = false;
                fadePane.setVisible(false);
                moveMenu(false, 25).play();

            }
        });

        List<Floor> floors = Main.h.getAllFloors();
        int fa = 0, b = 0;
        for (int i = 0; i < floors.size(); i++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                fa++;
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 1313, 1090, 1310, -1600, fa);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked(event -> {
                    System.out.println("Faulkner!");
                    removefloors();
                    event.consume();
                    flipToFloor(uf.getFloorIndex());
                    zoomFloor(uf);
                    getfloors(f.getBuildingID());
                });
            } else if (f.getBuildingID() == 0) {
                b++;
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 3350, 2625, 3350, -700, b);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked(event -> {
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
            WIDNDOW_WIDTH = (double) newValue;
            resetZoom(WIDNDOW_WIDTH, 1);
            fadePane.setPrefWidth((double) newValue);
//            fadeRect.setWidth((double)newValue);

        });

        mapPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            menuPane.setPrefHeight((double) newValue);
            fadePane.setPrefHeight((double) newValue);
//            fadeRect.setHeight((double)newValue);


        });


        viewButton.setOnAction(event -> {
            if (kioskLocs.size() > 0) {
                onSearch(kioskLocs.get(0));
            }
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
        viewButton.setGraphic(new ImageView(new Image("/image/Icons/location.png", 30, 30, false, false)));
        // upButton.setGraphic(new ImageView(new Image("/image/Icons/zoom_in.png",30, 30, false, false)));
        //downButton.setGraphic(new ImageView(new Image("/image/Icons/zoom_out.png",30, 30, false, false)));
        searchBtn.setGraphic(new ImageView(new Image("/image/Icons/search.png", 30, 30, false, false)));
        if(isVisitingHrs() == false){
            errorText.setVisible(true);
            return;
        }
        else{
            errorText.setVisible(false);
        }
        menuBtn.setGraphic(new ImageView(new Image("/image/Icons/menu.png", 30, 30, false, false)));
        loginBtn.setGraphic(new ImageView(new Image("/image/Icons/admin.png", 30, 30, false, false)));
        aboutBtn.setGraphic(new ImageView(new Image("/image/Icons/info.png", 30, 30, false, false)));
        menuExitBtn.setGraphic(new ImageView(new Image("/image/Icons/close.png", 30, 30, false, false)));
        language.setGraphic(new ImageView(new Image("/image/Icons/america.png"))); //default as english
        english.setGraphic(new ImageView(new Image("/image/Icons/america.png")));
        spanish.setGraphic(new ImageView(new Image("/image/Icons/spain.png")));
        portugues.setGraphic(new ImageView(new Image("/image/Icons/portugal.png")));
        chinese.setGraphic(new ImageView(new Image("/image/Icons/china.png")));
    }

    private void zoomFloor(UniqueFloor uf) {
//        double scaleValH = mapPane.getWidth()/uf.getImageView().getImage().getWidth()*.7;
        double scaleValV = mapPane.getHeight() / uf.getImageView().getImage().getHeight() * .9;
        double scaleVal = scaleValV;
//        if(scaleValV>scaleValH){
//            scaleVal = scaleValV;
//        }
        double xoffset = mapPane.getWidth() * 0.3;
        double yoffset = mapPane.getHeight() * 0.15;
        xoffset = mapPane.getWidth() / 3;
        yoffset = mapPane.getHeight() / 2.5;
        System.out.println();
        System.out.println("scaleVal = " + scaleVal);
        System.out.println("yoffset = " + yoffset);
        System.out.println("xoffset = " + xoffset);
        if (uf.getFloor().getBuildingID() == 1) {
            yoffset += 300;
        } else if (uf.getFloor().getBuildingID() == 0) {
            yoffset -= 100;
        }
        scaleImage(uf.getGroup().getTranslateX() - xoffset, uf.getGroup().getTranslateY() - yoffset, scaleVal, 1250, true).play();
    }

    private void setHighlight(int floorindex) {

    }

    private void getfloors(int buildingID) {
        List<Floor> floors = Main.h.getBuildingById(buildingID).getFloorList();
        for (int j = 0; j < floors.size(); j++) {

            Button button = new Button(Integer.toString(j + 1));
            //VBoxSelectPane.getChildren().add(j+1, button);
            Floorselectgrid.add(button, 0, floors.size() - j - 1);
            Floorselectgrid.setHalignment(button, HPos.CENTER);
            button.setOnMouseClicked((MouseEvent event) -> {
                flipToFloor(Integer.parseInt(button.getText()));
            });
            button.setOnMouseEntered((MouseEvent event) -> {
                button.setStyle("-fx-pref-width: 50; -fx-pref-height: 100; -fx-alignment: center; -fx-background-color:#dddddd; -fx-border-radius:0px; -fx-padding: 5px;" +
                        "    -fx-border-insets: 5px;" +
                        "    -fx-background-insets: 5px;");
            });
            button.setOnMouseExited((MouseEvent event) -> {
                button.setStyle("-fx-pref-width: 50; -fx-pref-height: 100; -fx-alignment: center; -fx-background-color:#ffffff;-fx-border-radius:0px;-fx-padding: 5px;" +
                        "    -fx-border-insets: 5px;" +
                        "    -fx-background-insets: 5px;");
            });
            button.setStyle("-fx-pref-width: 50; -fx-pref-height: 100;-fx-alignment: center; -fx-background-color:#ffffff;-fx-border-radius:0px;-fx-padding: 5px;" +
                    "    -fx-border-insets: 5px;" +
                    "    -fx-background-insets: 5px;");
        }

    }

    private void removefloors() {
        Floorselectgrid.getChildren().clear();
        while (Floorselectgrid.getRowConstraints().size() > 0) {
            Floorselectgrid.getRowConstraints().remove(0);
        }
    }

    private void flipToFloor(int index) {
        FloorSelectPane.setVisible(true);
        if (index <= 7 && index >= 0) {
            System.out.println("Flipping to floor " + index);
            currentFloor = index;
        }

//        System.out.println("Keeping floors " + (currentFloor) +" down");
        for (int j = 0; j < FaulknerFloors.size(); j++) {
            UniqueFloor u = FaulknerFloors.get(j);

            if (u.getFloorIndex() <= index && !u.onScreen()) {
                //moveMiniMap(u.getGroup(), u.getOnX(), u.getOnY(), 1250 + u.getFloorIndex() * 100).play();
                fadeGroup(u, true, 250).play();
                u.setOnScreen(true);
            } else if (u.getFloorIndex() > index && u.onScreen()) {
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
                double newX = curX + fraction * (x - curX);
                group.setTranslateX(newX);
                double newY = curY + fraction * (y - curY);
                group.setTranslateY(newY);
            }
        };

        expandPanel.setOnFinished(e -> {

        });
        return expandPanel;
    }

    private Animation fadeGroup(UniqueFloor uf, boolean fadeIn, double time) {

        double start, end;
        if (fadeIn) {
            uf.getGroup().setTranslateX(uf.getOnX());
            uf.getGroup().setTranslateY(uf.getOnY());
            start = 0;
            end = 1;
        } else {
            start = 1;
            end = 0;
        }


        final FadeTransition ft = new FadeTransition(Duration.millis(time), uf.getGroup());
        ft.setFromValue(start);
        ft.setToValue(end);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ft.setOnFinished(event -> {
            if (!fadeIn) {
                uf.getGroup().setTranslateX(uf.getOffX());
                uf.getGroup().setTranslateY(uf.getOffY());
            }
        });

        return ft;
    }


    private Animation scaleImage(double x, double y, double scaleIn, double time, boolean in) {

        mapGroup.getTransforms().clear();
        double curScale = scale.getMxx();
        double curX = -mapGroup.getTranslateX() / curScale;
        double curY = -mapGroup.getTranslateY() / curScale;


        mapGroup.getTransforms().add(scale);
        //double px = scale.getPivotX();
        //double py = scale.getPivotY();
        //System.out.println("px + \", \"+py = " + px + ", "+py);

        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {

                mapGroup.getTransforms().clear();
                mapGroup.getTransforms().add(scale);

                double newScale = curScale + Math.pow(fraction, .9) * (scaleIn - curScale);
                if (in) {
                    newScale = curScale + Math.pow(fraction, 1.7) * (scaleIn - curScale);
                }
                double newY = curY + fraction * (y - curY);
                double newX = curX + fraction * (x - curX);

//                double newY = curY + (scaleIn-newScale)/scaleIn *(y-curY);
//                double newX = curX + (scaleIn-newScale)/scaleIn *(x-curX);
                //scale.setPivotX(newX);
                //scale.setPivotX(newY);

                scale.setX(newScale);
                scale.setY(newScale);

                mapGroup.setTranslateX(-newX * (curScale + fraction * (scaleIn - curScale)));
                mapGroup.setTranslateY(-newY * (curScale + fraction * (scaleIn - curScale)));


//                fraction = Math.pow(fraction, .7);


//                scale.setPivotX(newX);
//                scale.setPivotY(newY);


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
                double cx, cy = 0, tx, ty = 0;
                if (on) {
                    tx = 0;
                    cx = -menuPane.getWidth();
                } else {
                    tx = -menuPane.getWidth();
                    cx = 0;
                }


                double newX = cx + fraction * (tx - cx);
                menuGroup.setTranslateX(newX);
                double newY = cy + fraction * (ty - cy);
                menuGroup.setTranslateY(newY);
            }
        };
        expandPanel.setOnFinished(e -> {
        });

        return expandPanel;
    }


    public void resetZoom(double width, double time) {
        double newScale = width / imageViewBase.getImage().getWidth();
        scaleImage(00, 00, newScale, time, false).play();
    }

    public void onSearch(Location searchNode) {

        System.out.println("Faulkner!");

        UniqueFloor curUniqueFloor;
        int floorID = searchNode.getFloorID();
        for (Floor f : Main.h.getAllFloors()) {
            if (Main.h.getFloorById(floorID).equals(f)) {
                for (UniqueFloor uf : FaulknerFloors) {
                    if (uf.getFloor().equals(f)) {
                        curUniqueFloor = uf;
                        flipToFloor(curUniqueFloor.getFloorIndex());
                        zoomFloor(uf);
                        getfloors(f.getBuildingID());
                        for (int i = 0; i < uf.getPoints().size(); i++) {
                            if (uf.getPoints().get(i).getLocation().getID() == searchNode.getID()) {
                                System.out.println("FOUND ID");
                                uf.getPoints().get(i).setRadius(20);
                                uf.getPoints().get(i).setFill(Color.RED);
                            }
                        }
                        //System.out.println(uf.getGroup().getChildren().size());
                    }
                }
            }

        }

    }

    public Animation animatePath(Circle circle, List<Location> path, double time) {
        double currx = circle.getTranslateX();
        double curry = circle.getTranslateY();

        final Animation animatePath = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double frac) {
                double newx = currx + frac + (path.get(0).getX() - currx);
                circle.setTranslateX(newx);
                double newy = curry + frac + (path.get(0).getY() - curry);
                circle.setTranslateY(newy);
            }
        };

        path.remove(path.get(0));
        if (path.isEmpty()) {
            return animatePath;
        } else {
            animatePath(circle, path, time);
        }
        return null; //should never happen ever!
    }

    public Location getClosestOfCategory(Location start, Category cat) {
        List<Location> filteredLocs = new ArrayList<>();
        List<Location> filterFloor = new ArrayList<>();
        HashMap<Floor, Location> hm = new HashMap<>();
        for (Location aloc : Main.h.getAllLocations()) {
            if (aloc.getCategory().equals(cat)) {
                filteredLocs.add(aloc);
            }
        }
        if (!filteredLocs.isEmpty()) {
            for (Floor floor : Main.h.getAllFloors()) {
                List<Location> floorLoc = new ArrayList<>();
                for (Location loca : filteredLocs) {
                    if (loca.getFloorID() == floor.getID()) {
                        floorLoc.add(loca);
                    }
                }
                Location close = closestTo(start, floorLoc);
                if (close != start) {
                    hm.put(floor, close);
                }
            }
            if (hm.containsKey(Main.h.getFloorById(start.getFloorID()))) {
                return hm.get(Main.h.getFloorById(start.getFloorID()));
            } else {
                for (Floor floor : Main.h.getAllFloors()) {
                    if (hm.containsKey(floor)) {
                        return hm.get(floor);
                    }
                }
            }
        } else {
            return start;
        }
        return start;
    }

    private Location closestTo(Location closest, List<Location> locs) {
        Location bestLoc = closest;
        double bestDist = Double.MAX_VALUE;
        double curr;
        for (Location loc : locs) {
            curr = loc.lengthTo(closest);
            if (curr < bestDist) {
                bestDist = curr;
                bestLoc = loc;
            }
        }
        return bestLoc;
    }


    private void generateTextDirections(List<Location> locations) {
        ObservableList<String> directions = FXCollections.observableArrayList();
        boolean enterElevator = false;
        boolean samepath = false;
        int pixelCounter = 0;
        boolean straight = false;
        //System.out.println(locations.size());
        // if there is only 1 node in the list
        if (locations.size() < 2) {
            switch (lang) {
                case "Eng":
                    directions.add("Please enter a valid start and end location to display locations");
                    break;
                case "Span":
                    directions.add("Por favor, ingrese una ubicación inicial y final");
                    break;
                case "Port":
                    directions.add("Insira um local inicial e final para obter instruções");
                    break;
                case "Chin":
                    directions.add("请输入开始和结束位置以获取路线");
                    break;
            }
            dirList.setItems(directions);
            // if there are multiple nodes in the list
        } else {
            double preAngle = 90; // start facing top of the map
            double currAngle = 0;
            double turn;
            // go though all locations
            for (int loc = 0; loc < locations.size(); loc++) {


                // if the node is the last node
                if (loc == locations.size() - 1) {
                    switch (lang) {
                        case "Eng":
                            directions.add("You have reached your destination");
                            break;
                        case "Span":
                            directions.add("Ten llegado a tu destino");
                            break;
                        case "Port":
                            directions.add("Você chegou ao seu destino");
                            break;
                        case "Chin":
                            directions.add("你已到达目的地");
                            break;
                        default:
                            directions.add("proceed to destination");
                    }
                    // if the node is not the last node
                } else {
                    if (samepath) {
                        pixelCounter += (int) locations.get(loc).lengthTo(locations.get(loc + 1));
//                        System.out.println(pixelCounter);
                    } else {
                        pixelCounter = (int) locations.get(loc).lengthTo(locations.get(loc + 1));
//                        System.out.println(pixelCounter);
                    }
                    String distance = " In " + (int) (pixelCounter / Main.h.getPixelsPerFeet()) + " ft\n";
                    // get the current angle of the node
                    currAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                    // from current facing position calculate turn
                    turn = (currAngle - preAngle + 360 + 90) % 360;
                    // as long as this isn't the first node
                    if (loc != 0) { //TODO change if we ever add start orientation
                        // if this is an elevator or stair case
                        if (enterElevator == false && h.getAllCategories().contains(locations.get(loc).getCategory()) && (locations.get(loc).getCategory().getCategory().equalsIgnoreCase("Elevator")
                                || (locations.get(loc).getCategory().getCategory().equalsIgnoreCase("Stairs")))) {
                            enterElevator = true;
                            //languages other than english currently only specify elevators
                            switch (lang) {
                                case "Eng":
                                    directions.add(distance + "Take " + locations.get(loc).getCategory().getCategory() + " to Floor " + h.getFloorById(locations.get(loc + 1).getFloorID()).getFloorNum());
                                    break;
                                case "Span":
                                    directions.add("Toma el ascensor hasta piso " + h.getFloorById(locations.get(loc + 1).getFloorID()).getFloorNum());
                                    break;
                                case "Port":
                                    directions.add("Pegue o elevador até o chão " + h.getFloorById(locations.get(loc + 1).getFloorID()).getFloorNum());
                                    break;
                                case "Chin":
                                    directions.add("把电梯带到地板上 " + h.getFloorById(locations.get(loc + 1).getFloorID()).getFloorNum());
                                    break;
                                default:
                                    directions.add("take " + locations.get(loc).getCategory().getCategory() + " to Floor " + h.getFloorById(locations.get(loc + 1).getFloorID()).getFloorNum());
                                    break;
                            }
                            //directions.add("take " + locations.get(loc).getCategory().getCategory() + " to Floor " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                        }
                        // if this not an elevator or stair case
                        else {
                            enterElevator = false;
                            if (samepath == false && locations.get(loc).getNeighbors().stream().filter(elm -> elm.getCategory().getCategory().equalsIgnoreCase("Hall")).collect(Collectors.toList()).size() < 2) {


                                switch (lang) {
                                    case "Eng":
                                        directions.add("Continue on same path");
                                        break;
                                    case "Span":
                                        directions.add("Continuar por el mismo camino");
                                        break;
                                    case "Port":
                                        directions.add("Continue no mesmo caminho");
                                        break;
                                    case "Chin":
                                        directions.add("在同一路径上继续");
                                        break;
                                    default:
                                        directions.add("Continue on same path");
                                        break;
                                }

                                samepath = true;
                            } else {
                                samepath = false;
                                String turnD = getTurn(turn);


                                if (straight == false && (turnD == "Go straight" || turnD == "Derecho" || turnD == "Siga em frente" || turnD == "笔直走")) {
                                    //System.out.println("found 1 go straight");
                                    straight = true;
                                    directions.add(distance + getTurn(turn));
                                } else {
                                    if (turnD != "Go straight" && turnD != "Derecho" && turnD != "Siga em frente" && turnD != "笔直走") {
                                        straight = false;
                                    }
                                }
                                if (straight == false) {
                                    directions.add(distance + getTurn(turn));
                                }
                            }
                        }
                    }

                }
                // set the current angle to the previous angle
                preAngle = currAngle;
            }
            dirList.setItems(directions);
            QRgen();
        }
    }

    private String getTurn(double turn) {
        /*
        0 right
        45 slight right
        90 straight
        135 slight left
        180 left
        225 backwards slight left
        270 backwards
        315 backwards slight right
        */
        if (turn > 315 + 22.5 || turn <= 22.5) {
            switch (lang) {
                case "Eng":
                    return "Take right";
                case "Span":
                    return "Toma un derecho";
                case "Port":
                    return "Tome un direito";
                case "Chin":
                    return "你对吧";
                default:
                    return "Take right";
            }

        }
        if (turn > 22.5 && turn <= 45 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Take slight right";
                case "Span":
                    return "Tomar ligeramente a la derecha";
                case "Port":
                    return "Leve ligeiramente à direita";
                case "Chin":
                    return "采取轻微的权利";
            }
            return "Take slight right";
        }
        if (turn > 45 + 22.5 && turn <= 90 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Go straight";
                case "Span":
                    return "Derecho";
                case "Port":
                    return "Siga em frente";
                case "Chin":
                    return "笔直走";
            }
            return "Go straight";
        }
        if (turn > 90 + 22.5 && turn <= 135 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Take slight left";
                case "Span":
                    return "Tomar ligeramente a la izquierda";
                case "Port":
                    return "Leve ligeiramente à esquerda";
                case "Chin":
                    return "轻轻一点";
            }
            return "Take slight left";
        }
        if (turn > 135 + 22.5 && turn <= 180 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Take left";
                case "Span":
                    return "gire a izquierda";
                case "Port":
                    return "Pegue un esquerda";
                case "Chin":
                    return "拿左";
            }
            return "Take left";
        }
        if (turn > 180 + 22.5 && turn <= 225 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Back and slight left";
                case "Span":
                    return "Atrás e izquierda ligera";
                case "Port":
                    return "Costas e esquerda ligeira";
                case "Chin":
                    return "背部和轻微的左";
            }
            return "Back and slight left";
        }
        if (turn > 225 + 22.5 && turn <= 270 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Go backwards";
                case "Span":
                    return "Dar marcha atrás";
                case "Port":
                    return "Ir para trás";
                case "Chin":
                    return "倒退";
            }
            return "Go backwards";
        }
        if (turn > 270 + 22.5 && turn <= 315 + 22.5) {
            switch (lang) {
                case "Eng":
                    return "Back and slight right";
                case "Span":
                    return "Atrás y ligero derecho";
                case "Port":
                    return "Costas e ligeira direita";
                case "Chin":
                    return "回来和轻微的权利";
            }
            return "Back and slight right";
        }

        return "";
    }

    private double getAngle(Location curNode, Location nextNode) {
        return (((Math.atan2(curNode.getY() - nextNode.getY(), nextNode.getX() - curNode.getX())) * 180 / Math.PI) + 360) % 360;
    }


    public void QRgen() {
        //String details = "";
        //System.out.print("QRGEN!");
        String textdir = new String();
        for (int j = 0; j < dirList.getItems().size(); j++) {
            textdir += (dirList.getItems().get(j) + "\n");
        }
        ByteArrayOutputStream out = QRCode.from(textdir).to(ImageType.JPG).stream();
        //System.out.print(out);
        File f = new File("qrcode.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(out.toByteArray());
            fos.flush();
            String imagepath = f.toString();
            FileInputStream input = new FileInputStream("qrcode.jpg");
            Image image = new Image(input);
            qrcode.setImage(image);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHandicapped() {
        if (handicapped.isSelected()) {
            h.setHandicapped(1);
        } else {
            h.setHandicapped(0);
        }

    }

}