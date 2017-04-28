package groupg.database;

/**
 * @author Ryan Benasutti
 * @since 2017-04-07
 */
public class EmptyCategory extends Category {
    public EmptyCategory(String category, int permission) {
        super(category, permission, "#000000");
    }

    public EmptyCategory() {
        super("", 0, "#000000");
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
