package groupg.database;

import javafx.scene.paint.Color;

/**
 * @author Saul Woolf
 * @since 2017-04-07
 */
public class Category {
    private String category = "";
    private int permission = -1;
    private String color;

    public Category(String category, int permission, String color) {
        if(color.charAt(0) == '#'){
            color = color.substring(1);
            color = "0x" + color;
        }
        this.category = category;
        this.permission = permission;
        this.color = color;
    }
    public Category(String category, int permission) {
        this.category = category;
        this.permission = permission;
        this.color = "0xffffff";
    }

    @Override
    public String toString() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Category &&
               ((Category) o).getCategory().equals(getCategory()) &&
               ((Category) o).getPermission() == getPermission();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
//        System.out.println("Changing category label from this.category " + this.category + " to " + category);
        //We shouldnt have a category set itself
        this.category = category;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
