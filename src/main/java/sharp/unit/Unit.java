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

/**
 * This class represents a {@code Collidable} object with a specific projection
 * that can be used in any simulation with physics or collision detection display.
 */
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

    /**
     * Creates a unit with a given {@code Projection}.
     *
     * @param projection - the projection to use
     */
    public Unit(T projection) {
	this.projection = projection;
	kinematics = new KinAnchor(projection.getPivot());
	previousPosition = new CVector(projection.getPivot());
	projection.transferPivot(kinematics, true);

	collidables = new ArrayList<>();
	collider = projection.createColliderArray(projection);
	Collision.setPriority(this);
	

	mass = Collidable.super.getMass();
	elasticity = Collidable.super.getElasticity();
    }

    /**
     * Retrieves the javafx node to use in displaying this unit.
     *
     * @return projection.getNode()
     */
    public Node getNode() {
	return projection.getNode();
    }

    /**
     * Retrieves the parameterized projection that this object stores.
     *
     * @return the projection of this unit
     */
    public T getProjection() {
	return projection;
    }

    /**
     * Sets the projection of this object equal to the given projection.
     *
     * @param projection - the new projection
     */
    public void setProjection(T projection) {
	this.projection = projection;
    }

    /**
     * Returns an array of {@code Projection}s which acts as this object's collider.
     *
     * @return the array of projections
     */
    public Projection[] getCollider() {
	return collider;
    }

    /**
     * Sets the array equal to the given array of projections.
     *
     * @param collider - the new collider
     */
    public void setCollider(T[] collider) {
	this.collider = collider;
    }

    /**
     * This method adds an array of parameterized projections to the current
     * list of colliders for this unit.
     *
     * @param colliders - any projections to add to the list
     */
    public <R extends Projection> void addColliders(R[] colliders) {
	int oldLength = this.collider.length;
	this.collider = Arrays.copyOf(this.collider, oldLength + colliders.length);
	for (int i = oldLength; i < this.collider.length; i++) {
	    this.collider[i] = colliders[i - oldLength];
	}
    }

    /**
     * Add a 2d array of projections to this array of colliders.
     *
     * @param colliders - the 2d array of projections
     */
    public void addColliders(T[][] colliders) {
	for (T[] colls: colliders) {
	    addColliders(colls);
	}
    }

    /**
     * Get the list of collidables that will cause collisions with this unit.
     *
     * @return the list of Collidables
     */
    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    /**
     * Sets the list of Collidable objects equal to the given list.
     * 
     * @param collidables - the new list of Collidables
     */
    public void setCollidables(ArrayList<Collidable> collidables) {
	this.collidables = collidables;
    }

    /**
     * This method sets this object to do (true) or not do (false) kinematics.
     * Kinematics include velocity, acceleration, and collision checking with the
     * collidables automatically each update.
     *
     * @param doKinematics - if this is true, it will do kinematics every update
     */
    public void setKinematics(boolean doKinematics) {
	doKinematics = doKinematics;
    }

    /**
     * Gets if this unit does kinematics.
     *
     * @return true if it does
     */
    public boolean getKinematics() {
	return doKinematics;
    }

    /**
     * Sets the priority number of this unit, used in the collisions class to determine
     * what order to check collisions and update transformations.
     *
     * @param priority - the new priority number, 0 being the highest priority
     */
    public void setPriority(int priority) {
	this.priority = priority;
    }

    /**
     * Gets the priority number of this Unit.
     *
     * @return the priority value
     */
    public int getPriority() {
	return priority;
    }

    /**
     * Gets the pivot of this unit. This unit uses a {@code KinAnchor} as its pivot
     * to allow use of kinematics.
     *
     * @return the pivot point
     */
    public KinAnchor getPivot() {
	return kinematics;
    }

    /**
     * Gets the position of the pivot in the previous update frame.
     *
     * @return the previous position of the pivot
     */
    public CVector getPreviousPosition() {
	return previousPosition;
    }

    /**
     * Sets the previous position equal to the given position. Used in this object's
     * update method to update the previous position.
     *
     * @param previousPosition - the new previousPosition
     */
    public void setPreviousPosition(CVector previousPosition) {
	this.previousPosition.set(previousPosition);
    }

    /**
     * Gets if this unit should show up in the display.
     * 
     * @return true if this unit should display
     */
    public boolean getShow() {
	return show;
    }

    /**
     * Set the value if this unit should show up in the display.
     *
     * @param show - true if this unit should display, false otherwise
     */
    public void setShow(boolean show) {
	this.show = show;
    }

    /**
     * Get the mass of this object, defaulting to 1.
     *
     * @return the wrapped value containing the mass
     */
    public WrappedValue<Double> getMass() {
	return mass;
    }

    /**
     * Gets the elasticity of this object, defaulting to 1/2.
     *
     * @return the wrapped value containing the elasticity
     */
    public WrappedValue<Double> getElasticity() {
	return elasticity;
    }

    /**
     * Sets the x value of this projection, relocating the pivot point
     * and any involved vertices.
     *
     * @param x - the new x coordinate
     */
    public void setX(double x) {
	projection.setX(x);
    }

    /**
     * Sets the y value of this projection, relocating the pivot point
     * and any involved vertices.
     *
     * @param y - the new y coordinate
     */
    public void setY(double y) {
	projection.setY(y);
    }

    /**
     * Sets the x and y value of this projection using a vector, relocating the pivot point
     * and any involved vertices.
     *
     * @param v - the new coordinate vector
     */
    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    /**
     * Gets the x value of this unit's pivot/projection.
     *
     * @return the x value of this unit
     */
    public double getX() {
	return projection.getX();
    }

    /**
     * Gets the y value of this unit's pivot/projection.
     *
     * @return the y value of this unit
     */
    public double getY() {
	return projection.getY();
    }

    /**
     * Rotates this unit around its pivot a given value.
     *
     * @param rot - the amount to rotate in radians
     */
    public void rotate(double rot) {
	projection.rotate(rot);
    }

    /**
     * Rotates this unit around a given pivot point by a given rotation.
     *
     * @param pivot - the coordinate vector to rotate this unit around
     * @param rot - the value in radians to rotate this unit
     */
    public void rotateAround(CVector pivot, double rot) {
	projection.rotateAround(pivot, rot);
    }

    /**
     * Gets a boolean determining if this unit is capable of rotating locally around
     * its own pivot. Usually will return true unless a special case occurs.
     *
     * @return true if this unit can rotate around its own pivot
     */
    public boolean canLocallyRotate() {
	return true;
    }

    /**
     * Gets the rotation that this unit has rotated from 0.
     *
     * @return the double value this unit has rotated in radians
     */
    public double getRotation() {
	return projection.getRotation();
    }

    /**
     * Get the current list of transforms that will be applied to this unit.
     * The transforms will be applied in order from index 0 to list.size().
     *
     * @return the list of transforms
     */
    public List<Transform> getTransforms() {
	return projection.getTransforms();
    }

    /**
     * Add a transform to the end of the current list of transforms to apply.
     *
     * @param t - a new transform to add to the list
     */
    public void addTransform(Transform t) {
	projection.addTransform(t);
    }

    /**
     * Get if this unit has transformed during this current update frame.
     *
     * @return true if this unit has applied and possibly reverted its transforms this frame
     */
    public boolean getHasTransformed() {
	return projection.getHasTransformed();
    }

    /**
     * Set if this unit has transformed during this current update frame. This is used
     * in both the update method to set true once it does and in the end update method
     * to reset the value to false.
     *
     * @param hasTransformed - the value if the unit has transformed this update frame
     */
    public void setHasTransformed(boolean hasTransformed) {
	projection.setHasTransformed(hasTransformed);
    }

    /**
     * Update this unit by updating the previous position, applying all transforms, and checking
     * if it has moved into and collided with the list of collidables this object contains.
     * If this unit collides, it is added to a list of collidables in the Collision class and
     * will undergo a continuous update.
     *
     * @param returns 0 if this runs sucessfully, otherwise a negative number
     */
    public int update() {
	App.print("Updating: " + this.toString());
	if (!getHasTransformed()) {
	    setHasTransformed(true);
	    previousPosition.set(getPivot());
	    kinematics.applyKinematics();
	    List<Collidable> collisions = discreteUpdate();
	    boolean queuedForUpdate = checkContinuousUpdate(collisions);
	    if (queuedForUpdate) {
		App.print(this.toString() + " is queued for collision updates.");
	    } else {
		endUpdate();
	    }
	} else {
	    App.print(this.toString() + " has already transformed");
	}
	return 0;
    }

    /**
     * This method marks the end of update in the current frame of this object,
     * running any maintenance method for this object to keep it updating smoothly.
     */
    public void endUpdate() {
	setHasTransformed(false);
	Collidable.super.endUpdate();
	projection.endUpdate();
	kinematics.endUpdate();
    }

    /**
     * This method applies a transform at the given index of transforms and checks
     * if this object collides with any collidables in its list. If this happens, the
     * transform is reverted and a bounce force is calculated.
     *
     * @param tIndex - the index in the transform list that determines which transform
     * will be used
     */
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
