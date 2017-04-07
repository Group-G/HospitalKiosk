package groupg.jfx;

/**
 * @author Ryan Benasutti
 * @since 2017-04-06
 */
public class Tuple<A, B> {
    public A left;
    public B right;

    public Tuple(A left, B right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return tuple.left.equals(left);
    }

    public A getLeft() {
        return left;
    }

    public void setLeft(A left) {
        this.left = left;
    }

    public B getRight() {
        return right;
    }

    public void setRight(B right) {
        this.right = right;
    }
}
