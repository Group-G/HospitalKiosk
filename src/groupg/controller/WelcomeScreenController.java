package groupg.controller;

import groupg.Main;
import groupg.algorithm.NavigationAlgorithm;
import groupg.algorithm.NavigationFacade;
import groupg.database.*;
import groupg.jfx.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static groupg.Main.h;

/**
 * Created by will on 4/21/17.
 */
public class WelcomeScreenController implements Initializable {
    @FXML
    private Label lblFloor;
    @FXML
    private GridPane Floorselectgrid;
    @FXML
    private Group mapGroup, menuGroup, textFieldGroup;
    @FXML
    private ImageView imageViewBase;
    @FXML
    private Pane mapPane, menuPane, fadePane, searchPane, FloorSelectPane;
    @FXML
    private VBox VBoxSelectPane, fieldsBox, superbox;
    @FXML
    private Button menuBtn, loginBtn, aboutBtn, searchBtn, viewButton, menuExitBtn, directionBtn,swapBtn;
    @FXML
    private AnchorPane LayerA, LayerB, LayerC, LayerD;
    @FXML
    private MenuButton language;
    @FXML
    private MenuItem english, spanish, portugues, chinese;
    @FXML
    private Accordion acccordionDropDown;
    @FXML
    private VBox dirBox;
    public AutoCompleteTextField searchField, startField, endField;
    Scale scale = new Scale();
    double WINDOW_WIDTH = 0, WINDOW_HEIGHT = 0;
    static List<UniqueFloor> FaulknerFloors = new ArrayList<>();
    boolean onScreen = false;
    private static int permission = 0;
    int currentFloor = 7;
    private boolean menuOpen = false;
    List<Floor> floors = Main.h.getAllFloors();
    private String lang = "Eng";
    private ListView<String> dirList;
    private ImageView qrcode = new ImageView();
    @FXML
    private CheckBox handicapped;
    @FXML
    private Text errorText;
    @FXML
    private ToggleButton handiBtn;
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    NavigationFacade navigation;
    Group GroundLines = new Group();
    private static  LocalTime currentTime = LocalTime.now();
    private static  LocalDate currentDate = LocalDate.now();
    private static  DayOfWeek dow = currentDate.getDayOfWeek();
    @FXML
    private HBox menuItems;
    private boolean searched = false;





    public static void setPermission(int p) {
        permission = p;
    }

    public static LocalTime getCurrentTime() {
        return currentTime;
    }

