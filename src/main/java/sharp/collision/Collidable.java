package sharp.collision;

import sharp.game.App;
import sharp.unit.Force;
import sharp.utility.Translatable;
import sharp.utility.Transform;
import sharp.utility.CVector;
import sharp.utility.WrappedValue;
import sharp.utility.KinAnchor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

/** This class represents any object with a projection that can collide with another. */
public interface Collidable extends Translatable {

    /**
     * Getter.
     * @return the list of collidables this object checks with
     */
    public ArrayList<Collidable> getCollidables();

    /**
     * Setter.
     * @param collidables - the list of collidables to check with this objects
     */
    public default void setCollidables(ArrayList<Collidable> collidables) {
	App.print("setCollidables(ArrayList<Collidable> collidables) not implemented in " + this);
    }

    /**
     * This method adds any given collidables to the list to check with.
     *
     * @param c - the collidables to add
     */
    public default void addCollidables(Collidable ... c) {
	for (Collidable coll: c) {
	    if (!getCollidables().contains(coll)) {
		getCollidables().add(coll);
	    }
	}
    }

    /**
     * This method adds any given collidables to the list to check with.
     * 
     * @param c - the collidables to add
     */
    public default void addCollidables(Collection<Collidable> c) {
	for (Collidable coll: c) {
	    if (!getCollidables().contains(coll)) {
		getCollidables().add(coll);
	    }
	}
    }

    /**
     * Getter.
     * @return the array of projections of this object
     */
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

    /**
     * Run the discrete update of this object. Update this collidable with all
     * transforms it has and check for collision between all collidables.
     * Return the list of any collidables found that this collided with.
     * 
     * @return any collided collidables
     */
    public default List<Collidable> discreteUpdate() {
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

    /** Revert the transforms done by discrete update. */
    public default void revertDiscreteUpdate() {
	for (int i = getTransforms().size() - 1; i >= 0; i--) {
	    this.revertTransform(getTransforms().get(i));
	}
    }

    /**
     * Check the collidable list to see if a continuous update is needed
     * with this unit. If one is needed, revert the discrete update
     * and add any collided units and this one to the continuous update.
     *
     * @param collidables - the list of any founded {@code Collidable}s after
     * running the discrete update
     * @return true if this was added to the continuous updates
     */
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

    /**
     * Apply transforsm at a given index during a continuous update.
     * 
     * @param tIndex - the transform list index to check to use
     */
    public default void continuousUpdate(int tIndex) {
	if (getTransforms().size() > tIndex) {
	    applyContinuousTransform(getTransforms().get(tIndex));
	}
    }

    /**
     * Apply a transform and check for collision between any collidables
     * and this after the application of the transform. Revert if collision detected.
     * 
     * @param t - the transform to attempt at applying
     * @return any collided objects with this
     */
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

    /**
     * Setter.
     * @param previousPosition - the previous position of this collidable
     * at the beginning of the frame update before translations
     */
    public void setPreviousPosition(CVector previousPosition);

    /**
     * Getter.
     * @return the priority value
     */
    public int getPriority();

    /**
     * Setter.
     * @param priority - the new priority value
     */
    public void setPriority(int priority);

    /**
     * Retrieve the mass value of the collidable. Default to 1.0.
     * @return the wrapped value of the collidable's mass
     */
    public default WrappedValue<Double> getMass() {
	return new WrappedValue<Double>(1.0);
    }

    /**
     * Retrieve the elasticity value of the collidable. Default to 0.45.
     * @return the wrapped value of the collidable's elasticity
     */
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
	return CVector.mult(getTotalTransform(), getMass().getValue());
    }

    /**
     * This method has to have an override to queue a force to this collidable.
     */
    public default void queueBounceForce(Force f) {
	App.print(this.toString() + " has not implemented queueBounceForce(Force f)");
    }

    /**
     * This method has to have an override to retrieve any kinematic anchor used by the collidable.
     * @return any found {@code KinAnchor}
     */
    public default KinAnchor getKinAnchor() {
	App.print(this.toString() + " has not implemented getKinAnchor()");
	return null;
    }
    
}
