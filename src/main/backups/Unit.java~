package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Transform;
import sharp.utility.Translatable;
import sharp.utility.KinAnchor;
import sharp.utility.WrappedValue;
import sharp.collision.Projection;
import sharp.collision.Collidable;
import sharp.collision.Collision;
import sharp.game.App;

import javafx.scene.Node;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Unit <T extends Projection> implements Collidable {

    public static final Double MAX_SPIN = Math.PI / 6;

    private T projection;
    private Projection[] collider;
    private ArrayList<Collidable> collidables;
    private WrappedValue<Double> mass;
    private WrappedValue<Double> elasticity;
    private int priority;
    
    private KinAnchor kinematics;
    private boolean doKinematics = true;
    private CVector previousPosition;
    private boolean show;
    
    public Unit(T projection) {
	this.projection = projection;
	collider = projection.createColliderArray(projection);
	Collision.setPriority(this);
	previousPosition = new CVector(projection.getPivot());
	mass = Collidable.super.getMass();
	elasticity = Collidable.super.getElasticity();
    }

    public Node getNode() {
	return projection.getNode();
    }

    public T getProjection() {
	return projection;
    }
    
    public void setProjection(T projection) {
	this.projection = projection;
    }
    
    public Projection[] getCollider() {
	return collider;
    }

    public void setCollider(T[] collider) {
	this.collider = collider;
    }

    public void addColliders(T ... colliders) {
	int oldLength = this.collider.length;
	this.collider = Arrays.copyOf(this.collider, oldLength + colliders.length);
	for (int i = 0; i < this.collider.length; i++) {
	    this.collider[i + oldLength] = colliders[i];
	}
    }

    public void addColliders(T[] ... colliders) {
	for (T[] colls: colliders) {
	    addColliders(colls);
	}
    }

    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public void setCollidables(ArrayList<Collidable> collidables) {
	this.collidables = collidables;
    }

    public void setKinematics(boolean doKinematics) {
	doKinematics = doKinematics;
    }

    public boolean getKinematics() {
	return doKinematics;
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }

    public int getPriority() {
	return priority;
    }

    public KinAnchor getPivot() {
	return kinematics;
    }

    public CVector getPreviousPosition() {
	return previousPosition;
    }

    public void setPreviousPosition(CVector previousPosition) {
	this.previousPosition.set(previousPosition);
    }
    
    public boolean getShow() {
	return show;
    }
    
    public void setShow(boolean show) {
	this.show = show;
    }

    public WrappedValue<Double> getMass() {
	return mass;
    }

    public WrappedValue<Double> getElasticity() {
	return elasticity;
    }

    public void setX(double x) {
	projection.setX(x);
    }

    public void setY(double y) {
	projection.setY(y);
    }

    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    public double getX() {
	return projection.getX();
    }

    public double getY() {
	return projection.getY();
    }

    public void rotate(double rot) {
	projection.rotate(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	projection.rotateAround(pivot, rot);
    }

    public boolean canLocallyRotate() {
	return true;
    }

    public double getRotation() {
	return projection.getRotation();
    }

    public List<Transform> getTransforms() {
	return projection.getTransforms();
    }

    public void addTransform(Transform t) {
	projection.addTransform(t);
    }

    public boolean getHasTransformed() {
	return projection.getHasTransformed();
    }

    public void setHasTransformed(boolean hasTransformed) {
	projection.setHasTransformed(hasTransformed);
    }

    public int update() {
	App.print("Updating: " + this.toString());
	if (!getHasTransformed()) {
	    previousPosition.set(getPivot());
	    kinematics.applyKinematics();
	    List<Collidable> collisions = discreteUpdate();
	    boolean queuedForUpdate = checkContinuousUpdate(collisions);
	    App.print("this is queued for collision updates.");
	} else {
	    App.print("This unit has already transformed");
	}
	return 0;
    }

    public void endUpdate() {
	Collidable.super.endUpdate();
	projection.endUpdate();
	kinematics.endUpdate();
    }

    public void continuousUpdate(int tIndex) {
	if (getTransforms().size() > tIndex) {
	    Collidable c = applyContinuousTransform(getTransforms().get(tIndex));
	    if (c != null) {
		c.queueBounceForce(getTransforms().get(tIndex).getAsForce(c.getElasticity().getValue() * 2));
		this.queueBounceForce(getTransforms().get(tIndex).getAsForce(-this.getElasticity().getValue() * 2));
	    }
	}
    }
    
}
