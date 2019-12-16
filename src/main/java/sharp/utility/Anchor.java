package sharp.utility;

public class Anchor extends CVector {

    public ArrayList<CVector> connections = new ArrayList<>();

    public Anchor(CVector ... vectors) {
	super();
	for (CVector v: vectors) {
	    connections.add(v);
	}
    }

    public Anchor(double x, double y) {
	super(x, y);
    }

    public Anchor(CVector v) {
	super(v);
    }

    public Anchor(double angleFrom, CVector ... vectors) {
	super(angleFrom);
	this(vectors);
    }

    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	updateAnchor(diff, 0.0, 0.0, null);
    }

    public void setY(double y) {
	double diff = y - this.y;
	this.y = y;
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
		v.getY(v.getY() + ymod);
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
