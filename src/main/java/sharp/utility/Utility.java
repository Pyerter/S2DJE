package sharp.utility;

/** This class will store all useful miscellanious methods. */
public class Utility {

    /**
     * This method will return a Double that is a certain fraction further
     * towards the desired end value.
     * @param start - the beginning value
     * @param end - the desired value
     * @param fraction - the fraction towards the desired value
     * @return the calculated interpolation
     */
    public static Double lerp(Double start, Double end, Double fraction) {
        Double lerpVal = (end - start) * fraction;
        return start + lerpVal;
    }

/**
     * Given a double value, this returns a value this is kept within
     * the given high and low bounds. This overload returns {@code checkBounds} with
     * the inclusive parameter as false.
     *
     * @param val - the double value to check
     * @param low - the lower bound
     * @param high - the higher bound
     * @return the bounded double value
     */
    public static double checkBounds(double val, double low, double high) {
        return checkBounds(val, low, high, false);
    }

    /**
     * Given a double value, this returns a value this is kept within
     * the given high and low bounds.
     *
     * @param val - the double value to check
     * @param low - the lower bound
     * @param high - the higher bound
     * @param inclusive - if this is true, values will be checked and bound directly
     * at the given low and high bounds. if this is false, only greater and less than
     * will be used
     * @return the bounded double value
     */
    public static double checkBounds(double val, double low, double high, boolean inclusive) {
        if (!inclusive) {
            if (val < low) {
                val = low;
            } else if (val > high) {
                val = high;
            }
        } else {
            if (val <= low) {
                val = low + Double.MIN_NORMAL;
            } else if (val >= high) {
                val = high - Double.MIN_NORMAL;
            }
        }
        return val;
    }

    /**
     * Given a double value, this method wraps the value to the opposite bounds
     * that is was detected to have passed.
     *
     * @param val - the value to wrap
     * @param low - the lower bound
     * @param high - the higher bound
     * @return the wrapped double value
     */
    public static double wrapBounds(double val, double low, double high) {
        return wrapBounds(val, low, high, false);
    }

    /**
     * Given a double value, this method wraps the value to the opposite bounds
     * that is was detected to have passed.
     *
     * @param val - the value to wrap
     * @param low - the lower bound
     * @param high - the higher bound
     * @param inclusive - false if the value is only being checked as PASSED
     * and not ON the bounds
     * @return the wrapped double value
     */
    public static double wrapBounds(double val, double low, double high, boolean inclusive) {
        if (!inclusive) {
            if (val < low) {
                val = high;
            } else if (val > high) {
                val = low;
            }
        } else {
            if (val <= low) {
                val = high - Double.MIN_NORMAL;
            } else if (val >= high) {
                val = low + Double.MIN_NORMAL;
            }
        }
        return val;
    }

    /**
     * This method will return -1, 0, or 1 depending on if the given number
     * is less than, equal to, or greater than zero.
     *
     * @param numb - the numb to check
     * @return the value sign of the number
     */
    public static int sign(double numb) {
	if (numb == 0) {
	    return 0;
	}
	return (int)(Math.abs(numb) / numb);
    }

}
