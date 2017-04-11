
package groupg.controller;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
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
    private TextArea dirList;
    @FXML
    private GridPane canvasWrapper;
    private ResizableCanvas canvas = new ResizableCanvas();
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
    private Tab fl1, fl2, fl3, fl4, fl5, fl6, fl7;
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
        //disable writing in the text area
        dirList.setDisable(true);
        //add automatic textfields
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
        canvasWrapper.getChildren().addAll(pane);

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

        //Add locations from DB
        locations.addAll(HospitalData.getAllLocations());
        startField.getEntries().addAll(locations);
        endField.getEntries().addAll(locations);

        //Fill dropdowns
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

        drawPath();
    }

    private void drawPath() {
        if (startField.getCurrentSelection() != null && endField.getCurrentSelection() != null) {
            navigation = new NavigationFacade();

            List<LocationDecorator> output = new ArrayList<>();
            output.addAll(navigation.runAstar(startField.getCurrentSelection(), endField.getCurrentSelection()));
            //Filter out locations not on this floor
            try {
                output = output.stream()
                               .filter(elem -> elem.getFloorObj().getFloorNum().equals(selectedTab.getText()))
                               .collect(Collectors.toList());
            } catch (NullPointerException e) {
                System.out.println("nullptr while filtering to draw");
            }
            displayedLines.clear();
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(output.stream()
                                                                                                .map(elem -> (Location) elem)
                                                                                                .collect(Collectors.toList())));
            lineOverlay.getChildren().setAll(displayedLines);

            generateTextDirections(output.stream()
                                         .map(elem -> (Location) elem)
                                         .collect(Collectors.toList()));
        }
    }

    // TODO check for adjacent nodes before instructing a turn
    private void generateTextDirections(List<Location> locations) {
        String directions = "";

        dirList.setWrapText(true);
        if (lang.equals("Eng")) {
            if (locations.size() < 2) {
                dirList.setText("Please enter a start and end location to display locations");
            } else {
                dirList.setText("Printing Directions...\n\n");
                //dirList.setText(directions);
                double preAngle = 90; // start facing top of the map
                double curaAngle = 0;
                double turn = 0;
                for (int loc = 0; loc < locations.size(); loc++) {
                    if (loc == locations.size() - 1) {
                        directions += "you have reached your destination\n";
                    } else {
                        curaAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                        turn = (curaAngle - preAngle + 360 + 90) % 360;
                        directions += getTurn(turn);
                    }
                    preAngle = curaAngle;
                }
                dirList.setText(directions);
            }
        }

        //SPANISH
        else if (lang.equals("Span")) {
            if (locations.size() < 2) {
                dirList.setText("Por favor, ingrese una ubicación inicial y final para obtener direcciones");
            } else {
                dirList.setText("Direcciones de impresión ...\n\n");
                //dirList.setText(directions);
                double preAngle = 90; // start facing top of the map
                double curaAngle = 0;
                double turn = 0;
                for (int loc = 0; loc < locations.size(); loc++) {
                    if (loc == locations.size() - 1) {
                        directions += "ha llegado a su destino\n";
                    } else {
                        curaAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                        turn = (curaAngle - preAngle + 360 + 90) % 360;
                        directions += getTurn(turn);
                    }
                    preAngle = curaAngle;
                }
                dirList.setText(directions);
            }
        }

        //PORTUGUESE
        else if (lang.equals("Port")) {
            if (locations.size() < 2) {
                dirList.setText("Insira um local inicial e final para obter instruções");
            } else {
                dirList.setText("Direcciones de impresión ...\n\n");
                //dirList.setText(directions);
                double preAngle = 90; // start facing top of the map
                double curaAngle = 0;
                double turn = 0;
                for (int loc = 0; loc < locations.size(); loc++) {
                    if (loc == locations.size() - 1) {
                        directions += "Você chegou ao seu destino\n";
                    } else {
                        curaAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                        turn = (curaAngle - preAngle + 360 + 90) % 360;
                        directions += getTurn(turn);
                    }
                    preAngle = curaAngle;
                }
                dirList.setText(directions);
            }
        }

        //CHINESE
        else if (lang.equals("Chin")) {
            if (locations.size() < 2) {
                dirList.setText("请输入开始和结束位置以获取路线");
            } else {
                dirList.setText("打印方向...");
                //dirList.setText(directions);
                double preAngle = 90; // start facing top of the map
                double curaAngle = 0;
                double turn = 0;
                for (int loc = 0; loc < locations.size(); loc++) {
                    if (loc == locations.size() - 1) {
                        directions += "你已到达目的地";
                    } else {
                        curaAngle = getAngle(locations.get(loc), locations.get(loc + 1));
                        turn = (curaAngle - preAngle + 360 + 90) % 360;
                        directions += getTurn(turn);
                    }
                    preAngle = curaAngle;
                }
                dirList.setText(directions);
            }
        } else {
            System.out.println("UNSPECIFIED LANGUAGE");
        }
    }

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
    private String getTurn(double turn) {
        if (turn > 315 + 22.5 || turn <= 22.5) {
            if (lang.equals("Eng")) {
                return "take right \n";
            } else if (lang.equals("Span")) {
                return "Toma el derecho \n";
            } else if (lang.equals("Port")) {
                return "Você toma direito \n";
            } else if (lang.equals("Chin")) {
                return "你对吧 \n";
            } else {
                return "take right \n";
            }

        }
        if (turn > 22.5 && turn <= 45 + 22.5) {
            if (lang.equals("Eng")) {
                return "take slight right \n";
            } else if (lang.equals("Span")) {
                return "Tomar ligeramente a la derecha \n";
            } else if (lang.equals("Port")) {
                return "Leve ligeiramente à direita \n";
            } else if (lang.equals("Chin")) {
                return "采取轻微的权利 \n";
            }
            return "take slight right \n";
        }
        if (turn > 45 + 22.5 && turn <= 90 + 22.5) {
            if (lang.equals("Eng")) {
                return "go straight \n";
            } else if (lang.equals("Span")) {
                return "ir directamente \n";
            } else if (lang.equals("Port")) {
                return "Siga em frente \n";
            } else if (lang.equals("Chin")) {
                return "笔直走 \n";
            }
            return "go straight \n";
        }
        if (turn > 90 + 22.5 && turn <= 135 + 22.5) {
            if (lang.equals("Eng")) {
                return "take slight left \n";
            } else if (lang.equals("Span")) {
                return "Tomar ligeramente a la izquierda \n";
            } else if (lang.equals("Port")) {
                return "Leve ligeiramente à esquerda \n";
            } else if (lang.equals("Chin")) {
                return "轻轻一点 \n";
            }
            return "take slight left \n";
        }
        if (turn > 135 + 22.5 && turn <= 180 + 22.5) {
            if (lang.equals("Eng")) {
                return "take left \n";
            } else if (lang.equals("Span")) {
                return "gire a la izquierda \n";
            } else if (lang.equals("Port")) {
                return "Pegue a esquerda \n";
            } else if (lang.equals("Chin")) {
                return "拿左 \n";
            }
            return "take left \n";
        }
        if (turn > 180 + 22.5 && turn <= 225 + 22.5) {
            if (lang.equals("Eng")) {
                return "back and slight left \n";
            } else if (lang.equals("Span")) {
                return "Atrás e izquierda ligera \n";
            } else if (lang.equals("Port")) {
                return "Costas e esquerda ligeira \n";
            } else if (lang.equals("Chin")) {
                return "背部和轻微的左 \n";
            }
            return "back and slight left \n";
        }
        if (turn > 225 + 22.5 && turn <= 270 + 22.5) {
            if (lang.equals("Eng")) {
                return "go backwards \n";
            } else if (lang.equals("Span")) {
                return "dar marcha atrás \n";
            } else if (lang.equals("Port")) {
                return "ir para trás \n";
            } else if (lang.equals("Chin")) {
                return "倒退 \n";
            }
            return "go backwards \n";
        }
        if (turn > 270 + 22.5 && turn <= 315 + 22.5) {
            if (lang.equals("Eng")) {
                return "back and slight right \n";
            } else if (lang.equals("Span")) {
                return "Atrás y ligero derecho \n";
            } else if (lang.equals("Port")) {
                return "Costas e ligeira direita \n";
            } else if (lang.equals("Chin")) {
                return "回来和轻微的权利 \n";
            }
            return "back and slight right \n";
        }
        // should never reach here
        return "i dont know which direciotn you should go please seek help imediatly!!!! \n";
    }

    private double getAngle(Location curNode, Location nextNode) {
        double angle = (((Math.atan2(curNode.getY() - nextNode.getY(), nextNode.getX() - curNode.getX())) * 180 / Math.PI) + 360) % 360;
        return angle;
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
        //System.out.println("changed to english");
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
        fl1.setText("Floor 1");
        fl2.setText("Floor 2");
        fl3.setText("Floor 3");
        fl4.setText("Floor 4");
        fl5.setText("Floor 5");
        fl6.setText("Floor 6");
        fl7.setText("Floor 7");
        dirList.setText("Please Enter a Starting and Ending Location to obtain directions");
    }

    public void changelangS(ActionEvent actionEvent) {
        // System.out.println("changed to spanish");
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
        fl1.setText("Piso 1");
        fl2.setText("Piso 2");
        fl3.setText("Piso 3");
        fl4.setText("Piso 4");
        fl5.setText("Piso 5");
        fl6.setText("Piso 6");
        fl7.setText("Piso 7");
        dirList.setText("Por favor, ingrese una ubicación inicial y final para obtener direcciones");
    }

    public void changelangP(ActionEvent actionEvent) {
        //System.out.println("changed to portuguese");
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
        fl1.setText("Andar 1");
        fl2.setText("Andar 2");
        fl3.setText("Andar 3");
        fl4.setText("Andar 4");
        fl5.setText("Andar 5");
        fl6.setText("Andar 6");
        fl7.setText("Andar 7");
        dirList.setText("Insira um local inicial e final para obter instruções");

    }

    public void changelangC(ActionEvent actionEvent) {
        //System.out.println("changed to chineee");
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
        fl1.setText("1楼");
        fl2.setText("2楼");
        fl3.setText("3楼");
        fl4.setText("4楼");
        fl5.setText("5楼");
        fl6.setText("6楼");
        fl7.setText("7楼");
        dirList.setText("请输入开始和结束位置以获取路线");

    }


}
