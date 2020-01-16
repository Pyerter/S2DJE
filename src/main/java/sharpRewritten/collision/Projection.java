package sharp.collision;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.Transform;
import sharp.utility.Translatable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;

public class Projection implements Translatable {

    private LinkedList<Transform> transforms = new LinkedList<>();
    private boolean hasTransformed;
    private Anchor pivot;
    private Double rotation = new Double(0.0);
    private ArrayList<CVector> outline = new ArrayList<>();
    private Double collisionRadius = 0.0;

    protected void setup() {
	pivot = new Anchor(0, 0);
	outline = new ArrayList<>();
    }
    
    protected void setup(Anchor pivot, CVector ... outline) {
	this.pivot = pivot;
	this.outline = new ArrayList<CVector>();
	setOutline(outline);
    }

    public <T extends Node> T getNode();

    public default Projection[] getCollider(){
	return new Projection[]{this};
    }
    
    public Anchor getPivot() {
	return pivot;
    }

    protected void setPivot(Anchor pivot) {
	this.pivot = pivot;
    }

    public double getX() {
	return pivot.getX();
    }

    public double getY() {
	return pivot.getY();
    }

    public void setX(double x) {
	pivot.setX(x);
    }

    public void setY(double y) {
	pivot.setY(y);
    }

    public void rotate(double rot) {
	pivot.rotateAnchor(rot);
	rotation += rot;
    }

    public void rotateAround(CVector pivot, double rot) {
	this.pivot.rotateAround(pivot, rot);
	rotation += rot;
    }

    public boolean canLocallyRotate() {
	return true;
    }

    public LinkedList<Transform> getTransforms() {
	return transforms;
    }

    public void addTransform(Transform t) {
	transforms.add(t);
    }

    public boolean getHasTransformed() {
	return hasTransformed;
    }

    public void setHasTransformed(boolean hasTransformed) {
	this.hasTransformed = hasTransformed;
    }

    public Double getRotation() {
	return rotation;
    }

    public void setOutline(CVector ... outline) {
	LinkedList<CVector> newOutline = new LinkedList<>();
	for (CVector v: outline) {
	    newOutline.add(v);
	}
	setOutline(newOutline);
    }
    
    public void setOutline(List<CVector> outline) {
	for (CVector v: this.outline) {
	    if (pivot.getConnections().contains(v)) {
		pivot.getConnections().remove(v);
	    }
	}
	this.outline.clear();
	collisionRadius = 0.0;
	for (CVector v: outline) {
	    CVector newV = new CVector(v);
	    this.outline.add(newV);
	    double distToCenter = CVector.subtract(v, getPivot()).getMag();
	    if (distToCenter > collisionRadius) {
		collisionRadius = distToCenter;
	    }
	    pivot.addConnections(newV);
	}
	App.print("New coll radius: " + collisionRadius);
    }

    public List<CVector> getOutline() {
	return outline;
    }

    public Double getCollisionRadius() {
	return collisionRadius;
    }

    public int update() {
	if (hasTransformed) {
	    return 1;
	}
	for (Transform t: transforms) {
	    applyTransform(t);
	}
	endUpdate();
	return 0;
    }

    public String toString() {
	return "Projection: Vertices(" + outline.size() + "), Position" + pivot;
    }

}
