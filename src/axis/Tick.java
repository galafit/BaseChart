package main.chart.axis;

/**
 * Created by hdablin on 08.04.17.
 */
public class Tick {
    private double value;
    private String label;

    public Tick(double value, String label) {
        this.value = value;
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
