package sharp.unit;

import sharp.utility.Anchor;
import sharp.utility.CVector;
import sharp.utility.Transform;
import sharp.collision.*;
import sharp.game.App;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

public class SimplePolyUnit extends SimpleUnit {

    private Poly poly;
    
    public SimplePolyUnit(Anchor point, CVector ... points) {
	super();
	poly = new Poly(point, points);
    }

    public SimplePolyUnit(CVector ... points) {
	super();
	double aveX = 0;
	double aveY = 0;
	for (CVector v: points) {
	    aveX += v.getX();
	    aveY += v.getY();
	}
	aveX /= points.length;
	aveY /= points.length;
	poly = new Poly(new Anchor(aveX, aveY), points);
    }

    public Projection getProjection() {
	return poly.getProjection();
    }
    
    public Projection[] getCollider() {
	return poly.getCollider();
    }

    public Node getNode() {
	return poly.getPolygon();
    }

    public Polygon getPolygon() {
	return poly.getPolygon();
    }

    public void setX(double x) {
	poly.setX(x);
    }

    public void setY(double y) {
	poly.setY(y);
    }

    public double getX() {
	return poly.getX();
    }

    public double getY() {
	return poly.getY();
    }

    public void rotate(double rot) {
	poly.rotate(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	poly.rotateAround(pivot, rot);
    }

    public boolean canLocallyRotate() {
	return poly.canLocallyRotate();
    }

    public List<Transform> getTransforms() {
	System.out.println("Returning transforms");
	return poly.getTransforms();
    }

    public void addTransform(Transform t) {
	poly.addTransform(t);
    }

    public void applyTransform(Transform t) {
	poly.applyTransform(t);
	System.out.println("Applying: " + t);
    }

    public void revertTransform(Transform t) {
	poly.revertTransform(t);
    }

    public boolean getHasTransformed() {
	return poly.getHasTransformed();
    }

    public void setHasTransformed(boolean hasTransformed) {
	poly.setHasTransformed(hasTransformed);
    }

    public ArrayList<Collidable> getCollidables() {
	return poly.getCollidables();
    }

    public void update() {
	poly.update();
	setPreviousPosition(getProjection().getPivot());
	System.out.println("Updating simple poly unit...");
	if (getGrav()) {
	    Unit.GRAVITY.apply(this);
	    System.out.println("Applying gravity...");
	}
	// testing movement
	getAcceleration().add(new CVector(0.01, 0.0));
	setRotAcceleration(0.01);
	super.update();
	LinkedList<Collidable> discreteCollisions = new LinkedList<>();
	System.out.println("Queueing discrete update...");
	List<Collidable> discUpd = discreteUpdate();
	boolean doneUpdating = true;
	if (discUpd != null) {
	    for (Collidable c: discUpd) {
		discreteCollisions.add(c);
	    }
	} else {
	    doneUpdating = !fineUpdate(discreteCollisions);
	}
	System.out.println("Checking end update");
	if (doneUpdating) {
	    this.endUpdate();
	}
    }
    
    public void endUpdate() {
	poly.endUpdate();
	System.out.println("Updated poly");
    }

}
