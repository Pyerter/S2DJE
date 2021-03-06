package sharp.utility;

import java.util.List;
import java.util.LinkedList;

/**
 * This class represents a mathematical 2D Vector. The purpose is to
 * use this class to represent both location and movements, such as
 * the location of a box and the forces acting on it.
 */
public class CVector implements Translatable {

    /** A default, origin vector to use when referencing the origin (0, 0). */
    public static final CVector ORIGIN = new CVector(0.0, 0.0);
    /** These two unit vectors only have values in the x or y directions. */
    public static final CVector X_VECTOR = new CVector(1.0, 0.0);
    public static final CVector Y_VECTOR = new CVector(0.0, 1.0);

    private LinkedList<Transform> transforms;
    private boolean hasTransformed = false;
    private double x;
    private double y;

    /**
     * Creates a vector with a given x and y.
     *
     * @param x - the given x
     * @param y - the given y
     */
    public CVector(double x, double y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Creates a new vector that has the same x and y values of a given vector.
     *
     * @param copied - the vector to copy the values from
     */
    public CVector(CVector copied) {
	this.x = copied.getX();
	this.y = copied.getY();
    }

    /**
     * Creates a normalized vector heading a given direction.
     *
     * @param angleFrom - the given angle to constructs a vector from
     */
    public CVector(double angleFrom) {
	this.x = Math.cos(angleFrom);
	this.y = Math.sin(angleFrom);
    }

    /** Creates a vector starts at zero. */
    public CVector() {
        x = 0;
        y = 0;
    }

    /**
     * Get the x value of the vector.
     *
     * @return the vector's x value
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the y value of the vector.
     *
     * @return the vector's y value
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets the x value of the vector.
     *
     * @param x - vector's new x value
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y value of the vector.
     *
     * @param y - vector's new x value
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the total magnitude of this vector. This is equal to
     * the java Math hypoteneuse calculation.
     *
     * @return the calculated magnitude
     */
    public double getMag() {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * Sets this vector to a certain magnitude, but still heading the same direction.
     *
     * @param mag - the magnitude to extend this vector to
     */
    public void setMag(double mag) {
        double angle = this.heading();
        this.x = Math.cos(angle) * mag;
        this.y = Math.sin(angle) * mag;
    }

    /**
     * This method sets this vector equivalent to the given vector,
     * avoiding referencing the other object itself.
     *
     * @param v - the given vector to mimic
     */
    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    /**
     * Returns the angle value this vector is heading. If y and x are both zero,
     * then a value of 0 is returned.
     *
     * @return the double value as a radian
     */
    public double heading() {
        return CVector.heading(this.x, this.y);
    }

    /**
     * This method returns a double in radians that the given vector
     * is calculated to be heading towards, in reference to the origin.
     *
     * @param v - the vector to calculate with
     * @return the calculated angle in radians
     */
    public static double heading(CVector v) {
	return CVector.heading(v.getX(), v.getY());
    }

    /**
     * This method calculates the angle that the pair of given vectors create
     * when forming a line between the two, as if they were points.
     * This is equal to the angle of the calculated difference vector.
     *
     * @param v1 - the first vector to use from
     * @param v2 - the second vector to use to
     * @return the calculated angle in radians
     */
    public static double heading(CVector v1, CVector v2) {
	return CVector.heading(v1.getX(), v1.getY(), v2.getX(), v2.getY());
    }
    
    /**
     * This method returns a double in radians that the given x and y
     * values are calculated to head towards, in reference to the origin.
     *
     * @param x1 - the x value to be used
     * @param y1 - the y value to be referenced
     * @return the calculated angle in radians
     */
    public static double heading(double x1, double y1) {
        return heading(0, 0, x1, y1);
    }

    /**
     * This method calculates the angle that the given x and y starts and ends
     * create when forming a line.
     *
     * @param x1 - the x value to be used
     * @param y1 - the y value to be referenced
     * @param x2 - the x value to head to
     * @param y2 - the y value to head to
     * @return the calculated angle in radians
     */
    public static double heading(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        if (x == 0) {
            if (y > 0) {
                return Math.PI / 2;
            } else if (y < 0) {
                return -Math.PI / 2;
            } else {
                return 0;
            }
        } else if (y == 0) {
            if (x < 0) {
                return Math.PI;
            } else if (x > 0) {
                return 0;
            }
        }
        double rad = Math.atan(y / x);
        if (x < 0) {
            rad += Math.PI;
        }
        return rad;
    }

    /**
     * Adds a given vector to this vector.
     *
     * @param v - the vector to add
     */
    public void add(CVector v) {
        this.x += v.getX();
        this.y += v.getY();
    }

    /**
     * Adds two vectors together and returns the result.
     *
     * @param v1 - the first vector to add
     * @param v2 - the second vector to add
     * @return the resulting vector
     */
    public static CVector add(CVector v1, CVector v2) {
        return new CVector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    /**
     * Subtracts a given vector from this vector.
     *
     * @param v - the vector to subtract
     */
    public void subtract(CVector v) {
        this.x -= v.getX();
        this.y -= v.getY();
    }

    /**
     * Substracts a vector from another and returns the result.
     *
     * @param v1 - the first vector, to substract from
     * @param v2 - the second vector, to subtract with
     * @return the resulting vector
     */
    public static CVector subtract(CVector v1, CVector v2) {
        return new CVector(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    /**
     * Multiplies the value of the vector by a given amount.
     *
     * @param scalar - the value to multiply the vector pieces by
     */
    public void mult(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Multiplies the value of a given vector by a specified amount
     * and returns the result.
     *
     * @param vector - the vector to multiply
     * @param scalar - the value to multiply the vector by
     * @return the calculated vector
     */
    public static CVector mult(CVector vector, double scalar) {
        return new CVector(vector.getX() * scalar, vector.getY() * scalar);
    }

    /**
     * Interpolates the values of this vector towards a given vector.
     *
     * @param v - the vector to lerp towards
     * @param fraction - the fraction towards the target to increment
     */
    public void lerp(CVector v, double fraction) {
        this.x = Utility.lerp(this.x, v.getX(), fraction);
        this.y = Utility.lerp(this.y, v.getY(), fraction);
    }

    /**
     * Normalizes this vector to a unit vector.
     */
    public void normalize() {
        this.setMag(1.0);
    }

    /**
     * This method sets the angle of this vector but retains the same magnitude.
     *
     * @param angle - the new angle to set to
     */
    public void setRotation(double angle) {
        double mag = this.getMag();
        this.x = Math.cos(angle) * mag;
        this.y = Math.sin(angle) * mag;
    }

    /**
     * This method rotates this vector a specified amount from the current angle.
     *
     * @param rotation - the amount to rotate
     */
    public void rotate(double rotation) {
        double angle = this.heading() + rotation;
        double mag = this.getMag();
        this.x = Math.cos(angle) * mag;
        this.y = Math.sin(angle) * mag;
    }

    /**
     * This method rotates this vector a specified amount from the current angle
     * around a given point reference.
     *
     * @param pointRef - the vector to rotate around
     * @param rotation - the amount to rotate this vector
     */
    public void rotateAround(CVector pointRef, double rotation) {
        CVector copying = CVector.rotateAround(this, pointRef, rotation);
        this.x = copying.getX();
        this.y = copying.getY();
    }

    /**
     * This method rotates a given vector around the origin.
     *
     * @param vector - the vector to rotate
     * @param rotation - the rotation to modify the vector with
     * @return the new calculated vector
     */
    public static CVector rotate(CVector vector, double rotation) {
        CVector rotated = new CVector(vector);
        rotated.rotate(rotation);
        return rotated;
    }

    /**
     * This method returns a vector this starts at a given value and rotates around
     * a specified point at a given amount.
     *
     * @param vector - the vector to rotate
     * @param pointRef - the point to reference the vector rotating around
     * @param rotation - the rotation to modify the vector with
     * @return the new rotated vector
     */
    public static CVector rotateAround(CVector vector, CVector pointRef, double rotation) {
        CVector rotated = new CVector(vector.getX() - pointRef.getX(),
                                      vector.getY() - pointRef.getY());
        rotated.rotate(rotation);
        rotated.add(pointRef);
        return rotated;
    }

    /**
     * This method returns the dot product of two vectors.
     *
     * @param v1 - the first vector
     * @param v2 - the second vector
     * @return the dot product
     */
    public static double dot(CVector v1, CVector v2) {
	return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
    }

    public String toString() {
	return "(" + this.x + ", " + this.y + ")";
    }

    public int update() {
	if (transforms == null) {
	    return 0;
	}
	for (Transform t: transforms) {
	    applyTransform(t);
	}
	hasTransformed = true;
	return 0;
    }

    public void endUpdate() {
	if (transforms != null) {
	    transforms.clear();
	}
	hasTransformed = false;
    }

    public void setHasTransformed(boolean hasTransformed) {
	this.hasTransformed = hasTransformed;
    }

    public boolean getHasTransformed() {
	return hasTransformed;
    }

    public LinkedList<Transform> getTransforms() {
	if (transforms == null) {
	    transforms = new LinkedList<>();
	}
	return transforms;
    }
    
    public void addTransform(Transform t) {
	if (transforms == null) {
	    transforms = new LinkedList<>();
	}
	transforms.add(t);
    }

    public boolean canLocallyRotate() {
	return false;
    }

}
