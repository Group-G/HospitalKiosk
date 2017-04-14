package groupg.database;

import javafx.scene.paint.Color;

/**
 * @author Saul Woolf
 * @since 2017-04-07
 */
public class Category {
    private String category = "";
    private int permission = -1;
    private Color color;

    public Category(String category, int permission) {
        this.category = category;
        this.permission = permission;
        color = Color.BLACK.deriveColor(1, 1, 1, 0.3);
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
        System.out.println("Changing category label from this.category " + this.category + " to " + category);

        this.category = category;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
