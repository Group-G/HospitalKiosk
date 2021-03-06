package groupg.jfx;

import groupg.controller.WelcomeScreenController;
import groupg.database.Location;
import groupg.database.Person;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static groupg.Main.h;

/**
 * Created by will on 4/28/17.
 */
public class UFuniqueNode extends Group {
    private final double radius;
    private WelcomeScreenController welcomeScreenController;
    private String color;
    private Location location;
    private double nodeOffset;
    private Group nodeOverlay;
    private Circle c;
    private boolean highlighted;
    private String lang;
    Label peop = new Label("People at this location:");
    Button navTo = new Button("Go to here!");
    Button navFrom = new Button("Start from here!");
    public UFuniqueNode(double radius, Location location, double nodeOffset, WelcomeScreenController welcomeScreenController) {
        this.radius = radius;
        this.welcomeScreenController = welcomeScreenController;
        c = new Circle(location.getX() * nodeOffset, location.getY() * nodeOffset, radius);
        this.getChildren().add(c);
        this.nodeOffset = nodeOffset;
        this.location = location;
        color = location.getCategory().getColor();
        c.setFill(Color.web(color));
        c.setStroke(Color.web("0x00000099"));
        c.setStrokeWidth(3);
    }


//    public void setUnhighlighted() {
//        color = location.getCategory().getColor();
//        color = color.substring(0, 8) + "99";
//        setFill(Color.web(color));
//        setStroke(Color.web("0x00000099"));
//        setStrokeWidth(1);
//    }
//
//    public void setHighlighted() {
//
//        color = location.getCategory().getColor();
//        color = color.substring(0, 8) + "ff";
//        setFill(Color.web(color));
//        setStroke(Color.web("0x000000"));
//        setStrokeWidth(3);
//    }

    public Location getLocation() {
        return location;
    }

//    public void setLocation(Location location) {
//        this.location = location;
//    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }

    public VBox makeDialog(WelcomeScreenController controller) {
        VBox vbox = new VBox();

        vbox.setMinHeight(410);
        vbox.setPadding(new Insets(3, 10, 10, 10));
        vbox.setSpacing(5);

        //Add buttons
        //System.out.println(welcomeScreenController.getlang());

        navTo.setOnAction(actionEvent -> {
            controller.endField.setCurrentSelection(location);
            welcomeScreenController.openMenu();
        });


        navFrom.setOnAction(actionEvent -> {
            controller.startField.setCurrentSelection(location);
            welcomeScreenController.openMenu();
        });
        HBox btns = new HBox();
        navTo.setPrefWidth(120);
        navFrom.setPrefWidth(120);
        Region r = new Region();
        r.setPrefWidth(20);
        btns.getChildren().addAll(navTo,r, navFrom);
        vbox.getChildren().addAll(btns);

        //Add loc name
        Label nameL = new Label(location.getName() );
        nameL.setFont(Font.font(20));
        vbox.getChildren().add(nameL);

        //Add loc cat
        Label catL = new Label(location.getCategory().toString());
        catL.setFont(Font.font(16));
        vbox.getChildren().add(catL);

        Label blankl = new Label("      ");
        blankl.setFont(Font.font(16));
        vbox.getChildren().add(blankl);


        //Add people at this loc
        List<Label> people = new ArrayList<>();
        for (Person per : h.getAllPeople()) {
            for (Integer l : per.getLocations()) {
                if (location.equals(h.getLocationById(l)))
                    people.add(new Label(per.getName() + ", " + per.getTitle()));
            }
        }

        if (people.size() > 0) {

            vbox.getChildren().add(peop);

            ListView<Label> nlable = new ListView<>();
            nlable.getItems().addAll(people);
            vbox.getChildren().addAll(nlable);
        }

        return vbox;
    }

    public Circle getCircle() {
       // System.out.println("UFuniqueNode.getCircle");
        return c;
    }

    public void setHighlighted(boolean h) {
        this.highlighted = h;
        if (this.highlighted) {
            c.setRadius(this.radius + 10);
            c.setStrokeWidth(5);
        } else {
            c.setRadius(this.radius);
            c.setStrokeWidth(3);
        }
    }
    public void checklang(){
       lang = welcomeScreenController.getlang();
    }
    public void updatelang(){
        checklang();
        if(lang.equals("Eng")){
            peop.setText("People at this location:");
        }
        else if(lang.equals("Span")){
            peop.setText("Gente en este lugar:");
        }
        else if(lang.equals("Port")){
            peop.setText("Pessoas neste local:");
        }
        else if(lang.equals("Chin")){
            peop.setText("在这个位置的人：");
        }

        if(lang.equals("Eng")){
            navTo.setText("Go to here!");
        }
        else if(lang.equals("Span")){
            navTo.setText("¡Vete aquí!");
        }
        else if(lang.equals("Port")){
            navTo.setText("Vá até aqui!");
        }
        else if(lang.equals("Chin")){
            navTo.setText("去这里！");
        }

        if(lang.equals("Eng")){
            navFrom.setText("Start from here!");
        }
        else if(lang.equals("Span")){
            navFrom.setText("Comienza desde aquí!");
        }
        else if(lang.equals("Port")){
            navFrom.setText("Comece aqui!");
        }
        else if(lang.equals("Chin")){
            navFrom.setText("从这里开始!");
        }
    }
}
