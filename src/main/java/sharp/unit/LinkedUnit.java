package sharp.unit;

import sharp.game.App;
import sharp.collision.Projection;
import sharp.collision.Collidable;
import sharp.collision.Collision;
import sharp.utility.CVector;
import sharp.utility.KinAnchor;
import sharp.utility.WrappedValue;
import sharp.utility.Transform;
import sharp.utility.Utility;

import javafx.scene.Group;
import javafx.scene.Node;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a unit that has a list of units with which it is linked.
 * These units will move as if hinged, depending on the rigidness of the unit.
 */
public class LinkedUnit <T extends Projection> extends Unit<T> {

    private ArrayList<LinkedUnit<? extends Projection>> childLinks = new ArrayList<>();
    private LinkedUnit<? extends Projection> parentUnit;

    private WrappedValue<Double> rigidness = new WrappedValue<>(1.0);

    private Group nodes = new Group();

    private LinkedList<Transform> pushbacks = new LinkedList<>();

    /**
     * Constructor given a projection to copy.
     * 
     * @param projection - the projection to copy
     */
    public LinkedUnit(T projection) {
	super(projection);
	nodes.getChildren().add(super.getNode());
    }

    /**
     * Returns the group of javafx {@code Node}s to display from this linked unit.
     *
     * @return the {@code Group} of nodes
     */
    public Node getNode() {
	return nodes;
    }

    /**
     * Gets the parent {@code LinkedUnit} of this unit.
     *
     * @return the parent unit
     */
    public LinkedUnit<? extends Projection> getParentUnit() {
	return parentUnit;
    }

    /**
     * This recursively returns the parent unit until there is no higher parent unit.
     *
     * @return the highest parent unit
     */
    public LinkedUnit<? extends Projection> getRootParentUnit() {
	if (parentUnit != null) {
	    return parentUnit.getRootParentUnit();
	}
	return this;
    }

    /**
     * Gets an array of all colliders that this unit creates with it's family.
     *
     * @return the total array of {@code Collidable}s
     */
    public Collidable[] getPersonalContinuousCollidables() {
	return new Collidable[]{getRootParentUnit()};
    }

    /**
     * Sets a new parent unit.
     *
     * @param parentUnit - the new parent unit of this object
     */
    public void setParentUnit(LinkedUnit<? extends Projection> parentUnit) {
	this.parentUnit = parentUnit;
    }

    /**
     * Retrieve all immediate child units of this {@code LinkedUnit}.
     *
     * @return a {@code List} of all immediate child units
     */
    public List<LinkedUnit<? extends Projection>> getSubUnits() {
	return childLinks;
    }

    /**
     * Recursively retrieve all child units starting from this unit.
     *
     * @return a {@code List} of all child units
     */
    public List<LinkedUnit<? extends Projection>> getAllSubUnits() {
	LinkedList<LinkedUnit<? extends Projection>> units = new LinkedList<>();
	for (LinkedUnit<? extends Projection> u: childLinks) {
	    units.add(u);
	    for (LinkedUnit<? extends Projection> subs: u.getAllSubUnits()) {
		units.add(subs);
	    }
	}
	return units;
    }

    /**
     * Adds a child unit to this unit at a given offset. It joins the unit family!
     *
     * @param u - the linked unit to add to this linked unit
     * @param offset - a {@code CVector} representing the offset in coordinates to this unit's anchor
     */
    public void addSubUnit(LinkedUnit<? extends Projection> u, CVector offset) {
	CVector diffPivot = CVector.subtract(getPivot(), u.getPivot());
	diffPivot.add(offset);
	Transform diff = new Transform(diffPivot.getX(), diffPivot.getY());
	u.applyTransform(diff);
	childLinks.add(u);
	u.setParentUnit(this);
	nodes.getChildren().add(u.getNode());
	addSubUnitColliders(u);
    }

    /**
     * Adds a child unit to this unit. It joins the unit family!
     *
     * @param u - the linked unit to add to this linked unit
     */
    public void addSubUnit(LinkedUnit<? extends Projection> u) {
	childLinks.add(u);
	u.setParentUnit(this);
	nodes.getChildren().add(u.getNode());
	addSubUnitColliders(u);
    }

