package sharp.unit;

import sharp.collision.*;
import sharp.utility.CVector;
import sharp.utility.Transform;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.Node;

public abstract class SimpleUnit implements Unit, Collidable {

    private ArrayList<Collidable> collidables;
    private Projection projection;
    private CVector velocity;
    private CVector acceleration;
    private Double rotVelocity;
    private Double rotAcceleration;
    private int priority;

    public SimpleUnit() {
	projection = new Projection();
	transforms = new LinkedList<>();
	Collision.setPriority(this);
    }
    
    public SimpleUnit(Projection projection) {
	transforms = new LinkedList<>();
	Collision.setPriority(this);
    }

    public Projection getCollider() {
	return projection;
    }

    public Projection getProjection() {
	return projection;
    }

    public void setProjection(Projection projection) {
	this.projection = projection;
    }

    public CVector getPivot() {
	return projection.getPivot();
    }

    public double getX() {
	return projection.getPivot().getX();
    }

    public double getY() {
	return projection.getPivot().getY();
    }

    public void setX(double x) {
	projection.getPivot().setX(x);
    }

    public void setY(double y) {
	projection.getPivot().setY(y);
    }

    public void rotate(double rot) {
	projection.getPivot().rotateAnchor(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	projection.getPivot().rotateAround(pivot, rot);
    }

    public boolean canLocallyRotate() {
	return true;
    }

    public List<Transform> getTransforms() {
	return projection.getTransforms();
    }

    public void addTransform(Transform t) {
	projection.addTransform(t);
    }

    public void applyTransform(Transform t) {
	t.apply(projection);
    }

    public void revertTransform(Transform t) {
	t.revert(projection);
    }

    public boolean getHasTransformed() {
	return projection.getHasTransformed();
    }

    public void setHasTransformed(boolean hasTransformed) {
	projection.setHasTransformed(hasTransformed);
    }

    public CVector getVelocity() {
	return velocity;
    }

    public CVector getAcceleration() {
	return acceleration;
    }

    public void setVelocity(CVector velocity) {
	this.velocity.set(velocity);
    }

    public void setAcceleration(CVector acceleration) {
	this.acceleration.set(acceleration);
    }

    public Double getRotVelocity() {
	return rotVelocity;
    }

    public Double getRotAcceleration() {
	return rotAcceleration;
    }

    public void setRotVelocity(double rotVelocity) {
	this.rotVelocity.set(rotVelocity);
    }

    public void setRotAcceleration(double rotAcceleration) {
	this.rotAcceleration.set(rotAcceleration);
    }

    public ArrayList<Collidable> getCollidables() {
	return collidabes;
    }

    public int getPriority() {
	return priority;
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }

    public Node getNode();

}
