
package groupg.controller;

import groupg.Main;
import groupg.algorithm.NavigationAlgorithm;
import groupg.algorithm.NavigationFacade;
import groupg.database.*;

import groupg.jfx.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static groupg.Main.h;
import static groupg.Main.main;
//import static groupg.controller.AdminMainController.infoOverlay;
//import static groupg.controller.AdminMainController.nodeOverlay;

public class WelcomeScreenController implements Initializable {
    @FXML
    private Button loginBtn, searchBtn;
    @FXML
    private HBox startFieldHBox, endFieldHBox;
    @FXML
    private ListView<String> dirList;
    @FXML
    private GridPane canvasWrapper;
    public static Pane imageViewPane, nodeOverlay, lineOverlay, infoOverlay;
    public static ObservableList<UniqueNode> displayedNodes = FXCollections.observableArrayList();
    public static ObservableList<Circle> displayedCircles = FXCollections.observableArrayList();
    public static ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
    public static ObservableList<PropertyDisplay> displayedPanels = FXCollections.observableArrayList();
    private Location closestLocToClick;
    private ImageView imageView;
    private AutoCompleteTextField startField, endField;
    private NavigationFacade navigation;
    private LinkedList<Location> locations = new LinkedList<>();
    @FXML
    private MenuButton language;
    @FXML
    private MenuItem english, spanish, chinese, portugues;
    @FXML
    private Label start, end;
    @FXML
    private Text directions;
    @FXML
    private Accordion acccordionDropDown;
    @FXML
    private TitledPane wareaPane, bathPane, hdeskPane, exitPane, docPane, officePane;
    @FXML
    private ListView<Location> waitAreaLV, bathroomLV, ServicesLV, exitsLV, doctorLV, officeLV;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button AboutBtn;
    @FXML
    private VBox sidebar;
    @FXML
    private ImageView qrcode;
    private Tab selectedTab;
    private static Floor currentFloor;
    private String lang = "Eng";
    private static int permission = 0;
    private List<Tab> tabList = new ArrayList<>();
    private boolean searched = false;

    @FXML
    private CheckBox handicapped;