    /**
     * Adds the colliders from the given linked unit to this unit's colliders.
     *
     * @param u - the given projection to get the colliders from
     */
    public void addSubUnitColliders(LinkedUnit<? extends Projection> u) {
	super.addColliders(u.getCollider());
    }

    /**
     * Retrieves the collidables from the root parent unit.
     *
     * @return a {@code List} of collidables
     */
    public ArrayList<Collidable> getCollidables() {
	if (parentUnit != null) {
	    return getRootParentUnit().getCollidables();
	    
	}
	return super.getCollidables();
    }

    /**
     * Applies a transform to this object and all child linked units of this object.
     *
     * @param t - the transform to apply
     */
    public void applyTransform(Transform t) {
	t.apply(this);
	for (LinkedUnit<? extends Projection> u: childLinks) {
	    u.applyTransform(t);
	}
    }

    /**
     * Aplies a transform to only this object and not the child links.
     *
     * @param t - the transform to apply
     */
    public void applyUnitTransform(Transform t) {
	super.applyTransform(t);
    }

    /**
     * Reverts a transform on this object and all child linked units of this object.
     *
     * @param t - the transform to revert
     */
    public void revertTransform(Transform t) {
	t.revert(this);
	for (LinkedUnit u: childLinks) {
	    u.revertTransform(t);
	}
    }

    /**
     * Reverts a transform on only this object and not the child links.
     *
     * @param t - the transform to revert
     */
    public void revertUnitTransform(Transform t) {
	super.revertTransform(t);
    }

    /**
     * Retrieve all transforms that were the cause of a pushback or rebound.
     * 
     * @return the list of transforms
     */
    public LinkedList<Transform> getPushbacks() {
	return pushbacks;
    }

    /**
     * Get the rigidness value, used to measure how much linked unit children should
     * rotate on collision.
     *
     * @return a double from 0.0 - 1.0
     */
    public double getRigidnessValue() {
	return rigidness.getValue();
    }

    /**
     * Set the rigidness value of this linked unit.
     *
     * @param value - a double between 0.0 and 1.0 (if not, it will be bounded by this range)
     */
    public void setRigidnessValue(double value) {
	value = Utility.checkBounds(value, 0.0, 1.0);
	rigidness.setValue(value);
    }

    /**
     * Add a force to a list of forces to apply.
     *
     * @param f - the force to apply
     */
    public void queueBounceForce(Force f) {
	getPivot().queueForce(f);
    }

    /**
     * Run the continuous update method, updating this unit while avoiding and bouncing
     * off all collisions. The requested transform at index {@code tIndex} will be applied
     * and handled if a collision occurs.
     *
     * @param tIndex - the index value of the transform to apply
     */
    public void continuousUpdate(int tIndex) {
	if (getTransforms().size() > tIndex) {
	    Collidable c = applyContinuousTransform(getTransforms().get(tIndex));
	}
    }

    /**
     * Apply a single transform while checking and correcting any occuring collisions.
     * This transform applies to the furthest child down the list of children first.
     * If a collision is detected, it will correct at that child and a new transform will
     * be created to adjust the movement accordingly.
     *
     * @param t - the transform to attempt
     * @return a Collidable object that caused a collision if transform t caused a collision
     */
    public Collidable applyContinuousTransform(Transform t) {
	boolean revert = false;
	Collidable c = null;
	for (int i = 0; i < childLinks.size(); i++) {
	    if (!revert) {
		c = childLinks.get(i).applyContinuousTransform(t);
		if (c != null) {
		    revert = true;
		}
	    } else {
		childLinks.get(i).revertTransform(t);
	    }
	    if (revert) {
		i -= 2;
		if (i < -1) {
		    return c;
		}
	    }
	}
	applyUnitTransform(t);
	for (Collidable c2: getCollidables()) {
	    CVector collPoint = Collision.collides(this, c2, true);
	    if (collPoint != null) {
		
		App.print("Applying rebound between " + this.toString() + " and " + c2.toString());
		if (tryPushback(c2, t, collPoint)) {
		    continue;
		}
		applyRebound(c2, t, rigidness.getValue());
		if (revert) {
		    this.revertUnitTransform(t);
		} else {
		    this.revertTransform(t);
		}
		return c2;
	    }
	}
	return c;
    }

