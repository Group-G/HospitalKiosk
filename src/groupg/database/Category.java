package groupg.database;

/**
 * Created by svwoolf on 4/7/17.
 */
public class Category {
    String category = "";
    int permission = -1;

    public Category(String category, int permission){
        this.category = category;
        this.permission = permission;
    }


    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