    public static void setPermission(int p){
        permission = p;
    }

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

//    public void checkTestBox(){
//        CodeArea codeArea = new CodeArea();
//
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acccordionDropDown.getPanes().clear();
        EmptyLocation empty = new EmptyLocation();
        for (Category category : h.getAllCategories()) {
            if (category.getPermission() <= permission) {
                ListView<Location> locByCat = new ListView();
                locByCat.getItems().addAll(h.getLocationsByCategory(category.getCategory()));
                locByCat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                acccordionDropDown.getPanes().addAll(new TitledPane(category.getCategory() + " "/* +category.getPermission()*/, locByCat));
                locByCat.setOnMouseClicked((MouseEvent event) -> {
                    if(event.getClickCount() == 2 && startField.getText().isEmpty() && endField.getText().isEmpty()){
                        startField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                    }
                    else if(event.getClickCount() == 2 && startField.getText().isEmpty() && !(endField.getText().isEmpty())){
                        startField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                    }
                    else if(event.getClickCount() == 2 && !(startField.getText().isEmpty()) && endField.getText().isEmpty()) {
                        endField.setCurrentSelection(locByCat.getSelectionModel().getSelectedItem());
                    }
                });
            }



        File qrcode = new File("qrcode.jpg");
        boolean exists = qrcode.exists();
        if(exists){
            qrcode.delete();
        }
        Pane imageViewPane = new Pane();
        //displayedNodes.addListener((ListChangeListener<UniqueNode>) c -> nodeOverlay.getChildren().setAll(displayedNodes));
        imageViewPane.setPickOnBounds(true);
        nodeOverlay = new Pane();
        nodeOverlay.setPickOnBounds(true);
        lineOverlay = new Pane();
        lineOverlay.setPickOnBounds(true);
        //infoOverlay = new Pane();
        //infoOverlay.setPickOnBounds(true);
        //displayedPanels.set


        startFieldHBox.getChildren().add(startField);
        endFieldHBox.getChildren().add(endField);
        Application.setUserAgentStylesheet(getClass().getResource("/view/welcomescreen.css").toExternalForm());
        startField.getStyleClass().add("startfield");
        endField.getStyleClass().add("endfield");
        //Find closest location
        imageViewPane.setOnMouseClicked(event -> {
            double shortest = Double.MAX_VALUE;
            for (Location l : h.getAllLocations()) {
                if (closestLocToClick == null) {
                    closestLocToClick = l;
                } else {
                    Double newShortest = l.lengthTo(new EmptyLocation(event.getX(), event.getY()));
                    if (newShortest < shortest) {
                        shortest = newShortest;
                        closestLocToClick = l;
                    }
                }
            }
        });

        imageView = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage("/image/faulkner_1_cropped.png"), imageViewPane);
        Group zoomGroup = new Group(imageView,lineOverlay,nodeOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setPannable(true);
        Scale newScale = new Scale();
        newScale.setX(939/imageView.getImage().getWidth());
        newScale.setY(939/imageView.getImage().getWidth());
        zoomGroup.getTransforms().add(newScale);
        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE &&
                            !(event.getButton() == MouseButton.PRIMARY && event.isControlDown()))
                event.consume();
        });

        pane.addEventFilter(ScrollEvent.SCROLL, e -> {
            if (e.isAltDown()) {
                double zoom_fac = .05;
                double delta_y = e.getDeltaY();
                if (delta_y < 0) {
                    zoom_fac = -zoom_fac;
                }
                double zoomx = zoomGroup.getTransforms().get(0).getMxx();
                double zoomy = zoomGroup.getTransforms().get(0).getMyy();
                if ((imageView.getImage().getWidth() * (zoomx + zoom_fac) >= canvasWrapper.getWidth() && zoom_fac < 0) || (zoom_fac > 0  && (zoomx+zoomx*zoom_fac) <= 1.25)) {
                    zoomGroup.getTransforms().clear();
                    newScale.setX(zoomx + zoomx*zoom_fac);
                    newScale.setY(zoomy + zoomx*zoom_fac);
                    zoomGroup.getTransforms().add(newScale);
                } else if (zoom_fac < 0) {
                    newScale.setX(canvasWrapper.getWidth()/imageView.getImage().getWidth());
                    newScale.setY(canvasWrapper.getWidth()/imageView.getImage().getWidth());
                }
//                pane.setHmax((zoomx + zoomx*zoom_fac));
                //System.out.println("max" + pane.getHmax());
                //System.out.println(pane.getHmin());
                //System.out.println(pane.getHvalue());
                //System.out.println(imageView.getImage().getWidth());
                //System.out.println();
                e.consume();
            }
        });

        canvasWrapper.getChildren().addAll(pane);

        Main.h.getAllFloors().forEach(floor -> {
            Tab tab = new Tab(floor.getFloorNum());
            tab.setOnSelectionChanged(event -> {
                imageView.setImage(ResourceManager.getInstance().loadImage(floor.getFilename()));
                //currentFloor = floor;

                //Set new nodes for this floor
                //displayedNodes.clear();
                //displayedNodes.addAll(floor.getLocations().stream().map(NodeFactory::getpublicNode).collect(Collectors.toList()));
                //nodeOverlay.getChildren().setAll(displayedNodes);

                //Clear lines
                //displayedLines.clear();
                //lineOverlay.getChildren().clear();

                //Clear current selection
                //NodeListenerFactoryLite.currentSelection = null;
            });
            tabList.add(tab);
        });
        updateTabs(new ArrayList<Floor>());

        //Listener for tab selection change
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab != null) {
            selectedTab = newTab;
            displayedLines.clear();
            System.out.println("newTab: " + newTab);
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(navigation.getPath()
                    .stream()
                    .filter(elem -> elem.getFloorObj().getFloorNum().equals(newTab.getText()))
                    .collect(Collectors.toList())));
            lineOverlay.getChildren().setAll(displayedLines);
            //Set new nodes for this floor

            //displayedNodes.addAll(floor.getLocations().stream().map(NodeFactory::getNode).collect(Collectors.toList()));
            //nodeOverlay.getChildren().setAll(displayedNodes);

            //Clear lines
            //displayedLines.clear();
            //lineOverlay.getChildren().clear();

            //Clear current selection
            //NodeListenerFactoryLite.currentSelection = null;
