package main.chart.axis;


/**
 * Present double as significant digits (int) and power. So
 * double = digits * Math.power(10, power);
 */
public class NormalizedDouble {
    int digits;
    int power;

    public NormalizedDouble(int digits, int power) {
        while (digits % 10 == 0) {
            digits = digits / 10;
            power++;
        }
        this.digits = digits;
        this.power = power;
    }

    public int getDigits() {
        return digits;
    }

    public int getPower() {
        return power;
    }

    public double getDouble() {
        return digits * Math.pow(10, power);
    }
}
