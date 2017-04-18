package groupg.database;

/**
 * @author Ryan Benasutti
 * @since 2017-04-07
 */
public class EmptyCategory extends Category {
    public EmptyCategory(String category, int permission) {
        super(category, permission, "#ffffff");
    }

    public EmptyCategory() {
        super("", 0, "#ffffff");
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
