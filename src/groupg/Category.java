package groupg;

/**
 * Created by AlazarGenene on 4/1/17.
 */
public class Category {
    private String categoryName;
    private Location location;

    /*CONSTRUCTORS*/

    /*GETTERS*/
    public Location getLocation() {
        return this.location;
    }

    public String getCategoryName() {
        return this.categoryName;
    }
    /*END GETTERS*/

    /*SETTERS*/
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    /*END SETTERS*/

}
