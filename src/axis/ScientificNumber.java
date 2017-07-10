package main.chart.axis;

import java.text.MessageFormat;

/**
 * Scientific Notation:
 * https://www.mathsisfun.com/numbers/scientific-notation.html
 */
public class ScientificNumber {
    double digits;
    int power;

    public ScientificNumber(double digits, int power) {
        if(digits >= 10 || digits < 1) {
            String errMsg = MessageFormat.format("Invalid digits: {0}. In Scientific Notation expected 1<= digits < 10.", digits);
            throw new IllegalArgumentException(errMsg);
        }
        this.digits = digits;
        this.power = power;
    }

    public ScientificNumber(double value) {
        power = calculatePower(value);
        digits = value/Math.pow(10,power);
    }

    private int calculatePower(double value){

        if (value == 0) {return 0;}

        double power = Math.log10(Math.abs(value));
        int powerInt = (int) power;
        if ((power < 0) && (power != powerInt)) {
            powerInt = powerInt - 1;
        }
        return powerInt;
    }


    public double getDigits() {
        return digits;
    }

    public int getPower() {
        return power;
    }

    /**
     * Unit tests {@code ScientificNumber}.
     *
     * String.format: https://www.dotnetperls.com/format-java
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String format = "%1$10s :  digits = %2$9s  power = %3$3s";
        double value = 100;
        ScientificNumber number = new ScientificNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = 0.03234234;
        number = new ScientificNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = 7000.15;
        number = new ScientificNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = -20;
        number = new ScientificNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));

        value = 0;
        number = new ScientificNumber(value);
        System.out.println(String.format(format, value, number.getDigits(), number.getPower()));
    }
}