    /**
     * Used when a collision occured using a transform against a certain collidable object at a known point.
     * This method attempts to use this information to alter how the object was moved and rotate linked units
     * as if they were hinged to make the collision semi-realistic. A new transform is created to move the
     * unit as close as possible to the Collidable and rotating the unit to account for the lost movement.
     *
     * @param c - the Collidable that the collision occurred with
     * @param t - the transform that caused the collision
     * @param collPoint - the point at which the collision was detected (or the closest estimated point)
     * @return true if the pushback was successful, false if the pushback calculation caused another collision
     */
    public boolean tryPushback(Collidable c, Transform t, CVector collPoint) {
	if (t.isTranslation() || getRootParentUnit().getPushbacks().contains(t)) {
	    return false;
	}
	
	getRootParentUnit().revertTransform(t);
	
	CVector closePoint = Collision.getClosestPoint(this, collPoint, c);

	Transform pivotRotation = new Transform(closePoint, t.getRot() * rigidness.getValue());
	Transform smallPivotRotation = new Transform(getPivot(), t.getRot() * (-1 + rigidness.getValue()));
	LinkedUnit<? extends Projection> rootParent = getRootParentUnit();
	CVector acceleration = new CVector(rootParent.getPivot());
	applyTransform(smallPivotRotation);
	rootParent.applyTransform(pivotRotation);
	boolean revert = false;
	for (Collidable coll: rootParent.getCollidables()) {
	    if (Collision.collides(rootParent, coll)) {
		revert = true;
		break;
	    }
	    for (LinkedUnit<? extends Projection> u: rootParent.getAllSubUnits()) {
		if (Collision.collides(u, coll)) {
		    revert = true;
		    break;
		}
	    } 
	}
	
	if (revert) {
	    rootParent.revertTransform(pivotRotation);
	    revertTransform(smallPivotRotation);
	    rootParent.applyTransform(t);
	    return false;
	} else {
	    acceleration.set(CVector.subtract(rootParent.getPivot(), acceleration));
	    rootParent.getPivot().queueForce(e -> e.getAcceleration().add(acceleration));
	    rootParent.getPivot().queueForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + pivotRotation.getRot()));
	    rootParent.getPushbacks().add(t);
	    return true;
	}
    }

    /**
     * This method takes a transform that caused a collision between this object and a given Collidable
     * and creates a bounce force that will be applied to both this object and the Collidable if applicable.
     *
     * @param c - the Collidable object that this unit collided with
     * @param t - the transform that caused the collision
     * @param multiplier - the multiplier applied to the bounce force to determine how "elastic" the object is
     */
    public void applyRebound(Collidable c, Transform t, double multiplier) {
	WrappedValue<Double> elastics = new WrappedValue<>(c.getElasticity().getValue() + this.getElasticity().getValue());
	double massElastics = elastics.getValue() / (c.getMass().getValue() + multiplier * getRootParentUnit().getMass().getValue());
	double thisMult = (c.getMass().getValue()) * massElastics;
	double thatMult = (getRootParentUnit().getMass().getValue()) * massElastics;
	if (t.isTranslation()) {
	    CVector trans = new CVector(t.getX(), t.getY());
	    KinAnchor k = c.getKinAnchor();
	    if (k == null) {
		Force f = e -> e.getAcceleration().add(CVector.mult(trans, -2 * elastics.getValue()));
		getRootParentUnit().queueBounceForce(f);
	    } else {
		Force thisForce = e -> e.getAcceleration().add(CVector.mult(CVector.mult(trans, -2), thisMult));
		Force thatForce = e -> e.getAcceleration().add(CVector.mult(CVector.mult(trans, 2), thatMult));
		getRootParentUnit().queueBounceForce(thisForce);
		c.queueBounceForce(thatForce);
	    }
	    
	} else {
	    if (elastics.getValue() <= 0.6) {
		elastics.setValue(0.601);
	    }
	    KinAnchor k = c.getKinAnchor();
	    if (k != null) {
		getRootParentUnit().queueBounceForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + (-e.getRotVelocity().getValue() * thisMult * 2)));
		c.queueBounceForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + (e.getRotVelocity().getValue() * thatMult * 2)));
	    } else {
		getRootParentUnit().queueBounceForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + (-e.getRotVelocity().getValue() * elastics.getValue() * 2)));
	    }
	}
    }
    
}