    public static DayOfWeek getDow(){
        return dow;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(WINDOW_WIDTH != 0){
            menuPane.setPrefHeight((double) WINDOW_WIDTH);
            fadePane.setPrefHeight((double) WINDOW_WIDTH);
        }



        navigation = new NavigationFacade();

        dirList = new ListView<>();

        dirList.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(final String item, final boolean empty) {
                super.updateItem(item, empty);

                // if null, display nothing
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                setText(null);
                final HBox hbox = new HBox();

                Text text = new Text(item);
                hbox.setMaxWidth(dirList.getPrefWidth());
                text.wrappingWidthProperty().bind(hbox.widthProperty().subtract(10));
                text.textProperty().bind(itemProperty());
                setPrefWidth(0);
                setPrefHeight(60);

                Label iconLabel = new Label();
                String url;
                if (item.contains("same") || item.contains("straight")) {
                    url = "/image/directions/forward.png";
                }
                else if (item.contains("left")){
                    url = "/image/directions/left.png";
                }
                else if (item.contains("right")){
                    url = "/image/directions/right.png";
                }
                else if (item.contains("back")){
                    url = "/image/directions/backward.png";
                }
                else if (item.contains("stairs")){
                    url = "/image/directions/upstair.png";
                }
                else if (item.contains("reached")){
                    url = "/image/directions/destination.png";
                }
                else if (item.contains("levator")) {
                    url = "/image/directions/elevator.jpg";
                } else {
                    url = "/image/directions/startlocation.png";
                }

                iconLabel.setGraphic(new ImageView(ResourceManager.getInstance().loadImage(url,20, 20, false, false)));
                iconLabel.setPadding(new Insets(0,5,0,0));

                hbox.getChildren().addAll(iconLabel, text);
                setGraphic(hbox);
            }
        });
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
        startField.setPrefHeight(40);
        endField.setPrefHeight(40);

        List<Location> kioskLocs = h.getLocationsByCategory("Kiosk");

        searchField.setOnKeyPressed(key -> {

            if (key.getCode() == KeyCode.ENTER) {
                if (searchField.getCurrentSelection().getX() != 0) {
                    onSearch(searchField.getCurrentSelection());
                }
            }
        });

        startField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                if (startField.getCurrentSelection().getX() != 0) {
                    onSearch(startField.getCurrentSelection());
                }
            }
        });

        qrcode.setOnMouseClicked(event ->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Lightbox.fxml"));
                loader.load();
                Lightbox controller = loader.getController();
                 controller.showOn(LayerA,qrcode);
                //Save changes to disk
            }
            catch (IOException ex) {
                // なんか適当にエラー処理でも
                Logger.getLogger(AdminMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        endField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                if (endField.getCurrentSelection().getX() != 0) {
                    onSearch(endField.getCurrentSelection());
                }
            }
        });

        searchBtn.setOnAction(event -> {
            if (searchField.getCurrentSelection().getX() != 0) {
                onSearch(searchField.getCurrentSelection());
            }
        });

        swapBtn.setOnAction(event -> {
            String start = startField.getText();
            String end = endField.getText();
            startField.setText(end);
            endField.setText(start);
        });
        ToggleGroup group = new ToggleGroup();
        handiBtn.setToggleGroup(group);
        handiBtn.setSelected(false);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null){
                    h.setWantStairs(0);
                    System.out.println(h.getWantStairs());}
                else{
                    h.setWantStairs(1);
                    System.out.println(h.getWantStairs());}
            }
        });
        directionBtn.setOnAction(event -> {
            if (startField.getCurrentSelection().getX() == 0 || endField.getCurrentSelection().getX() == 0) {
                return;
            }
            //System.out.println("endField = " + endField.getCurrentSelection().getID());
            //System.out.println("endField = " + endField.getCurrentSelection().getX());
            drawPath();
            exitMenu();
            searched = true;
            unhighLightNodes();
            for(UniqueFloor uf : FaulknerFloors){
                if(uf.getFloor().getID() == startField.getCurrentSelection().getFloorID()){
                    focusFloor(uf);
                }
            }
        });
        english.setOnAction(event ->{
            lang="Eng";
            lblFloor.setText("Floor");
            language.setText("English");
            language.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/america.png")));
        });
        spanish.setOnAction(event ->{
            lang="Span";
            language.setText("Español");
            lblFloor.setText("Piso");
            language.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/spain.png")));
        });
        portugues.setOnAction(event ->{
            lang="Port";
            language.setText("Português");
            lblFloor.setText("Chão");
            language.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/portugal.png")));
        });
        chinese.setOnAction(event ->{
            lang="Chin";
            language.setText("中文");
            lblFloor.setText("地板");
            language.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/china.png")));
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
        Accordion allperson = new Accordion();
        Collections.sort(Main.h.getAllPeople());
        for (Person p : Main.h.getAllPeople()) {
            ListView<Location> perloc = new ListView<>();
            for (Integer lid : p.getLocations()) {
                //System.out.println(p.getName());
                //System.out.println("!!!!!!!!!!!!!!!" +Main.h.getLocationById(lid));
               // System.out.println("!!!!!!!" +lid);
                perloc.getItems().add(Main.h.getLocationById(lid));
                perloc.setPrefHeight(60);
                perloc.setOnMouseClicked((MouseEvent event) -> {
                    if (event.getClickCount() == 2) {
                        endField.setCurrentSelection(perloc.getSelectionModel().getSelectedItem());
                    }
                });
            }
            TitledPane person = new TitledPane(p.getName(), perloc);
            allperson.getPanes().add(person);
        }

        TitledPane quickSearch = new TitledPane("Quick Search", quickList);
        acccordionDropDown.getPanes().add(quickSearch);
        ScrollPane spane = new ScrollPane(allperson);
        TitledPane Personel = new TitledPane("Personel", spane);
        acccordionDropDown.getPanes().add(Personel);


        for (Category category : h.getAllCategories()) {
            if (category.getCategory().contains("Hall") || category.getCategory().contains("Elevator") || category.getCategory().contains("Stairs") || category.getCategory().contains("Kiosk")) {
            } else {
                if (category.getPermission() <= permission){
                    ListView<Location> locByCat = new ListView<>();
                    locByCat.getItems().addAll(h.getLocationsByCategory(category.getCategory()));
                    locByCat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    TitledPane t =new TitledPane(category.getCategory() + " ", locByCat);
                    acccordionDropDown.getPanes().addAll(t);
                    Circle r = new Circle(5);
                    r.setStroke(Color.BLACK);
                    r.setFill(Paint.valueOf(category.getColor()));
                    t.setGraphic(r);
                    t.setContentDisplay(ContentDisplay.RIGHT);
                    locByCat.setOnMouseClicked((MouseEvent event) -> {
                   //     System.out.println(startField.getText().trim().isEmpty() && endField.getText().isEmpty());
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
            openMenu();
        });

        menuExitBtn.setOnAction(event -> {
            exitMenu();
        });
        //TODO ggroupnd tliems

        mapGroup.getChildren().add(GroundLines);
        List<Floor> floors = Main.h.getAllFloors();
        int fa = 0, b = 0;
        for (int i = 0; i < floors.size(); i++) {
            Floor f = floors.get(i);
            if (f.getBuildingID() == 1) {
                fa++;
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 1313, 1090, 1310, -1600, fa, this);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked(event -> {
                 //   System.out.println("Faulkner!");
                    removefloors();
                    event.consume();
                    flipToFloor(uf.getFloorIndex());
                    zoomFloor(uf);
                    getfloors(f.getBuildingID());
                });
            } else if (f.getBuildingID() == 0) {
                b++;
                UniqueFloor uf = new UniqueFloor(f, mapGroup, 3351, 2618, 3351, -700, b, this);
                FaulknerFloors.add(uf);
                uf.getImageView().setOnMouseClicked(event -> {
                //    System.out.println("BELKIN!");
                    removefloors();
                    event.consume();
                    flipToFloor(uf.getFloorIndex());
                    zoomFloor(uf);
                    getfloors(f.getBuildingID());
                });
            }
        }
        UniqueFloor bRoof = new UniqueFloor(new Floor(0, "/image/FaulknerMaps/BelkinR.png", "Belkin Roof"), mapGroup, 3350, 2625, 3350, -700, b+1, this);
        bRoof.getImageView().setOnMouseClicked(event -> {
           // System.out.println("BELKIN!");
            removefloors();
            event.consume();
            flipToFloor(bRoof.getFloorIndex());
            zoomFloor(bRoof);
            getfloors(bRoof.getFloor().getBuildingID());
        });
        UniqueFloor fRoof = new UniqueFloor(new Floor(1, "/image/FaulknerMaps/FaulknerR.png", "Faulkner Roof"), mapGroup, 1313, 1090, 1310, -1600, fa+1, this);
        fRoof.getImageView().setOnMouseClicked(event -> {
         //   System.out.println("FAULKNER!");
            removefloors();
            event.consume();
            flipToFloor(fRoof.getFloorIndex());
            zoomFloor(fRoof);
            getfloors(fRoof.getFloor().getBuildingID());
        });
        FaulknerFloors.add(bRoof);
        FaulknerFloors.add(fRoof);

        mapGroup.getTransforms().add(scale);
//        System.out.println(mapPane.getWidth());
        mapPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //mapPane.setMaxWidth(anchorPane.getMaxWidth());
        mapPane.setOnMouseClicked((MouseEvent event) -> {
           // System.out.println("clicked the pane!!!!");
            setMenuFill(new VBox());
            resetZoom(WINDOW_WIDTH, 1250);
//            zoomFloor(FaulknerFloors.get(0));
            flipToFloor(8);
            FloorSelectPane.setVisible(false);
        });

        mapPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            WINDOW_WIDTH = (double) newValue;
            resetZoom(WINDOW_WIDTH, 1);
            fadePane.setPrefWidth((double) newValue);
            if (kioskLocs.size() > 0) {
                startField.setCurrentSelection(kioskLocs.get(0));
            }
//            scale.setPivotX(-(double)newValue/2);
//            fadeRect.setWidth((double)newValue);

        });

        mapPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            WINDOW_HEIGHT = (double) newValue;
            menuPane.setPrefHeight((double) newValue);
            fadePane.setPrefHeight((double) newValue);
//            scale.setPivotY(-(double)newValue/2);
//            fadeRect.setHeight((double)newValue);


        });


        viewButton.setOnAction(event -> {
            if (kioskLocs.size() > 0) {
                onSearch(kioskLocs.get(0));
            }
        });

        aboutBtn.setOnAction(event -> {
            exitMenu();
            try {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/aboutscreen.fxml", "Welcome", aboutBtn.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loginBtn.setOnAction(event -> {
            exitMenu();
            try {
                ResourceManager.getInstance().loadFXMLIntoScene("/view/adminLogin.fxml", "Welcome", aboutBtn.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




        //set button graphics
        viewButton.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/location.png", 30, 30, false, false)));
        // upButton.setGraphic(new ImageView(new Image("/image/Icons/zoom_in.png",30, 30, false, false)));
        //downButton.setGraphic(new ImageView(new Image("/image/Icons/zoom_out.png",30, 30, false, false)));
        searchBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/search.png", 30, 30, false, false)));
        menuBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/menu.png", 30, 30, false, false)));
        loginBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/admin.png", 30, 30, false, false)));
        aboutBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/info.png", 30, 30, false, false)));
        menuExitBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/close.png", 30, 30, false, false)));
        language.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/america.png"))); //default as english
        english.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/america.png")));
        spanish.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/spain.png")));
        portugues.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/portugal.png")));
        chinese.setGraphic(new ImageView(ResourceManager.getInstance().loadImageNatural("/image/Icons/china.png")));
        swapBtn.setGraphic(new ImageView( new Image("/image/Icons/swap.png",20,20,false,false)));
        directionBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/search.png",20, 20, false, false)));
        handiBtn.setGraphic(new ImageView(ResourceManager.getInstance().loadImage("/image/Icons/stairs.png",20, 20, false, false)));
    }

    public void openMenu() {
        if (!menuOpen) {
            setMenuFill(new VBox());
            menuOpen = true;
            menuPane.setVisible(true);
            menuPane.setPickOnBounds(true);
            fadePane.setVisible(true);
            moveMenu(true, 25).play();
        }
    }

    private void focusFloor(UniqueFloor uf) {
        flipToFloor(uf.getFloorIndex());
        zoomFloor(uf);
        getfloors(uf.getFloor().getBuildingID());
    }

    private void exitMenu() {
        if (menuOpen) {
//                menuPane.setVisible(false);
            menuPane.setPickOnBounds(false);
            menuOpen = false;
            fadePane.setVisible(false);
            moveMenu(false, 25).play();

        }
    }

    private void zoomFloor(UniqueFloor uf) {
//        double scaleValH = mapPane.getWidth()/uf.getImageView().getImage().getWidth()*.7;
        double scaleValV = mapPane.getHeight() / uf.getImageView().getImage().getHeight();
        double scaleVal = scaleValV;



        double xoffset = mapPane.getWidth() * 0.3;
        double yoffset = mapPane.getHeight() * 0.2;
        if(uf.getFloor().getBuildingID() == 1){
            yoffset = mapPane.getHeight() * 0.5;
        }

//        xoffset = 0;
//        yoffset = 0;
        //System.out.println();

        scaleImage(uf.getGroup().getTranslateX() - xoffset, uf.getGroup().getTranslateY() - yoffset, scaleVal, 1250, true).play();
    }

    private void setHighlight(int floorindex) {
        //getNodeFromGridPane(Floorselectgrid, 0, floorindex-1).setStyle("-fx-background-color:#dddddd;");
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
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
        if (index <= 7  && index >= 0) {
           // System.out.println("Flipping to floor " + index);
            currentFloor = index;
            setHighlight(currentFloor);
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


    private Animation animateCircle(Circle c, List<Location> l, String lastCategory) {

        String thisCategory = l.get(0).getCategory().getCategory();

        switch(Main.h.getFloorById(l.get(0).getFloorID()).getBuildingID()){
            case -2:
                c.setFill(Color.web("#0000ff"));
                break;
            case 1:
                c.setFill(Color.web("#00ff00"));
                break;
            case 0:
                c.setFill(Color.web("#ff0000"));
                break;
            default:
                c.setFill(Color.web("#ffffff"));
                break;
        }

        UniqueFloor uf = getUf(l.get(0));
        if(uf == null){
            flipToFloor(7);
        }
        else{
            flipToFloor(uf.getFloorIndex());
        }

        double startX = c.getCenterX();
        double startY = c.getCenterY();

        double targetX = l.get(0).getX();
        double targetY = l.get(0).getY();

        double difx = targetX-startX;
        double dify = targetY-startY;

        double dif = Math.sqrt(difx*difx+dify*dify);


        final Animation expandPanel = new Transition() {
            {
                if((lastCategory.equals("Elevator") && thisCategory.equals("Elevator")) ||(lastCategory.equals("Stairs") && thisCategory.equals("Stairs") )){
                    setCycleDuration(Duration.millis(15*dif));
                }
                else{
                    setCycleDuration(Duration.millis(3*dif));
                }

            }

            @Override
            protected void interpolate(double fraction) {

//                double newX = curX + fraction * (x - curX);
//                group.setTranslateX(newX);
//                double newY = curY + fraction * (y - curY);
//                group.setTranslateY(newY);

                double newX = startX+difx*fraction;
                double newY = startY+dify*fraction;
                c.setCenterX(newX);
                c.setCenterY(newY);
            }
        };

        expandPanel.setOnFinished(e -> {
            if(l.size()>1){
                l.remove(0);
                animateCircle(c, l, thisCategory).play();
            }
        });
        return expandPanel;
    }

    private UniqueFloor getUf(Location l) {
        for(UniqueFloor uf : FaulknerFloors){
            if(l.getFloorID() == uf.getFloor().getID()){
                return uf;
            }
        }
        return null;
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
        double curX = -mapGroup.getTranslateX()/scaleIn;
        double curY = -mapGroup.getTranslateY()/scaleIn;


        mapGroup.getTransforms().add(scale);

        final Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(time));
            }

            @Override
            protected void interpolate(double fraction) {

                mapGroup.getTransforms().clear();
                mapGroup.getTransforms().add(scale);

                double newY = curY + fraction * (y - curY);
                double newX = curX + fraction * (x - curX);
                double newScale = curScale + fraction * (scaleIn - curScale);

                mapGroup.setTranslateX(-newX *scaleIn);
                mapGroup.setTranslateY(-newY *scaleIn);


                scale.setX(newScale);
                scale.setY(newScale);


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
        scaleImage(0, 0, newScale, time, false).play();
    }

    public void onSearch(Location searchNode) {
        unhighLightNodes();
       // System.out.println("Faulkner!");

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
                              //  System.out.println("FOUND ID");
                                uf.displayNodeInfo(uf.getPoints().get(i));

//                                uf.getPoints().get(i).getCircle().setFill(Color.RED);
                            }
                        }
                        //System.out.println(uf.getGroup().getChildren().size());
                    }
                }
            }

        }

    }

    public void animatePath(List<LocationDecorator> path, double time){
        final Group group = new Group();
        final Circle pathCircle = new Circle(path.get(0).getX(), path.get(0).getY(), 8);
        pathCircle.setFill(Color.DARKRED);
        final Path animPath = new Path();
        for (int i=1;i<path.size();i++){
            animPath.getElements().add(new MoveTo(path.get(i).getX(),path.get(i).getY()));
        }

        group.getChildren().add(animPath);
        group.getChildren().add(pathCircle);

        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(time));
        pathTransition.setDelay(Duration.seconds(.1));
        pathTransition.setPath(animPath);
        pathTransition.setNode(pathCircle);
        pathTransition
                .setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        pathTransition.play();
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
            }
            else {
                for (Floor floor : Main.h.getAllFloors()) {
                    if (hm.containsKey(floor) && (floor.getBuildingID()==start.getBuilding()) ) {
                        return hm.get(floor);
                    }
                }
                for (Floor floor : Main.h.getAllFloors()) {
                    if (hm.containsKey(floor)){
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
            //QRgen();
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
            h.setWantStairs(0);
        } else {
            h.setWantStairs(1);
        }

    }

    private void drawPath() {
        if (startField.getCurrentSelection().getX() != 0 && endField.getCurrentSelection().getX() != 0) {

            final List<LocationDecorator> output = new ArrayList<>();
            //Default algorithm
            if (AdminMainController.selectedAlgorithm == null)
                AdminMainController.selectedAlgorithm = NavigationAlgorithm.A_STAR;
            //Use proper algorithm
            switch (AdminMainController.selectedAlgorithm) {
                case A_STAR:
                    output.addAll(navigation.runAstar(startField.getCurrentSelection(),
                            endField.getCurrentSelection()));
                    break;
                case DEPTH_FIRST:
                    output.addAll(navigation.runDepthFirst(startField.getCurrentSelection(),
                            endField.getCurrentSelection()));
                    break;
                case BREADTH_FIRST:
                    output.addAll(navigation.runBreadthFirst(startField.getCurrentSelection(),
                            endField.getCurrentSelection()));
                    break;
            }
            int startfloorID = startField.getCurrentSelection().getFloorID();
            int endfloorID = endField.getCurrentSelection().getFloorID();


            generateTextDirections(output.stream()
                    .map(elem -> (Location) elem)
                    .collect(Collectors.toList()));


            List<Location> GroundNodes = new ArrayList<>();

            for (Location l: output) {
                if (Main.h.getFloorById(l.getFloorID()).getBuildingID() == -2) {
                    GroundNodes.add(l.makeCopy());
                }
            }

            for (UniqueFloor uf: FaulknerFloors){
                uf.getPath().clear();
                for (Location l: output) {
                    if (l.getFloorID() == uf.getFloor().getID()){
                        uf.getPath().add(l);
                    }
                }

                uf.adjustPath();
                displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(uf.getPath()));
                uf.getLines().getChildren().setAll(displayedLines);
            }

            for (Location l: GroundNodes) {
                l.setX((int)(l.getX()*2.79));
                l.setY((int)(l.getY()*2.79));
            }
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(GroundNodes));
            GroundLines.getChildren().setAll(displayedLines);
            //mapGroup.getChildren().add(new Circle(1000,1000, 400));
            setMenuFill(getDisplayDirections());

            double groundOffsetX = 10;
            double faulknerOffsetX = 10;
            double benkinOffsetX = 20;
            double groundOffsetY = 10;
            double faulknerOffsetY = 10;
            double benkinOffsetY = 20;

            for (UniqueFloor uf : FaulknerFloors){
                if(uf.getFloor().getBuildingID() == -2){
                    groundOffsetX = uf.getOffsetX();
                    groundOffsetY = uf.getOffsetY();
                }
                if(uf.getFloor().getBuildingID() == 0){
                    faulknerOffsetX = uf.getOffsetX();
                    faulknerOffsetY = uf.getOffsetY();
                }
                if(uf.getFloor().getBuildingID() == 1){
                    benkinOffsetX = uf.getOffsetX();
                    benkinOffsetY = uf.getOffsetY();
                }

            }


            List<Location> lCopy = new ArrayList<>();
            for(Location l: output){

                Location newL = l.makeCopy();
                switch(Main.h.getFloorById(newL.getFloorID()).getBuildingID()) {
                    case -2:
                        newL.setX((int)((newL.getX()*2.79)));
                        newL.setY((int)((newL.getY()*2.79)));
                        break;
                    case 0:
                        newL.setX((int)((newL.getX()*.56)+3351));
                        newL.setY((int)((newL.getY()*.56)+2618));
                        break;
                    case 1:
                        newL.setX((int)((newL.getX()*1.285)+1313));
                        newL.setY((int)((newL.getY()*1.285)+1090));
                        break;
                    default:
                        newL.setX((int)((newL.getX()*1.285)));
                        newL.setY((int)((newL.getY()*1.285)));
                        break;
                }
                lCopy.add(newL);

            }
            Circle c = new Circle(lCopy.get(0).getX(), lCopy.get(0).getX(), 15);
            lCopy.remove(0);

            mapGroup.getChildren().add(c);
            animateCircle(c, lCopy, "doesnt fuckin matter").play();
        }
    }


    private VBox getDisplayDirections(){
        //Pane dirPane = new Pane();
        QRgen();
        VBox p =new VBox();
        //Pane dicks = new Pane();
        //dicks.getChildren().add(qrcode);
        //dicks.setPadding(new Insets(20, 0, 20, 0));
        //dicks.getChildren().add(dirList);
        p.setPrefHeight(LayerA.getHeight());
        dirList.setMinHeight(LayerA.getHeight()/2);
       // System.out.println("p.getPrefHeight() = " + p.getPrefHeight());
        //dirPane.getChildren().add();
       // System.out.println("dirList = " + dirList.getHeight());
        p.getChildren().add(dirList);
        p.getChildren().add(qrcode);
        p.setAlignment(Pos.TOP_CENTER);
        dirList.setPrefWidth(dirBox.getWidth()-100);
        menuItems.setStyle("-fx-padding: 0 0 20 0;");
        qrcode.setStyle("-fx-padding: 20 0 0 0;");
        return p;
    }

    public void setMenuFill(VBox menuFill) {
        if(menuFill.getChildren().size() == 0){
            searched = false;
        }
        dirBox.getChildren().clear();
        dirBox.getChildren().add(menuItems);
        dirBox.getChildren().add(menuFill);
       // System.out.println("menuFill.getHeight() = " + menuFill.getHeight());
    }

    public void unhighLightNodes() {
        for(UniqueFloor uf : FaulknerFloors){
            uf.unhighlightNodes();
        }
    }
    public boolean getSearched(){
        return this.searched;
    }
    public void setSearched(boolean s){
        this.searched = s;
    }




}