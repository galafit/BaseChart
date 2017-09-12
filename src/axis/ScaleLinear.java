package axis;

import configuration.TickFormatInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 6/9/17.
 */
public class ScaleLinear extends Scale {

    @Override
    public double scale(double value) {
        return range[0] + (value - domain[0]) * (range[range.length - 1] - range[0]) / (domain[domain.length - 1] - domain[0]);
    }

    @Override
    public double invert(double value) {
        return domain[0] + (value - range[0]) * (domain[domain.length - 1] - domain[0]) / (range[range.length - 1] - range[0]);
    }

    @Override
    public TickProvider getTickProvider() {
        return new LinearTickProvider();
    }

    class LinearTickProvider implements TickProvider {
        private TickFormatInfo tickFormatInfo;
        private double tickStep;
        private DecimalFormat tickFormat = new DecimalFormat();

        public LinearTickProvider() {
           setTickAmount(DEFAULT_TICKS_AMOUNT);
        }

        @Override
        public void setTickFormatInfo(TickFormatInfo tickFormatInfo) {
            this.tickFormatInfo = tickFormatInfo;
         }

        @Override
        public void setTickAmount(int amount) {
            double max = domain[domain.length - 1];
            double min = domain[0];
            if (max != min) {
                double step = (max - min) / amount;

                // Round Tick Step
                // The default ticks for quantitative scales are multiples of 2, 5 and 10
                // firstDigit is in {1,2,5,10};
                NormalizedNumber normalizedStep = new NormalizedNumber(step);

                int power = normalizedStep.getPower();
                int firstDigit = (int)Math.round(normalizedStep.getMantissa());
                switch (firstDigit) {
                    case 3:
                        firstDigit = 2;
                        break;
                    case 4:
                        firstDigit = 5;
                        break;
                    case 6:
                        firstDigit = 5;
                        break;
                    case 7:
                        firstDigit = 5;
                        break;
                    case 8:
                        firstDigit = 1;
                        power++;
                        break;
                    case 9:
                        firstDigit = 1;
                        power++;
                        break;
                }
                tickStep = firstDigit * Math.pow(10, power);
                tickFormat = getNumberFormat(power);
            }
        }

        @Override
        public void setTickStep(double step) {
            double max = domain[domain.length - 1];
            double min = domain[0];
            if (max != min) {
                tickStep = step;
                NormalizedNumber normalizedNumber = new NormalizedNumber(step);
                tickFormat = getNumberFormat(normalizedNumber.getPowerOfLastSignificantDigit());
            }
        }

        @Override
        public List<Tick> getTicks(int ticksDivider) {
            double max = domain[domain.length - 1];
            double min = domain[0];
            if(max == min) {
                ArrayList<Tick> ticks = new ArrayList<Tick>(1);
                ticks.add(new Tick(min, String.valueOf(min)));
                return ticks;
            }
            ArrayList<Tick> ticks = new ArrayList<Tick>();
            int maxTicksAmount = 500; // if bigger it means that there is some error
            double tickValue = getTickRight(min);
            double resultantTickStep = tickStep * ticksDivider;
            for (int i = 0; i < maxTicksAmount; i++) {
                if(tickValue <= max) {
                    ticks.add(new Tick(tickValue, tickFormat.format(tickValue)));
                } else {
                    break;
                }
                tickValue += resultantTickStep;
            }
            double maxTickValue = getTickLeft(max);
            if(maxTickValue > tickValue) {
                ticks.add(new Tick(maxTickValue, tickFormat.format(maxTickValue)));
            }

            return ticks;
        }

        @Override
        public List<Double> getMinorTicks(int ticksDivider, int minorTickCount) {
            double max = domain[domain.length - 1];
            double min = domain[0];
            if(max == min) {
                ArrayList<Double> minorTicks = new ArrayList<Double>(0);
                return minorTicks;
            }
            ArrayList<Double> minorTicks = new ArrayList<Double>(0);
            int maxTicksAmount = 500; // if bigger it means that there is some error
            double tickValue = getTickRight(min);
            double minorTickStep = tickStep * ticksDivider / minorTickCount;
            for (int i = 0; i < maxTicksAmount; i++) {
                if(tickValue <= max) {
                    minorTicks.add(new Double(tickValue));
                    tickValue += minorTickStep;
                } else {
                    break;
                }
            }
            return minorTicks;
        }

        @Override
        public double getRoundMin() {
            double min = domain[0];
            return getTickLeft(min) ;
        }

        @Override
        public double getRoundMax() {
            double max = domain[domain.length - 1];
            return getTickRight(max) ;
        }

        private double getTickRight(double value) {
            return Math.ceil(value / tickStep) * tickStep;
        }

        private double getTickLeft(double value) {
            return Math.floor(value / tickStep) * tickStep;
        }


        // TODO: use metric shortcuts - k, M, G... from formatInfo
        private DecimalFormat getNumberFormat(int power) {
            DecimalFormat dfNeg4 = new DecimalFormat("0.0000");
            DecimalFormat dfNeg3 = new DecimalFormat("0.000");
            DecimalFormat dfNeg2 = new DecimalFormat("0.00");
            DecimalFormat dfNeg1 = new DecimalFormat("0.0");
            DecimalFormat df0 = new DecimalFormat("#,##0");

            DecimalFormat df = new DecimalFormat("#.######E0");

            if (power == -4) {
                df = dfNeg4;
            }
            if (power == -3) {
                df = dfNeg3;
            }
            if (power == -2) {
                df = dfNeg2;
            }
            if (power == -1) {
                df = dfNeg1;
            }
            if (power >= 0 && power <= 6) {
                df = df0;
            }

            String formatPattern = df.toPattern();
            if (tickFormatInfo != null && tickFormatInfo.prefix != null) {
                formatPattern = tickFormatInfo.prefix + " " + formatPattern;
            }
            if (tickFormatInfo != null && tickFormatInfo.suffix != null) {
                formatPattern = formatPattern + " " + tickFormatInfo.suffix;
            }
            df = new DecimalFormat(formatPattern);
            return df;
        }

    }
}
