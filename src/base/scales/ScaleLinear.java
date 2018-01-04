package base.scales;

import base.config.LabelFormatInfo;

import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * Created by galafit on 6/9/17.
 */
public class ScaleLinear extends Scale {

    @Override
    public float scale(float value) {
        return range[0] + (value - domain[0]) * (range[range.length - 1] - range[0]) / (domain[domain.length - 1] - domain[0]);
    }

    @Override
    public float invert(float value) {
        return domain[0] + (value - range[0]) * (domain[domain.length - 1] - domain[0]) / (range[range.length - 1] - range[0]);
    }

    @Override
    public TickProvider getTickProvider(int tickCount, LabelFormatInfo labelFormatInfo) {
        return new LinearTickProvider(tickCount, labelFormatInfo);
    }

    @Override
    public  TickProvider getTickProvider(float tickStep, Unit tickUnit, LabelFormatInfo labelFormatInfo) {
        return new LinearTickProvider(tickStep, tickUnit, labelFormatInfo);
    }

    class LinearTickProvider implements TickProvider {
        private float tickStep;
        private DecimalFormat labelFormat = new DecimalFormat();
        private Tick lastTick;

        public LinearTickProvider(int tickCount, LabelFormatInfo labelFormatInfo) {
            NormalizedNumber normalizedStep = getTickStep(tickCount);
            tickStep = (float) normalizedStep.getValue();
            labelFormat = getNumberFormat(normalizedStep.getPower(), labelFormatInfo);
        }

        public LinearTickProvider(float tickStep, Unit tickUnit, LabelFormatInfo labelFormatInfo) {
            this.tickStep = tickStep;
            NormalizedNumber normalizedStep = new NormalizedNumber(tickStep);
            labelFormat = getNumberFormat(normalizedStep.getPowerOfLastSignificantDigit(), labelFormatInfo);
        }

        @Override
        public Tick getNextTick() {
            if(lastTick == null) {
                float min = domain[0];
                lastTick = getLowerTick(min);
            } else {
                float tickValue = lastTick.getValue() + tickStep;
                lastTick = new Tick(tickValue, labelFormat.format(tickValue));
            }
            return lastTick;
        }

        @Override
        public Tick getPreviousTick() {
            if(lastTick == null) {
                float min = domain[0];
                lastTick = getLowerTick(min);
            } else {
                float tickValue = lastTick.getValue() - tickStep;
                lastTick = new Tick(tickValue, labelFormat.format(tickValue));
            }
            return lastTick;
        }

        @Override
        public Tick getUpperTick(float value) {
            float tickValue = (float) (Math.ceil(value / tickStep) * tickStep);
            lastTick = new Tick(tickValue, labelFormat.format(tickValue));
            return lastTick;
        }

        @Override
        public Tick getLowerTick(float value) {
            float tickValue = (float) (Math.floor(value / tickStep) * tickStep);
            lastTick = new Tick(tickValue, labelFormat.format(tickValue));
            return lastTick;
        }


        /**
         * On the base of ticks amount calculate round Tick Step
         * that is  multiples of 2, 5 or 10.
         * FirstDigit is in {1,2,5,10};
         */
        private NormalizedNumber getTickStep(int tickCount)  {
            if(tickCount <= 1) {
                String errMsg = MessageFormat.format("Invalid ticks tickCount: {0}. Expected >= 2", tickCount);
                throw new IllegalArgumentException(errMsg);
            }
            float max = domain[domain.length - 1];
            float min = domain[0];
            float step = (max - min) / (tickCount - 1);
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
                case 10:
                    firstDigit = 1;
                    power++;
                    break;
            }
            return new NormalizedNumber(firstDigit, power);
        }


        // TODO: use metric shortcuts - k, M, G... from formatInfo
        private DecimalFormat getNumberFormat(int power, LabelFormatInfo labelFormatInfo) {
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


        /**
         * Find closest roundStep >= given step
         * @param step given step
         * @return closest roundInterval >= given step
         */
        private NormalizedNumber roundStepUp(float step)  {
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

        /**
         * Find and set round ticksStep such that:
         * resultantTicksAmount <= maxCount
         *
         * @param maxCount - desirable amount of ticks
         */
        private NormalizedNumber getStepForMaxTickCount(int maxCount)  {
            if(maxCount <= 1) {
                String errMsg = MessageFormat.format("Invalid ticks amount: {0}. Expected >= 2", maxCount);
                throw new IllegalArgumentException(errMsg);
            }
            float max = domain[domain.length - 1];
            float min = domain[0];
            float step = (max - min)  / (maxCount - 1);
            NormalizedNumber roundStep = roundStepUp(step);
            tickStep = (float) roundStep.getValue();
            int ticksCount = (int) ((getLowerTick(min).getValue() - getUpperTick(max).getValue()) / tickStep) + 1;

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
            }
            return roundStep;
        }
    }
}
