package base.config.general;

/**
 * Created by galafit on 18/8/17.
 */
public class Margin {
    private int top;
    private int right;
    private int bottom;
    private int left;

    public Margin(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public int top() {
        return top;
    }

    public int right() {
        return right;
    }

    public int bottom() {
        return bottom;
    }

    public int left() {
        return left;
    }
}
