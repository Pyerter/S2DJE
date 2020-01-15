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

    public default void addCollidables(Collection<Collidable> c) {
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
    
    public default List<Collidabe> discreteUpdate() {
	App.print("Discrete updating: " + this);
	if (Collision.willContinuousUpdate(this)) {
	    App.print(this.toString() + " is already queued for a continous update.");
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

    public default void revertDiscreteUpdate() {
	for (int i = getTransforms().size() - 1; i >= 0; i--) {
	    this.revertTransform(t);
	}
    }

    public default boolean checkContinuousUpdate(List<Collidable> collidables) {
	if (collidables == null) {
	    return true;
	} else if (collidables.size() == 0) {
	    return false;
	}
	
	revertDiscreteUpdate();
	
	Collidable[] arr = new Collidable[collidables.size() + 1];
	for (int i = 0; i < collidables.size(); i++) {
	    arr[i] = collidables.get(i);
	}
	arr[arr.length - 1] = this;
	Collision.queueContinuousCollidables(arr);

	return true;
    }
    
    public default void continuousUpdate(int tIndex) {
	if (getTransforms().size() > index) {
	    applyContinousTransform(getTransforms().get(tIndex));
	}
    }

    public default Collidable applyContinuousTransform(Transform t) {
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

    public default WrappedValue<Double> getMass() {
	return new WrappedValue<Double>(1.0);
    }

    public default WrappedValue<Double> getElasticity() {
	return new WrappedValue<Double>(0.45);
    }

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

    /**
     * This method returns a vector that represents the magnitude and direction
     * of the momentum of this unit in its current frame. This will be useful
     * when calculating how objects bounce off of each other.
     *
     * @return the vector representing momentum
     */
    public default CVector getTransformMomentum() {
	return CVector.mult(getTotalTransform(), getMass());
    }

    public default void queueBounceForce(Force f) {
	App.print(this.toString() + " has not implemented queueBounceForce(Force f)");
    }
    
}
