package base.config;

/**
 * Created by hdablin on 15.08.17.
 */
public enum Position {
    TOP_LEFT,
    TOP_RIGHT,
    TOP_CENTER,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    BOTTOM_CENTER;

    public boolean isTop(){
        if (this == TOP_LEFT || this == TOP_CENTER || this == TOP_RIGHT) {
            return true;
        }
        return false;
    }
}