//            if (oldTab != null) {
//                Main.h.getFloorByName(oldTab.getText()).setZoom(zoomGroup.getTransforms().get(0).getMxx());
//            }
            zoomGroup.getTransforms().clear();
            Scale newZoom = new Scale();
            double zoomVal = 1;
                String filename = Main.h.getFloorByName(newTab.getText()).getFilename();
                Image img = ResourceManager.getInstance().loadImage(filename);
                zoomVal = pane.getWidth()/img.getWidth();
                newZoom.setX(zoomVal);
                newZoom.setY(zoomVal);
            zoomGroup.getTransforms().add(newZoom);
        }
        });

        //Default selected tab
       selectedTab = tabPane.getTabs().get(0);

        //Add locations from DB
        locations.addAll(h.getAllLocations());
        startField.getEntries().addAll(locations);
        endField.getEntries().addAll(locations);
        //Fill drop downs
        waitAreaLV.getItems().addAll(h.getLocationsByCategory("Waiting Area"));
        waitAreaLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        waitAreaLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(waitAreaLV.getSelectionModel().getSelectedItem());
        });

        bathroomLV.getItems().addAll(h.getLocationsByCategory("Bathroom"));
        bathroomLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bathroomLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(bathroomLV.getSelectionModel().getSelectedItem());
        });

        ServicesLV.getItems().addAll(h.getLocationsByCategory("Service"));
        ServicesLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ServicesLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(ServicesLV.getSelectionModel().getSelectedItem());
        });

        exitsLV.getItems().addAll(h.getLocationsByCategory("Exit"));
        exitsLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        exitsLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(exitsLV.getSelectionModel().getSelectedItem());
        });

        doctorLV.getItems().addAll(h.getLocationsByCategory("Doctor"));
        doctorLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        doctorLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(doctorLV.getSelectionModel().getSelectedItem());
        });

        officeLV.getItems().addAll(h.getLocationsByCategory("Office"));
        officeLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        officeLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(officeLV.getSelectionModel().getSelectedItem());
        });

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

                iconLabel.setGraphic(new ImageView(new Image(url,20, 20, false, false)));
                iconLabel.setPadding(new Insets(0,5,0,0));

                hbox.getChildren().addAll(iconLabel, text);
                setGraphic(hbox);
            }
        });
        if(permission == 1){
            loginBtn.setText("Logout");
        }
        else if(permission == 0){
            loginBtn.setText("Login");
        }
    }

    private void updateTabs(List<Floor> floors) {
        tabPane.getTabs().clear();
        if (floors.size() == 0) {
            tabPane.getTabs().addAll(tabList);
        } else {
            for (Floor f:floors){
                for (Tab t :tabList) {
                    if (t.getText().equals(f.getFloorNum())){
                        tabPane.getTabs().add(t);
                    }
                }
            }
        }
    }

    private void drawPath() {
        if (searched && startField.getCurrentSelection() != null && endField.getCurrentSelection() != null) {
            final List<LocationDecorator> output = new ArrayList<>();
            final List<LocationDecorator> filtered_output = new ArrayList<>();

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
            output.forEach(e -> {
                filtered_output.add(e);
            });

            generateTextDirections(filtered_output.stream()
                    .map(elem -> (Location) elem)
                    .collect(Collectors.toList()));
            //Filter out locations not on this floor
            try {
                //Highlight tabs with paths
//                tabPane.getTabs().parallelStream().forEach(elem -> {
//                    elem.setStyle("");
//                    if (filtered_output.parallelStream()
//                            .map(item -> item.getFloorObj().getFloorNum())
//                            .collect(Collectors.toList())
//                            .contains(elem.getText())) {
//                        elem.setStyle("-fx-background-color: #f4f142");
//                    }
//                });

                List<Floor> floors = Main.h.getFloorsByIds(filtered_output);

                updateTabs(floors);

                //Filter output based on current tab
                List<LocationDecorator> tempList = filtered_output.stream()
                        .filter(elem -> elem.getFloorObj().getFloorNum().equals(selectedTab.getText()))
                        .collect(Collectors.toList());

                //Move filtered items back
                filtered_output.clear();
                filtered_output.addAll(tempList);

//                for (int i = 0; i < tempList.size(); i++) {
//                    output.set(i, tempList.get(i));
//                }
            } catch (NullPointerException e) {
                System.out.println("nullptr while filtering to draw");
            }
            displayedLines.clear();
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(filtered_output.stream()
                    .map(elem -> (Location) elem)
                    .collect(Collectors.toList())));
            lineOverlay.getChildren().setAll(displayedLines);

        }
    }

    // TODO check for adjacent nodes before instructing a turn
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
                    switch ( lang){
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
                    if(samepath) {
                        pixelCounter += (int) locations.get(loc).lengthTo(locations.get(loc+1));
//                        System.out.println(pixelCounter);
                    }
                    else {
                        pixelCounter = (int) locations.get(loc).lengthTo(locations.get(loc+1));
//                        System.out.println(pixelCounter);
                    }
                    String distance = " In " + (int)(pixelCounter/Main.h.getPixelsPerFeet()) + " ft\n";
                    // get the current angle of the node
                    currAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                    // from current facing position calculate turn
                    turn = (currAngle - preAngle + 360 + 90) % 360;
                    // as long as this isn't the first node
                    if (loc != 0) { //TODO change if we ever add start orientation
                        // if this is an elevator or stair case
                        if (enterElevator == false && h.getAllCategories().contains(locations.get(loc).getCategory())&& (locations.get(loc).getCategory().getCategory().equalsIgnoreCase("Elevator")
                                || (locations.get(loc).getCategory().getCategory().equalsIgnoreCase("Stairs"))))
                        {
                            enterElevator = true;
                            //languages other than english currently only specify elevators
                            switch(lang){
                                case "Eng":
                                    directions.add(distance + "Take " + locations.get(loc).getCategory().getCategory() + " to Floor " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                                    break;
                                case "Span":
                                    directions.add("Toma el ascensor hasta piso " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                                    break;
                                case "Port":
                                    directions.add("Pegue o elevador até o chão " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                                    break;
                                case "Chin":
                                    directions.add("把电梯带到地板上 " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                                    break;
                                default: directions.add("take " + locations.get(loc).getCategory().getCategory() + " to Floor " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                                    break;
                            }
                            //directions.add("take " + locations.get(loc).getCategory().getCategory() + " to Floor " + h.getFloorById(locations.get(loc+1).getFloorID()).getFloorNum());
                        }
                        // if this not an elevator or stair case
                        else
                        {
                            enterElevator = false;
                            if(samepath == false && locations.get(loc).getNeighbors().stream().filter(elm -> elm.getCategory().getCategory().equalsIgnoreCase("Hall")).collect(Collectors.toList()).size() < 2){


                                switch (lang){
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
                            }else {
                                samepath = false;
                                String turnD = getTurn(turn);


                                if ( straight == false && (turnD == "Go straight" ||turnD == "Derecho" || turnD== "Siga em frente" || turnD =="笔直走")) {
                                    //System.out.println("found 1 go straight");
                                    straight = true;
                                    directions.add(distance + getTurn(turn));
                                } else {
                                    if (turnD != "Go straight" && turnD!="Derecho" && turnD !="Siga em frente"&& turnD != "笔直走") {
                                        straight =false;
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

    public void onLogin(ActionEvent actionEvent) {
        try {
            if(permission == 1){
                permission = 0;
                ResourceManager.getInstance().loadFXMLIntoScene("/view/welcomeScreen.fxml", "Login", loginBtn.getScene());
            }else{
//                loginBtn.setText("Logout");
                ResourceManager.getInstance().loadFXMLIntoScene("/view/adminLogin.fxml", "Login", loginBtn.getScene());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAbout(ActionEvent actionEvent) {
        try {
            ResourceManager.getInstance().loadFXMLIntoScene("/view/aboutscreen.fxml", "About", AboutBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSearch(ActionEvent actionEvent) {
        if (searched == true) {
            searched = false;
            updateTabs(new ArrayList<>());
            dirList.getItems().clear();
            qrcode.setVisible(false);
            displayedLines.clear();
            lineOverlay.getChildren().clear();
            searchBtn.setText("Search");
        } else {
            searched = true;
            drawPath();
            searchBtn.setText("Cancel");
            qrcode.setVisible(true);
        }
    }

    public void onFloor1(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_1_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor2(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_2_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor3(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_3_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor4(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_4_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor5(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_5_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor6(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_6_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor7(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_7_cropped.png"));
        } catch (NullPointerException e) {
        }
    }

    public void changelangE(ActionEvent actionEvent) {
        lang = "Eng";
        start.setText("Start:");
        end.setText("End:");
        directions.setText("Directions:");
        if(permission == 0){
            loginBtn.setText("Login");
        }
        else{
            loginBtn.setText("Logout");
        }
        searchBtn.setText("Search");
        wareaPane.setText("Waiting Areas");
        bathPane.setText("Bathrooms");
        hdeskPane.setText("Services");
        exitPane.setText("Exits");
        docPane.setText("Doctors");
        language.setText("Language");
        ObservableList<String> directions = FXCollections.observableArrayList();
        directions.add("Please enter a start and end location to display locations");
        officePane.setText("Offices");
        dirList.setItems(directions);
    }

    public void changelangS(ActionEvent actionEvent) {
        lang = "Span";
        start.setText("Comienzo:");
        end.setText("Fin:");
        directions.setText("Direcciones:");
        if(permission == 0){
            loginBtn.setText("Inicio");
        }
        else{
            loginBtn.setText("Cerrar Sesión");
        }
        searchBtn.setText("Busca");
        wareaPane.setText("Área de Espera");
        bathPane.setText("Baño");
        hdeskPane.setText("Servicios");
        exitPane.setText("Salidas");
        docPane.setText("Doctores");
        language.setText("Idiomas");
        ObservableList<String> directions = FXCollections.observableArrayList();
        directions.add("Por favor, ingrese una ubicación inicial y final");
        officePane.setText("Oficinas");
        dirList.setItems(directions);

    }

    public void changelangP(ActionEvent actionEvent) {
        lang = "Port";
        start.setText("Enceta:");
        end.setText("Fim:");
        directions.setText("Instruções:");
        if(permission == 0){
            loginBtn.setText("Entra");
        }
        else{
            loginBtn.setText("Sair");
        }
        searchBtn.setText("Busca");
        wareaPane.setText("Área de Espera");
        bathPane.setText("Banheiro");
        hdeskPane.setText("Serviços");
        exitPane.setText("Saída");
        docPane.setText("Médicos");
        language.setText("Línguas");
        ObservableList<String> directions = FXCollections.observableArrayList();
        directions.add("Você chegou ao seu destino");
        officePane.setText("Escritórios");
        dirList.setItems(directions);

    }

    public void changelangC(ActionEvent actionEvent) {
        lang = "Chin";
        start.setText("开始");
        end.setText("终点");
        directions.setText("说明");
        if(permission == 0){
            loginBtn.setText("注册");
        }
        else{
            loginBtn.setText("登出");
        }
        searchBtn.setText("搜查");
        wareaPane.setText("等候区");
        bathPane.setText("盥洗室");
        hdeskPane.setText("服务");
        exitPane.setText("紧急出口");
        docPane.setText("医生");
        language.setText("语");
        ObservableList<String> directions = FXCollections.observableArrayList();
        directions.add("请输入开始和结束位置以获取路线");
        officePane.setText("办公室");
        dirList.setItems(directions);
    }


    public void QRgen(){
        //String details = "";
        //System.out.print("QRGEN!");
        String textdir = new String();
        for (int j = 0; j < dirList.getItems().size(); j++) {
            textdir += (dirList.getItems().get(j)+ "\n");
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

    public void setHandicapped(){
        if(handicapped.isSelected()){
            h.setHandicapped(1);
        } else {
            h.setHandicapped(0);
        }

    }

//    public static void updateNodePD() {
//        if (NodeListenerFactory.currentSelection != null) {
//            //Update property display
//            PropertyDisplay pd = displayedPanels.get(0);
//            pd.setProperty("X value", "" + NodeListenerFactoryLite.currentSelection.getLocation().getX());
//            pd.setProperty("Y value", "" + NodeListenerFactoryLite.currentSelection.getLocation().getY());
//            pd.setProperty("Name", NodeListenerFactoryLite.currentSelection.getLocation().getName());
//            pd.setProperty("Category", NodeListenerFactoryLite.currentSelection.getLocation().getCategory().getCategory());
//            pd.setProperty("# of Neighbors", NodeListenerFactoryLite.currentSelection.getLocation().getNeighbors().size() + "");
//            pd.setProperty("ID", "" + NodeListenerFactoryLite.currentSelection.getLocation().getID());
//            pd.setProperty("Permissions", "" + NodeListenerFactoryLite.currentSelection.getLocation().getCategory().getPermission() + "");
//            displayedPanels.set(0, pd);
//            AdminMainController.infoOverlay.getChildren().clear();
//            AdminMainController.infoOverlay.getChildren().addAll(displayedPanels);
//        }
//    }

}
