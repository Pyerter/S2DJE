package sharp.utility;

public class CVectorPair {

    private CVector v1;
    private CVector v2;

    public CVectorPair(CVector v1, CVector v2) {
	this.v1 = new CVector(v1);
	this.v2 = new CVector(v2);
    }

    public CVectorPair(double x1, double y1, double x2, double y2) {
	this(new CVector(x1, y1), new CVector(x2, y2));
    }

    public double getLength() {
	return CVector.subtract(v2, v1).getMag();
    }

    public double getAngle() {
	return CVector.heading(v1, v2);
    }

    public CVector getV1() {
	return v1;
    }

    public CVector getV2() {
	return v2;
    }

    public double getStartX() {
	return v1.getX();
    }

    public double getEndX() {
	return v2.getX();
    }

    public double getStartY() {
	return v1.getY();
    }

    public double getEndY() {
	return v2.getY();
    }

}
