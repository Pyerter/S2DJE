package sharp.utility;

/**
 * This class represents a pair of two {@code CVector}s. It has methods
 * that provide quality of life when using two vectors.
 */
public class CVectorPair {

    private CVector v1;
    private CVector v2;

    /**
     * Create a pair of vectors given two {@code CVector} objects.
     *
     * @param v1 - the first vector
     * @param v2 - the second vector
     */
    public CVectorPair(CVector v1, CVector v2) {
	this.v1 = new CVector(v1);
	this.v2 = new CVector(v2);
    }

    /**
     * Create a pair of vectors given two sets of x and y values.
     *
     * @param x1 - x value of the first vector
     * @param y1 - y value of the first vector
     * @param x2 - x value of the second vector
     * @param y2 - y value of the second vector
     */
    public CVectorPair(double x1, double y1, double x2, double y2) {
	this(new CVector(x1, y1), new CVector(x2, y2));
    }

    /**
     * Given the two vectors are coordinate vectors, calculates the difference
     * vector and returns the magnitude, giving the distance between the pair.
     *
     * @return the distance between the vector pair
     */
    public double getLength() {
	return CVector.subtract(v2, v1).getMag();
    }

    /**
     * Given the two vectors are coordinate vectors, returns the angle
     * of a line segment drawn from coordinate vector1 to coordinate vector2.
     *
     * @return the angle of a line segment between the vectors, relative to v1
     */
    public double getAngle() {
	return CVector.heading(v1, v2);
    }

    /**
     * Get the first vector.
     *
     * @return the first vector
     */
    public CVector getV1() {
	return v1;
    }

    /**
     * Get the second vector.
     *
     * @return the second vector
     */
    public CVector getV2() {
	return v2;
    }

    /**
     * Get the x value of the first vector.
     *
     * @return the v1 x value
     */
    public double getStartX() {
	return v1.getX();
    }

    /**
     * Get the x value of the second vector.
     *
     * @return the v2 x value
     */
    public double getEndX() {
	return v2.getX();
    }

    /**
     * Get the y value of the first vector.
     *
     * @return the v1 y value
     */
    public double getStartY() {
	return v1.getY();
    }

    /**
     * Get the y value of the second vector.
     *
     * @return the v2 y value
     */
    public double getEndY() {
	return v2.getY();
    }

}
