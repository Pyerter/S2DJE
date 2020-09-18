package sharp.utility;

import java.util.ArrayList;

/**
 * This class represents a vector that other {@code Translatable}s can "anchor" to,
 * moving any anchored objects relative to this one. They will translate one-to-one
 * with this vector. Local rotations will rotate all anchored objects around this one.
 * Relative rotations will work as usual.
 */
public class Anchor extends CVector {

    public ArrayList<Translatable> connections = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Anchor() {
	super();
    }

    /**
     * Constructor with a position and any translatables to anchor.
     *
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @param cons - any "connected" {@code Translatable} objects that will be anchored
     */
    public Anchor(double x, double y, Translatable ... cons) {
	super(x, y);
	addConnections(cons);
    }

    /**
     * Constructor that copies another vector and adds anchors any translatables.
     * 
     * @param v - the vector to copy
     * @param cons - any "connected" {@code Translatable} objects that will be anchored
     */
    public Anchor(CVector v, Translatable ... cons) {
	super(v);
	addConnections(cons);
    }

    /**
     * Constructor that makes a vector given an angle and also anchors any translatables.
     *
     * @param angleFrom - the angle, in radians, to form this vector off of
     * @param cons - any "connected" {@code Translatable} objects that will be anchored
     */
    public Anchor(double angleFrom, Translatable ... cons) {
	super(angleFrom);
	addConnections(cons);
    }

    /**
     * This fetches any "anchored" object that are connected to this {@code Anchor}.
     *
     * @return the {@code ArrayList} of connections
     */
    public ArrayList<Translatable> getConnections() {
	return connections;
    }

    /**
     * This method adds any given {@code Translatable}s to this object's list of connections.
     *
     * @param cons - the objects to connect to this by adding them to the list of connections
     */
    public void addConnections(Translatable ... cons) {
	for (Translatable t: cons) {
	    if (!connections.contains(t)) {
		connections.add(t);
	    }
	}
    }

    /**
     * Sets the x coordinate of this object.
     *
     * @param x - the new x coordinate
     */
    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	updateAnchor(diff, 0.0, 0.0, null);
    }

    /**
     * Sets the y coordinate of this object.
     *
     * @param y - the new y coordinate
     */
    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	updateAnchor(0.0, diff, 0.0, null);
    }

    /**
     * This method sets this vector equivalent to the given vector,
     * avoiding referencing the other object itself.
     *
     * @param v - the given vector to mimic
     */
    public void set(CVector v) {
	// double diffX = v.getX() - getX();
	// double diffY = v.getY() - getY();
	setX(v.getX());
	setY(v.getY());
    }

    /**
     * Sets the rotation of this vector relative to the origin, 
     * adjusting all connections accordingly.
     *
     * @param angle - the new local angle of this vector, in radians
     */
    public void setRotation(double angle) {
	double diff = angle - this.heading();
	super.setRotation(angle);
	updateAnchor(0.0, 0.0, diff, null);
    }

    /**
     * Rotates this vector around the origin by the given amount,
     * rotating all connections appropriately.
     *
     * @param rotation - the amount to rotate, in radians
     */
    public void rotate(double rotation) {
	super.rotate(rotation);
	updateAnchor(0.0, 0.0, rotation, null);
    }

    /**
     * Rotates this vector and all connections relative to the given vector as a point.
     *
     * @param pointRef - the vector containing the proper x and y coordinates to rotate around
     * @param rotation - the amount, in radians, to rotate this vector
     */
    public void rotateAround(CVector pointRef, double rotation) {
	super.rotateAround(pointRef, rotation);
	updateAnchor(0.0, 0.0, rotation, pointRef);
    }

    /**
     * Rotates this anchor locally, rotating all connections around this point vector.
     *
     * @param rotation - the amount to rotate locally, in radians
     */
    public void rotateAnchor(double rotation) {
	rotateAround(this, rotation);
    }

    /**
     * Adds a given vector to this one, updating all connecting points.
     *
     * @param v - the vector to add
     */
    public void add(CVector v) {
	super.add(v);
	updateAnchor(v.getX(), v.getY(), 0.0, null);
    }

    /**
     * Subtracts a given vector from this one, updating all connection points.
     *
     * @param v - the vector to subtract
     */
    public void subtract(CVector v) {
	super.subtract(v);
	updateAnchor(-v.getX(), -v.getY(), 0.0, null);
    }

    /**
     * Interpolate this vector towards a given point vector, updating all connection points.
     *
     * @param v - the vector to interpolate towards
     * @param fraction - a value (recommended 0.0 - 1.0) that determines what fraction
     * of the distance between the two vectors will be closed
     */
    public void lerp(CVector v, double fraction) {
	double xDiff = Utility.lerp(getX(), v.getX(), fraction) - getX();
	double yDiff = Utility.lerp(getY(), v.getY(), fraction) - getY();
	updateAnchor(xDiff, yDiff, 0.0, null);
	super.setX(xDiff + getX());
	super.setY(yDiff + getY());
    }

    /**
     * Set the magnitude of this vector, adjusting all magnitudes of connections accordingly.
     * Do not touch this method if this is a point vector (unless you're very confident about
     * what you're doing).
     *
     * @param mag - the new mag of this vector
     */
    public void setMag(double mag) {
	double angle = this.heading();
	double xDiff = (Math.cos(angle) * mag) - getX();
	double yDiff = (Math.sin(angle) * mag) - getY();
	updateAnchor(xDiff, yDiff, 0.0, null);
	super.setX(xDiff + getX());
	super.setY(xDiff + getY());
    }

    /**
     * Sets the magnitude of this vector to 1.
     */
    public void normalize() {
	this.setMag(1.0);
    }

    /**
     * Multiply this vector by a scalar value, adjusting all connections to maintain
     * their local distance to this vector.
     *
     * @param scalar - the value to scale by
     */
    public void mult(double scalar) {
	double xDiff = (getX() * scalar) - getX();
	double yDiff = (getY() * scalar) - getY();
	updateAnchor(xDiff, yDiff, 0.0, null);
	super.setX(xDiff + getX());
	super.setY(yDiff + getY());
    }

    /**
     * This method updates all the connection and anchors attached
     * to this anchor, first changing the x and y coords, then changing
     * the rotation relative to this anchor.
     *
     * @param xmod - the x value to mod by
     * @param ymod - the y value to mod by
     * @param rot - the rotation value
     */
    protected void updateAnchor(double xmod, double ymod, double rot, CVector rotAround) {
	boolean changeX = (xmod != 0);
	boolean changeY = (ymod != 0);
	boolean changeRot = (rot != 0);
	for (Translatable t: connections) {
	    if (changeX) {
		t.setX(t.getX() + xmod);
	    }
	    if (changeY) {
		t.setY(t.getY() + ymod);
	    }
	    if (changeRot) {
		if (rotAround != null) {
		    t.rotateAround(rotAround, rot);
		} else if (rot != 0) {
		    t.rotateAround(CVector.ORIGIN, rot);
		}
	    }
	}
    }

}
