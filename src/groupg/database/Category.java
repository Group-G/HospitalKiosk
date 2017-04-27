package groupg.database;

/**
 * @author Saul Woolf
 * @since 2017-04-07
 */
public class Category implements Comparable {
    private String category = "";
    private int permission = -1;
    private String color;
    private int quicksearchOn = 0;

    public Category(String category, int permission, String color, int quicksearch) {
        if(color.charAt(0) == '#'){
            color = color.substring(1);
            color = "0x" + color;
        }
        this.category = category;
        this.permission = permission;
        this.color = color;
        this.quicksearchOn = quicksearch;

    }
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
        //category == name
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

    public int getQuicksearchOn() {
        return quicksearchOn;
    }

    public void setQuicksearchOn(int quicksearchOn) {
        this.quicksearchOn = quicksearchOn;
    }

    @Override
    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }
}
