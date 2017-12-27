package base.scales;

import base.config.axis.LabelFormatInfo;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galafit on 20/12/17.
 */
public class ScaleLinear1 extends Scale1 {
    @Override
    public double scale(double value) {
        return range[0] + (value - domain[0]) * (range[range.length - 1] - range[0]) / (domain[domain.length - 1] - domain[0]);
    }

    @Override
    public double invert(double value) {
        return domain[0] + (value - range[0]) * (domain[domain.length - 1] - domain[0]) / (range[range.length - 1] - range[0]);
    }

    @Override
    public  List<Tick> getTicksByStep(double tickStep, LabelFormatInfo labelFormatInfo) {
        TickProvider tickProvider = new TickProvider(labelFormatInfo);
        tickProvider.setTickStep(tickStep);
        return tickProvider.getTicks();
    }

    @Override
    public  List<Tick> getTicksByCount(int tickCount, LabelFormatInfo labelFormatInfo) {
        TickProvider tickProvider = new TickProvider(labelFormatInfo);
        tickProvider.setTickCount(tickCount);
        return tickProvider.getTicks();
    }

    @Override
    public  List<Tick> getTicksByMaxCount(int tickMaxCount, LabelFormatInfo labelFormatInfo) {
        TickProvider tickProvider = new TickProvider(labelFormatInfo);
        tickProvider.setTickCount(tickMaxCount);
        return tickProvider.getTicks();
    }


    class TickProvider {
        private LabelFormatInfo labelFormatInfo_;
        private double tickStep;
        private DecimalFormat tickFormat = new DecimalFormat();
        LabelFormatInfo labelFormatInfo;

        public TickProvider(LabelFormatInfo labelFormatInfo) {
            this.labelFormatInfo = labelFormatInfo;
        }

        public List<Tick> getTicks() {
            ArrayList<Tick> ticks = new ArrayList<Tick>();
            int maxTicksAmount = 500; // if bigger it means that there is some error
            double roundMin = getRoundMin();
            double roundMax = getRoundMax();
            double tickValue = roundMin;
            for (int i = 0; i < maxTicksAmount; i++) {
                if(tickValue <= roundMax) {
                    ticks.add(new Tick(tickValue, tickFormat.format(tickValue)));
                } else {
                    break;
                }
                tickValue += tickStep;
            }
            return ticks;
        }

        public void setTickStep(double step) {
            double max = domain[domain.length - 1];
            double min = domain[0];
            if (max != min) {
                tickStep = step;
                NormalizedNumber normalizedNumber = new NormalizedNumber(step);
                tickFormat = getNumberFormat(normalizedNumber.getPowerOfLastSignificantDigit());
            }
        }

        public void setTickCount(int count) {
            if(count <= 1) {
                String errMsg = MessageFormat.format("Invalid ticks count: {0}. Expected >= 2", count);
                throw new IllegalArgumentException(errMsg);
            }
            double max = domain[domain.length - 1];
            double min = domain[0];
            double step = (max - min) / (count - 1);
            NormalizedNumber roundStep = roundStep(step);

            tickStep = roundStep.getValue();
            tickFormat = getNumberFormat(roundStep.getPower());
        }


        /**
         * Find and set round ticksStep such that:
         * resultantTicksAmount <= maxCount
         *
         * @param maxCount - desirable amount of ticks
         */
        private void setMaxTickCount(int maxCount)  {
            if(maxCount <= 1) {
                String errMsg = MessageFormat.format("Invalid ticks amount: {0}. Expected >= 2", maxCount);
                throw new IllegalArgumentException(errMsg);
            }
            double max = domain[domain.length - 1];
            double min = domain[0];
            Double step = (max - min)  / (maxCount - 1);
            NormalizedNumber roundStep = roundStepUp(step);
            tickStep = roundStep.getValue();
            int ticksCount = (int) ((getRoundMax() - getRoundMin()) / tickStep) + 1;

        /*
         * Due to rounding (roundMin < min < max < roundMax)
         * sometimes it is possible that the resultant ticksCount may be
         * greater than the maxCount:
         * resultantAmount = maxCount + 1.
         * In this case we repeat the same procedure with (maxCount -1)
         */
            if(ticksCount > maxCount && maxCount > 2) {
                maxCount--;
                step = (max - min)  / (maxCount - 1);
                roundStep = roundStepUp(step);
                tickStep = roundStep.getValue();
            }
            tickFormat = getNumberFormat(roundStep.getPower());
        }

        /**
         * On the base of ticks amount calculate round Tick Step
         * that is  multiples of 2, 5 or 10.
         * FirstDigit is in {1,2,5,10};
         */
        private NormalizedNumber roundStep(double step)  {
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
            return new NormalizedNumber(firstDigit, power);
        }

        /**
         * Find closest roundStep >= given step
         * @param step given step
         * @return closest roundInterval >= given step
         */
        private NormalizedNumber roundStepUp(double step)  {
           // int[] roundValues = {1, 2, 3, 4, 5, 6, 8, 10};
            int[] roundSteps = {1, 2, 4, 5, 8, 10};
            NormalizedNumber normalizedStep = new NormalizedNumber(step);
            int power = normalizedStep.getPower();
            int firstDigit = (int) normalizedStep.getMantissa();
            if(firstDigit < normalizedStep.getMantissa()) {
                firstDigit++;
            }

            // find the closest roundStep that is >= firstDigits
            for (int roundStep : roundSteps) {
                if(roundStep >= firstDigit) {
                    firstDigit = roundStep;
                    break;
                }
            }
            if(firstDigit == 10) {
                firstDigit = 1;
                power++;
            }
            return new NormalizedNumber(firstDigit, power);
        }

        public List<Double> getMinorTicks(int ticksDivider, int minorTickCount) {
            if(tickStep == 0) {
                setTickCount(DEFAULT_TICKS_AMOUNT);
            }
            double max = domain[domain.length - 1];
            double min = domain[0];

            ArrayList<Double> minorTicks = new ArrayList<Double>(0);
            int maxTicksAmount = 500; // if bigger it means that there is some error
            double tickValue = getUpperTick(min);
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

        public double getRoundMin() {
            double min = domain[0];
            return getLowerTick(min) ;
        }


        public double getRoundMax() {
            double max = domain[domain.length - 1];
            return getUpperTick(max);
        }

        private double getUpperTick(double value) {
            return Math.ceil(value / tickStep) * tickStep;
        }

        private double getLowerTick(double value) {
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
            if (labelFormatInfo != null && labelFormatInfo.prefix != null) {
                formatPattern = labelFormatInfo.prefix + " " + formatPattern;
            }
            if (labelFormatInfo != null && labelFormatInfo.suffix != null) {
                formatPattern = formatPattern + " " + labelFormatInfo.suffix;

            }
            df = new DecimalFormat(formatPattern);
            return df;
        }
    }
}
