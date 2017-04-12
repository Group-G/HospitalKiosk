
package groupg.controller;

import groupg.algorithm.NavigationAlgorithm;
import groupg.algorithm.NavigationFacade;
import groupg.database.EmptyLocation;
import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.database.LocationDecorator;
import groupg.jfx.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class WelcomeScreenController implements Initializable {
    @FXML
    private Button loginBtn, searchBtn;
    @FXML
    private HBox startFieldHBox, endFieldHBox;
    @FXML
    private ListView<String> dirList;
    @FXML
    private GridPane canvasWrapper;
    private Pane lineOverlay;
    private ObservableList<UniqueLine> displayedLines = FXCollections.observableArrayList();
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
    private TitledPane wareaPane, bathPane, hdeskPane, exitPane, docPane, officePane;
    @FXML
    private ListView<Location> waitAreaLV, bathroomLV, helpDeskLV, exitsLV, doctorLV, officeLV;
    @FXML
    private TabPane tabPane;
    private Tab selectedTab;
    private String lang = "Eng";

    public WelcomeScreenController() {
        startField = new AutoCompleteTextField();
        startField.setCurrentSelection(new EmptyLocation());
        endField = new AutoCompleteTextField();
        endField.setCurrentSelection(new EmptyLocation());

        List<Location> kioskLocs = HospitalData.getLocationsByCategory("Kiosk");
        if (kioskLocs.size() > 0)
            startField.setCurrentSelection(kioskLocs.get(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pane imageViewPane = new Pane();
        imageViewPane.setPickOnBounds(true);
        lineOverlay = new Pane();
        lineOverlay.setPickOnBounds(true);
        startFieldHBox.getChildren().add(startField);
        endFieldHBox.getChildren().add(endField);

        //Find closest location
        imageViewPane.setOnMouseClicked(event -> {
            double shortest = Double.MAX_VALUE;
            for (Location l : HospitalData.getAllLocations()) {
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
        Group zoomGroup = new Group(imageView, lineOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setPannable(true);
        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE &&
                !(event.getButton() == MouseButton.PRIMARY && event.isControlDown()))
                event.consume();
        });

        pane.addEventFilter(ScrollEvent.SCROLL, e -> {
            if (e.isAltDown()) {
                double zoom_fac = 1.05;
                double delta_y = e.getDeltaY();
                if (delta_y < 0) {
                    zoom_fac = 2.0 - zoom_fac;
                }
                Scale newScale = new Scale();
                newScale.setX(zoomGroup.getScaleX() * zoom_fac);
                newScale.setY(zoomGroup.getScaleY() * zoom_fac);
                zoomGroup.getTransforms().add(newScale);

                System.out.println("the pane hight is" + pane.getHeight());
                System.out.println("the pane width is" + pane.getWidth());
                System.out.println("the zoomgroup hight is" + zoomGroup);
                System.out.println("the zoomgroup width is" + zoomGroup.getScaleY());
                e.consume();
            }
        });
        canvasWrapper.getChildren().addAll(pane);

        HospitalData.getAllFloors().forEach(floor -> {
            Tab tab = new Tab(floor.getFloorNum());
            tab.setOnSelectionChanged(event -> imageView.setImage(ResourceManager.getInstance().loadImage(floor.getFilename())));
            tabPane.getTabs().add(tab);
        });

        //Listener for tab selection change
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            selectedTab = newTab;
            displayedLines.clear();
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(navigation.getPath()
                                                                                                    .stream()
                                                                                                    .filter(elem -> elem.getFloorObj().getFloorNum().equals(newTab.getText()))
                                                                                                    .collect(Collectors.toList())));
            lineOverlay.getChildren().setAll(displayedLines);
        });

        //Default selected tab
        selectedTab = tabPane.getSelectionModel().getSelectedItem();

        //Add locations from DB
        locations.addAll(HospitalData.getAllLocations());
        startField.getEntries().addAll(locations);
        endField.getEntries().addAll(locations);

        //Fill drop downs
        waitAreaLV.getItems().addAll(HospitalData.getLocationsByCategory("Waiting Area"));
        waitAreaLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        waitAreaLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(waitAreaLV.getSelectionModel().getSelectedItem());
        });

        bathroomLV.getItems().addAll(HospitalData.getLocationsByCategory("Bathroom"));
        bathroomLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bathroomLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(bathroomLV.getSelectionModel().getSelectedItem());
        });

        helpDeskLV.getItems().addAll(HospitalData.getLocationsByCategory("Help Desk"));
        helpDeskLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        helpDeskLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(helpDeskLV.getSelectionModel().getSelectedItem());
        });

        exitsLV.getItems().addAll(HospitalData.getLocationsByCategory("Exit"));
        exitsLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        exitsLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(exitsLV.getSelectionModel().getSelectedItem());
        });

        doctorLV.getItems().addAll(HospitalData.getLocationsByCategory("Doctor"));
        doctorLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        doctorLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(doctorLV.getSelectionModel().getSelectedItem());
        });

        officeLV.getItems().addAll(HospitalData.getLocationsByCategory("Office"));
        officeLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        officeLV.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2)
                endField.setCurrentSelection(officeLV.getSelectionModel().getSelectedItem());
        });

        dirList.setCellFactory(list -> new ListCell<String>() {
            {
                Text text = new Text();
                text.wrappingWidthProperty().bind(list.widthProperty().subtract(15));
                text.textProperty().bind(itemProperty());
                setPrefWidth(0);
                setGraphic(text);
            }
        });
    }

    private void drawPath() {
        if (startField.getCurrentSelection() != null && endField.getCurrentSelection() != null) {
            navigation = new NavigationFacade();

            final List<LocationDecorator> output = new ArrayList<>();

            //Default algorithm
            if (AdminMainController.selectedAlgorithm == null)
                AdminMainController.selectedAlgorithm = NavigationAlgorithm.A_STAR;

            //Use proper algorithm
            switch (AdminMainController.selectedAlgorithm) {
                case A_STAR:
                    output.addAll(navigation.runAstar(startField.getCurrentSelection(), endField.getCurrentSelection()));
                    break;

                case DEPTH_FIRST:
                    output.addAll(navigation.runDepthFirst(startField.getCurrentSelection(), endField.getCurrentSelection()));
                    break;
            }

            //Filter out locations not on this floor
            try {
                //Highlight tabs with paths
                tabPane.getTabs().parallelStream().forEach(elem -> {
                    elem.setStyle("");
                    if (output.parallelStream()
                              .map(item -> item.getFloorObj().getFloorNum())
                              .collect(Collectors.toList())
                              .contains(elem.getText())) {
                        elem.setStyle("-fx-background-color: #f4f142");
                    }
                });

                //Filter output based on current tab
                List<LocationDecorator> tempList = output.stream()
                                                         .filter(elem -> elem.getFloorObj().getFloorNum().equals(selectedTab.getText()))
                                                         .collect(Collectors.toList());

                //Move filtered items back
                output.clear();
                output.addAll(tempList);
//                for (int i = 0; i < tempList.size(); i++) {
//                    output.set(i, tempList.get(i));
//                }
            } catch (NullPointerException e) {
                System.out.println("nullptr while filtering to draw");
            }
            displayedLines.clear();
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(output.stream()
                                                                                                .map(elem -> (Location) elem)
                                                                                                .collect(Collectors.toList())));
            generateTextDirections(output.stream()
                    .map(elem -> (Location) elem)
                    .collect(Collectors.toList()));

            lineOverlay.getChildren().setAll(displayedLines);

        }
    }

    // TODO check for adjacent nodes before instructing a turn
    private void generateTextDirections(List<Location> locations) {
        ObservableList<String> directions = FXCollections.observableArrayList();

            if (locations.size() < 2) {
                switch (lang) {
                    case "Eng":
                        directions.add("Please enter a start and end location to display locations");
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

            } else {
                double preAngle = 90; // start facing top of the map
                double curaAngle = 0;
                double turn;
                for (int loc = 0; loc < locations.size(); loc++) {
                    if (loc == locations.size() - 1) {
                        switch ( lang){
                            case "Eng":
                                directions.add("you have reached your destination");
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
                                directions.add("you have reached your destination");
                        }
                    } else {
                        if (HospitalData.getAllCategories().contains(locations.get(loc).getCategory())
                                && (locations.get(loc).getCategory().getCategory().equalsIgnoreCase("Elevator")
                                    || (locations.get(loc).getCategory().getCategory().equalsIgnoreCase("Stairs")))) {

                        }
                        curaAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                        turn = (curaAngle - preAngle + 360 + 90) % 360;
                        if (loc != 0) { //TODO change if we ever add start orientation
                            directions.add(getTurn(turn));
                        }
                    }
                    preAngle = curaAngle;
                }
                dirList.setItems(directions);
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
            ResourceManager.getInstance().loadFXMLIntoScene("/view/adminLogin.fxml", "Login", loginBtn.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSearch(ActionEvent actionEvent) {
        drawPath();
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
        loginBtn.setText("Login");
        searchBtn.setText("Search");
        wareaPane.setText("Waiting Areas");
        bathPane.setText("Bathrooms");
        hdeskPane.setText("Help Desks");
        exitPane.setText("Exits");
        docPane.setText("Doctors");
        language.setText("Language");
    }

    public void changelangS(ActionEvent actionEvent) {
        lang = "Span";
        start.setText("Comienzo:");
        end.setText("Fin:");
        directions.setText("Direcciones:");
        loginBtn.setText("Inicio");
        searchBtn.setText("Busca");
        wareaPane.setText("Área de Espera");
        bathPane.setText("Baño");
        hdeskPane.setText("Mesa de Ayuda");
        exitPane.setText("Salidas");
        docPane.setText("Doctores");
        language.setText("Idiomas");
    }

    public void changelangP(ActionEvent actionEvent) {
        lang = "Port";
        start.setText("Enceta:");
        end.setText("Fim:");
        directions.setText("Instruções:");
        loginBtn.setText("Entra");
        searchBtn.setText("Busca");
        wareaPane.setText("Área de Espera");
        bathPane.setText("Banheiro");
        hdeskPane.setText("Central de Ajuda");
        exitPane.setText("Saída");
        docPane.setText("Médicos");
        language.setText("Línguas");
    }

    public void changelangC(ActionEvent actionEvent) {
        lang = "Chin";
        start.setText("开始");
        end.setText("终点");
        directions.setText("说明");
        loginBtn.setText("注册");
        searchBtn.setText("搜查");
        wareaPane.setText("等候区");
        bathPane.setText("盥洗室");
        hdeskPane.setText("帮助台");
        exitPane.setText("紧急出口");
        docPane.setText("医生");
        language.setText("语");
    }
}
