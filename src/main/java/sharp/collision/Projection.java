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

/**
 * The projection class is the foundation for how units can interact with each other.
 * This class is used to detect collision, distance, and other information when
 * comparing polygons to each other. Most measurements depend on vectors. This class's
 * primary tool is using an {@code ArrayList<CVector>} to create an outline of points
 * that create the projection shape.
 */
public abstract class Projection implements Translatable {

    private LinkedList<Transform> transforms = new LinkedList<>();
    private boolean hasTransformed;
    private Anchor pivot;
    private Double rotation = new Double(0.0);
    private ArrayList<CVector> outline = new ArrayList<>();
    private Double collisionRadius = 0.0;

    /**
     * Default setup method will create an empty outline list and start at (0, 0).
     */
    protected void setup() {
	pivot = new Anchor(0, 0);
	outline = new ArrayList<>();
    }

    /**
     * This setup method with take a center point, passed as a {@code Anchor}, and will take
     * a varargs list of {@code Anchor} points to create the outline.
     *
     * @param pivot - the center coords of the projection
     * @param outline - the outlined points relative to the center
     */
    protected void setup(Anchor pivot, CVector ... outline) {
	this.pivot = pivot;
	this.outline = new ArrayList<CVector>();
	setOutline(outline);
    }

    /**
     * @return The Node object that is used to base the projection off of.
     */
    public abstract <T extends Node> T getNode();

    /**
     * @return A {@code Projection[]} which is essentially a copy of the outline.
     */
    public Projection[] getCollider(){
	return new Projection[]{this};
    }

    /**
     * @return The {@code Anchor} that marks the "center" of this projection.
     */
    public Anchor getPivot() {
	return pivot;
    }

    /**
     * This method will set the pivot of this projection to reference the given pivot.
     *
     * @param pivot - the new pivot to use as a reference
     */
    protected void setPivot(Anchor pivot) {
	this.pivot = pivot;
    }

    /** 
     * Getter.
     * @return the x coord of the projection pivot. 
     */
    public double getX() {
	return pivot.getX();
    }

    /** 
     * Setter.
     * @return the y coord of the projection pivot. 
     */
    public double getY() {
	return pivot.getY();
    }

    /** 
     * Setter.
     * @param the new x coord of this projection pivot. 
     */
    public void setX(double x) {
	pivot.setX(x);
    }

    /** @param the new y coord of this projection pivot. */
    public void setY(double y) {
	pivot.setY(y);
    }

    /**
     * This method rotates the projection and all of the outline points
     * with respect to the pivot.
     *
     * @param rot - the value in radians to rotate by
     */
    public void rotate(double rot) {
	pivot.rotateAnchor(rot);
	rotation += rot;
    }

    /**
     * This method rotates the projection and all of the outline points
     * with respect to the passed {@code CVector}.
     *
     * @param pivot - the point to rotate around
     * @param rot - the value in radians to rotate by
     */
    public void rotateAround(CVector pivot, double rot) {
	this.pivot.rotateAround(pivot, rot);
	rotation += rot;
    }

    /**
     * This method is used to signify that this projection can rotate
     * locally. This means it has a center pivot that will be used to rotate
     * corresponding outline points.
     *
     * @return true as default
     */
    public boolean canLocallyRotate() {
	return true;
    }

    /**
     * Getter.
     * @return the list of transforms this unit has at the immediate time
     */
    public LinkedList<Transform> getTransforms() {
	return transforms;
    }

    /**
     * This method simply adds a passed transform into the list of transforms.
     * @param t - the passed transform
     */
    public void addTransform(Transform t) {
	transforms.add(t);
    }

    /**
     * Getter.
     * This method is used to signify if this projection has applied and finished
     * its transforms during this frame update.
     * @return true if it has applied transforms and not undone them
     */
    public boolean getHasTransformed() {
	return hasTransformed;
    }

    /**
     * Setter.
     * @param hasTransformed - the value of hasTransformed
     */
    public void setHasTransformed(boolean hasTransformed) {
	this.hasTransformed = hasTransformed;
    }

    /**
     * Getter.
     * @return the rotation value of this projection.
     */
    public Double getRotation() {
	return rotation;
    }

    /**
     * Setter.
     * Sets the outline of this projection using a varargs parameter.
     * @param outline - the list of {@code CVector} points
     */
    public void setOutline(CVector ... outline) {
	LinkedList<CVector> newOutline = new LinkedList<>();
	for (CVector v: outline) {
	    newOutline.add(v);
	}
	setOutline(newOutline);
    }

    /**
     * Setter.
     * Sets the outline of this projection, but first deletes the previous one.
     * Outlines are added to the list of the connections in this projection's
     * anchor object.
     * @param outline - the {@code List<CVector>} of points
     */
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

    /**
     * Getter.
     * @return the {@code List<CVector>} of this projection
     */
    public List<CVector> getOutline() {
	return outline;
    }

    /**
     * Getter.
     * Returns the value of the distance between the pivot and the furthest
     * points in the outline {@code List<CVector>}.
     * @return the furthest inner distance from pivot to a point
     */
    public Double getCollisionRadius() {
	return collisionRadius;
    }

    /**
     * This method is the default for updating this projection by applying transforms.
     * If this unit has {@code hasTransformed} set to {@code True}, then it will not
     * transform. At the end of the update call, endUpdate is called, using the translatable
     * implementation of it, if this updated. This will set {@code hasTransformed} to {@code False}.
     * @param returns an int value, 0 if this method successfully tranformed, -1 if not
     */
    public int update() {
	if (hasTransformed) {
	    return -1;
	}
	for (Transform t: transforms) {
	    applyTransform(t);
	}
	endUpdate();
	return 0;
    }

    /**
     * This returns a string stating this is a projection with a given amount
     * of vertices (equal to {@code List<CVector>.size()}) and a position (equal
     * to the (x, y) value of the anchor).
     * @return string description of this projection
     */
    public String toString() {
	return "Projection: Vertices(" + outline.size() + "), Position" + pivot;
    }

    /**
     * Simple method to take in a varargs param and immediately return it.
     * @return a {@code Projection[]}
     */
    public Projection[] createColliderArray(Projection ... projs) {
	return projs;
    }

}
