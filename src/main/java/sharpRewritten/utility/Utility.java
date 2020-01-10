package sharp.utility;

import java.util.List;

/** This class will store all useful miscellanious methods. */
public class Utility {

    private static final Double DEFAULT_ACCURACY = new Double(0.0001);
    
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

    public static CVector getClosest(CVector point, CVector ... compares) {
	if (compares == null || compares.length == 0) {
	    return null;
	}
	CVector closest = compares[0];
	double closestDist = CVector.subtract(point, closest).getMag();
	for (int i = 1; i < compares.length; i++) {
	    double dist = CVector.subtract(point, compares[i]).getMag();
	    if (dist < closestDist) {
		closest = compares[i];
		closestDist = dist;
	    }
	}
	return closest;
    }

    public static CVector getClosest(CVector point, List<CVector> compares) {
	return getClosest(point, compares.stream().toArray(CVector[]::new));
    }

    /**
     * This method returns true or false depending on if the first number and the second number
     * are equal to or less than an amount, accuracy, apart.
     * Difference is less than or equal to accuracy.
     *
     * @param n1 - the first number
     * @param n2 - the second number
     * @param accuracy - the distance to be apart
     * @return true if the numbers are close enough
     */
    public static boolean isAbout(double n1, double n2, double accuracy) {
	return Math.abs(n1 - n2) < accuracy;
    }

    /**
     * This method returns true or false depending on if the first number and the second number
     * are equal to or less than an amount, accuracy, apart.
     * Difference is less than or equal to accuracy.
     *
     * @param n1 - the first number
     * @param n2 - the second number
     * @return true if the numbers are close enough
     */
    public static boolean isAbout(double n1, double n2) {
	return isAbout(n1, n2, DEFAULT_ACCURACY);
    }

}
