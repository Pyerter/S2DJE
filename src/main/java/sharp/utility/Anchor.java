package sharp.utility;

import java.util.ArrayList;

public class Anchor extends CVector {

    public ArrayList<CVector> connections = new ArrayList<>();

    public Anchor() {
	super();
    }

    public Anchor(double x, double y, CVector ... vectors) {
	super(x, y);
	addConnections(vectors);
    }

    public Anchor(CVector v, CVector ... vectors) {
	super(v);
	addConnections(vectors);
    }

    public Anchor(double angleFrom, CVector ... vectors) {
	super(angleFrom);
	addConnections(vectors);
    }

    public ArrayList<CVector> getConnections() {
	return connections;
    }

    public void addConnections(CVector ... vectors) {
	for (CVector v: vectors) {
	    if (!connections.contains(v)) {
		connections.add(v);
	    }
	}
    }

    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	updateAnchor(diff, 0.0, 0.0, null);
    }

    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	updateAnchor(0.0, diff, 0.0, null);
    }

    public void setRotation(double angle) {
	double diff = angle - this.heading();
	super.setRotation(angle);
	updateAnchor(0.0, 0.0, diff, null);
    }

    public void rotate(double rotation) {
	super.rotate(rotation);
	updateAnchor(0.0, 0.0, rotation, null);
    }

    public void rotateAround(CVector pointRef, double rotation) {
	super.rotateAround(pointRef, rotation);
	updateAnchor(0.0, 0.0, rotation, pointRef);
    }

    public void rotateAnchor(double rotation) {
	rotateAround(this, rotation);
    }

    public void add(CVector v) {
	super.add(v);
	updateAnchor(v.getX(), v.getY(), 0.0, null);
    }

    public void subtract(CVector v) {
	super.subtract(v);
	updateAnchor(-v.getX(), -v.getY(), 0.0, null);
    }

    public void lerp(CVector v, double fraction) {
	double xDiff = Utility.lerp(getX(), v.getX(), fraction) - getX();
	double yDiff = Utility.lerp(getY(), v.getY(), fraction) - getY();
	updateAnchor(xDiff, yDiff, 0.0, null);
	super.setX(xDiff + getX());
	super.setY(yDiff + getY());
    }

    public void setMag(double mag) {
	double angle = this.heading();
	double xDiff = (Math.cos(angle) * mag) - getX();
	double yDiff = (Math.sin(angle) * mag) - getY();
	updateAnchor(xDiff, yDiff, 0.0, null);
	super.setX(xDiff + getX());
	super.setY(xDiff + getY());
    }

    public void normalize() {
	this.setMag(1.0);
    }

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
	for (CVector v: connections) {
	    if (changeX) {
		v.setX(v.getX() + xmod);
	    }
	    if (changeY) {
		v.setY(v.getY() + ymod);
	    }
	    if (changeRot) {
		if (rotAround != null) {
		    v.rotateAround(rotAround, rot);
		} else {
		    v.rotate(rot);
		}
		    
	    }
	}
    }

}
