package sharp.unit;

import sharp.collision.*;
import sharp.utility.CVector;
import sharp.utility.Transform;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.Node;

public abstract class SimpleUnit implements Unit, Collidable {

    private CVector previousPosition = new CVector();
    private CVector velocity = new CVector();
    private CVector acceleration = new CVector();
    private Double rotVelocity = 0.0;
    private Double rotAcceleration = 0.0;
    private int priority;
    private boolean grav = true;

    public SimpleUnit() {
	Collision.setPriority(this);
    }

    public void setPreviousPosition(CVector previousPosition) {
	this.previousPosition.set(previousPosition);
    }

    public CVector getPreviousPosition() {
	return previousPosition;
    }

    public void setGrav(boolean grav) {
	this.grav = grav;
    }

    public boolean getGrav() {
	return grav;
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
	this.rotVelocity = rotVelocity;
    }

    public void setRotAcceleration(double rotAcceleration) {
	this.rotAcceleration = rotAcceleration;
    }

    public int getPriority() {
	return priority;
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }
    
    public void update() {
	setPreviousPosition(getProjection().getPivot());
	if (grav) {
	    Unit.GRAVITY.apply(this);
	}
	Unit.super.update();
	getProjection().update();
	LinkedList<Collidable> discreteCollisions = new LinkedList<>();
	List<Collidable> discUpd = discreteUpdate();
	boolean doneUpdating = true;
	if (discUpd != null) {
	    for (Collidable c: discUpd) {
		discreteCollisions.add(c);
	    }
	} else {
	    doneUpdating = !fineUpdate(discreteCollisions);
	}
	if (doneUpdating) {
	    this.endUpdate();
	}
    }

}
