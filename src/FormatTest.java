import java.text.DecimalFormat;

/**
 * Created by galafit on 5/9/17.
 */
public class FormatTest {

    private DecimalFormat getTickLabelFormat(int power) {
        DecimalFormat dfNeg4 = new DecimalFormat("0.0000");
        DecimalFormat dfNeg3 = new DecimalFormat("0.000");
        DecimalFormat dfNeg2 = new DecimalFormat("0.00");
        DecimalFormat dfNeg1 = new DecimalFormat("0.0");
        DecimalFormat df0 = new DecimalFormat("#,##0");
        DecimalFormat df = new DecimalFormat("#.######E0");

        if (power == -4) {
            return dfNeg4;
        }
        if (power == -3) {
            return dfNeg3;
        }
        if (power == -2) {
            return dfNeg2;
        }
        if (power == -1) {
            return dfNeg1;
        }
        if (power >= 0 && power <= 6) {
            return df0;
        }
        return df;
    }

    private static void printDoble(double d) {
        DecimalFormat f1 = new DecimalFormat("000.###");
        DecimalFormat f2 = new DecimalFormat("##0.###");
        DecimalFormat df = new DecimalFormat("#.######E0");
        System.out.println(d+ "  "+ f1.format(d) + "  "+f2.format(d));
    }

    public static void main(String[] args) {
        printDoble(23.0008);
        printDoble(-0.0008);
    }

}
