
package groupg.controller;

import groupg.algorithm.Astar;
import groupg.database.Category;
import groupg.database.EmptyLocation;
import groupg.database.HospitalData;
import groupg.database.Location;
import groupg.jfx.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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
    private Astar astar;
    private LinkedList<Location> locations = new LinkedList<>();
    //added for Language change
    @FXML
    private MenuButton language;
    @FXML
    private MenuItem english,spanish,chinese,portugues;
    @FXML
    private Label start, end;
    @FXML
    private Text directions;
    @FXML
    private TitledPane WArea,Bath,Hdesk,exit,doc;
    @FXML
    private Tab fl1,fl2,fl3,fl4,fl5,fl6,fl7;
    private String lang = "Eng";
    double SCALE_DELTA = 1.1;
    double SCALE_TOTAL = 1;


    public WelcomeScreenController() {
        startField = new AutoCompleteTextField();
        startField.setCurrentSelection(new EmptyLocation());
        endField = new AutoCompleteTextField();
        endField.setCurrentSelection(new EmptyLocation());

        List<Location> kioskLocs = HospitalData.getLocationsByCategory("Kiosk");
        if (kioskLocs.size() > 0) {
            startField.setCurrentSelection(kioskLocs.get(0));
            startField.setText(kioskLocs.get(0).getName());
        }
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
                }
                else {
                    Double newShortest = l.lengthTo(new EmptyLocation(event.getX(), event.getY()));
                    if (newShortest < shortest) {
                        shortest = newShortest;
                        closestLocToClick = l;
                    }
                }
            }
        });


        imageView = ImageViewFactory.getImageView(ResourceManager.getInstance().loadImage("/image/faulkner_1.png"), imageViewPane);
        Group zoomGroup = new Group(imageView, lineOverlay);
        ScrollPane pane = new ScrollPane(new Pane(zoomGroup));
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setPannable(true);
        pane.addEventFilter(ScrollEvent.SCROLL, e -> {
            if (e.isAltDown()) {
                double zoom_fac = 1.05;
                double delta_y = e.getDeltaY();

                if(delta_y < 0) {
                    zoom_fac = 2.0 - zoom_fac;
                }

                Scale newScale = new Scale();
                newScale.setX( zoomGroup.getScaleX() * zoom_fac );
                newScale.setY( zoomGroup.getScaleY() * zoom_fac );
                zoomGroup.getTransforms().add(newScale);
                e.consume();
            }
        });

        zoomGroup.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE &&
                !(event.getButton() == MouseButton.PRIMARY && event.isControlDown()))
                event.consume();
        });
        canvasWrapper.getChildren().addAll(pane);

