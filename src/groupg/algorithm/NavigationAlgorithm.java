package groupg.algorithm;

/**
 * @author Ryan Benasutti
 * @since 2017-04-11
 */
public enum NavigationAlgorithm {
    A_STAR("A-Star"), DEPTH_FIRST("Depth First"), BREADTH_FIRST("Breadth First");

    private final String val;

    NavigationAlgorithm(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }
}
