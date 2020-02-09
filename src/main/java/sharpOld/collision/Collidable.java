package sharp.collision;

import sharp.game.App;
import sharp.utility.Translatable;
import sharp.utility.Transform;
import sharp.utility.CVector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface Collidable extends Translatable {

    public ArrayList<Collidable> getCollidables();

    public default void setCollidables(ArrayList<Collidable> collidables) {
	App.print("setCollidables(ArrayList<Collidable> collidables) not implemented in " + this);
    }

    public default void addCollidables(Collidable ... c) {
	for (Collidable coll: c) {
	    if (!getCollidables().contains(coll)) {
		getCollidables().add(coll);
	    }
	}
    }
    
    public Projection[] getCollider();

    public default List<Collidable> getCollisions() {
	LinkedList<Collidable> collidedWith = new LinkedList<>();
	for (Collidable c: getCollidables()) {
	    if (Collision.collides(this, c)) {
		collidedWith.add(c);
	    }
	}
	return collidedWith;
    }
    
    public default List<Collidable> discreteUpdate() {
	App.print("Discrete updating: " + this);
	if (Collision.willFineUpdate(this)) {
	    return null;
	}
	for (Transform t: getTransforms()) {
	    this.applyTransform(t);
	}
	List<Collidable> collidedWith = getCollisions();
	if (collidedWith.size() == 0) {
	    setHasTransformed(true);
	}
	return collidedWith;
    }
    
    public default boolean fineUpdate(List<Collidable> collidables) {
	if (collidables == null) {
	    return true;
	} else if (collidables.size() == 0) {
	    return false;
	}
	reset();
	Collidable[] arr = new Collidable[collidables.size() + 1];
	for (int i = 0; i < collidables.size(); i++) {
	    arr[i] = collidables.get(i);
	}
	arr[arr.length - 1] = this;
	Collision.addFineColliders(arr);
	return true;
    }

    public default Collidable applyFineTransform(Transform t) {
	this.applyTransform(t);
	for (Collidable c: getCollidables()) {
	    if (Collision.collides(this, c)) {
		this.revertTransform(t);
		return c;
	    }
	}
	return null;
    }

    public CVector getPreviousPosition();

    public void setPreviousPosition(CVector previousPosition);
    
    public int getPriority();

    public void setPriority(int priority);

    public default Double getMass() {
	return 1.0;
    }

    public default void setElasticity(double d) {
	// it's fine if this isn't implemented
	App.print("Err - setElasticity() not implemented in " + this);
    }
    
    public default Double getElasticity() {
	return 0.45;
    }

    // This might be tricky... problem for later when conservation of momentum
    // might become a concern

    /**
     * This method returns a vector that represents the magnitude and direction
     * of the total transformation this object is /suppposed/ to go through.
     * This can be useful when calculating how a collision might work.
     *
     * @return the vector representing the difference in transformations
     */
    public default CVector getTotalTransform() {
	CVector transformation = new CVector(getPreviousPosition());
	for (Transform t: this.getTransforms()) {
	    transformation.applyTransform(t);
	}
	return CVector.subtract(transformation, getPreviousPosition());
    }

    public default CVector getTransformMomentum() {
	return CVector.mult(getTotalTransform(), getMass());
    }
    
}