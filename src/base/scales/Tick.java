package base.scales;

/**
 * Created by galafit on 5/9/17.
 */
public class Tick {
    private float value;
    private String label;

    public Tick(float value, String label) {
        this.value = value;
        this.label = label;
    }

    public float getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}