//        canvasWrapper.add(canvas, 0, 0);
//        canvasWrapper.add(overlay, 0, 0);

        //Add locations from DB
        locations.addAll(HospitalData.getAllLocations());
        startField.getEntries().addAll(locations);
        endField.getEntries().addAll(locations);

        drawPath();


    }

    private void drawPath() {
        if (startField.getCurrentSelection() != null && endField.getCurrentSelection() != null) {
            LinkedList<Location> locsIn = new LinkedList<>();
            locsIn.addAll(HospitalData.getAllLocations());

            astar = new Astar(locsIn);

            List<Location> output = new ArrayList<>();
            output.addAll(astar.run(startField.getCurrentSelection(), endField.getCurrentSelection()));
            displayedLines.clear();
            displayedLines = FXCollections.observableArrayList(DrawLines.drawLinesInOrder(output));
            lineOverlay.getChildren().setAll(displayedLines);

            generateTextDirections(output);
        }
    }
    // TODO check for adjacent nodes before instructing a turn

    private void generateTextDirections(List<Location> locations) {
        String directions = "";

        dirList.setWrapText(true);
        if(lang.equals("Eng")) {
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
        else if(lang.equals("Span")){
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
        else if(lang.equals("Port")){
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
        else if(lang.equals("Chin")){
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
        }
        else{
            System.out.println("UNSPACIFIED LANGUAGE");
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
    private String getTurn(double turn){
        if(turn > 315+22.5 || turn <= 22.5){
            if(lang.equals("Eng")){
                return "take right \n";
            }
            else if(lang.equals("Span")){
                return "Toma el derecho \n";
            }
            else if(lang.equals("Port")){
                return "Você toma direito \n";
            }
            else if(lang.equals("Chin")){
                return "你对吧 \n";
            }
            else{
                return "take right \n";
            }

        }
        if(turn > 22.5 && turn <= 45+22.5){
            if(lang.equals("Eng")){
                return "take slight right \n";
            }
            else if (lang.equals("Span")){
            return "Tomar ligeramente a la derecha \n";
            }
            else if (lang.equals("Port")){
            return "Leve ligeiramente à direita \n";
            }
            else if (lang.equals("Chin")){
            return "采取轻微的权利 \n";
            }
            return "take slight right \n";
        }
        if(turn > 45+22.5 && turn <= 90+22.5){
            if(lang.equals("Eng")){
                return "go straight \n";
            }
            else if(lang.equals("Span")){
            return "ir directamente \n";
            }
            else if (lang.equals("Port")){
            return "Siga em frente \n";
            }
            else if(lang.equals("Chin")){
                return "笔直走 \n";
            }
            return "go straight \n";
        }
        if(turn > 90+22.5 && turn <= 135+22.5){
            if(lang.equals("Eng")){
                return "take slight left \n";
            }
            else if(lang.equals("Span")){
                return "Tomar ligeramente a la izquierda \n";
            }
            else if (lang.equals("Port")){
                return "Leve ligeiramente à esquerda \n";
            }
            else if(lang.equals("Chin")){
                return "轻轻一点 \n";
            }
            return "take slight left \n";
        }
        if(turn > 135+22.5 && turn <= 180+22.5){
            if(lang.equals("Eng")){
                return "take left \n";
            }
            else if(lang.equals("Span")){
                return "gire a la izquierda \n";
            }
            else if (lang.equals("Port")){
                return "Pegue a esquerda \n";
            }
            else if(lang.equals("Chin")){
                return "拿左 \n";
            }
            return "take left \n";
        }
        if(turn > 180+22.5 && turn <= 225+22.5){
            if(lang.equals("Eng")){
                return "back and slight left \n";
            }
            else if(lang.equals("Span")){
                return "Atrás e izquierda ligera \n";
            }
            else if (lang.equals("Port")){
                return "Costas e esquerda ligeira \n";
            }
            else if(lang.equals("Chin")){
                return "背部和轻微的左 \n";
            }
            return "back and slight left \n";
        }
        if(turn > 225+22.5 && turn <= 270+22.5){
            if(lang.equals("Eng")){
                return "go backwards \n";
            }
            else if(lang.equals("Span")){
                return "dar marcha atrás \n";
            }
            else if (lang.equals("Port")){
                return "ir para trás \n";
            }
            else if(lang.equals("Chin")){
                return "倒退 \n";
            }
            return "go backwards \n";
        }
        if(turn > 270+22.5 && turn <= 315+22.5){
            if(lang.equals("Eng")){
                return "back and slight right \n";
            }
            else if(lang.equals("Span")){
                return "Atrás y ligero derecho \n";
            }
            else if (lang.equals("Port")){
                return "Costas e ligeira direita \n";
            }
            else if(lang.equals("Chin")){
                return "回来和轻微的权利 \n";
            }
            return "back and slight right \n";
        }
        // should never reach here
        return "i dont know which direciotn you should go please seek help imediatly!!!! \n";
    }

    private double getAngle(Location curNode, Location nextNode) {
        double angle = (((Math.atan2(curNode.getY()-nextNode.getY(), nextNode.getX()-curNode.getX()))*180/Math.PI)+360)%360;
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
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_1.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor2(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_2.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor3(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_3.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor4(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_4.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor5(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_5.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor6(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_6.png"));
        } catch (NullPointerException e) {
        }
    }

    public void onFloor7(Event event) {
        try {
            imageView.setImage(ResourceManager.getInstance().loadImage("/image/faulkner_7.png"));
        } catch (NullPointerException e) {
        }
    }

    public void changelangE(ActionEvent actionEvent){
        //System.out.println("changed to english");
        lang = "Eng";
    start.setText("Start:");
    end.setText("End:");
    directions.setText("Directions:");
    loginBtn.setText("Login");
    searchBtn.setText("Search");
    WArea.setText("Waiting Areas");
    Bath.setText("Bathrooms");
    Hdesk.setText("Help Desks");
    exit.setText("Exits");
    doc.setText("Doctors");
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
    public void changelangS(ActionEvent actionEvent){
       // System.out.println("changed to spanish");
        lang = "Span";
        start.setText("Comienzo:");
        end.setText("Fin:");
        directions.setText("Direcciones:");
        loginBtn.setText("Inicio");
        searchBtn.setText("Busca");
        WArea.setText("Área de Espera");
        Bath.setText("Baño");
        Hdesk.setText("Mesa de Ayuda");
        exit.setText("Salidas");
        doc.setText("Doctores");
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
    public void changelangP(ActionEvent actionEvent){
        //System.out.println("changed to portuguese");
        lang="Port";
        start.setText("Enceta:");
        end.setText("Fim:");
        directions.setText("Instruções:");
        loginBtn.setText("Entra");
        searchBtn.setText("Busca");
        WArea.setText("Área de Espera");
        Bath.setText("Banheiro");
        Hdesk.setText("Central de Ajuda");
        exit.setText("Saída");
        doc.setText("Médicos");
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
    public void changelangC(ActionEvent actionEvent){
        //System.out.println("changed to chineee");
        lang = "Chin";
        start.setText("开始");
        end.setText("终点");
        directions.setText("说明");
        loginBtn.setText("注册");
        searchBtn.setText("搜查");
        WArea.setText("等候区");
        Bath.setText("盥洗室");
        Hdesk.setText("帮助台");
        exit.setText("紧急出口");
        doc.setText("医生");
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
