package sharp.unit;

import sharp.collision.Projection;
import sharp.collision.Collidable;
import sharp.collision.Collision;
import sharp.utility.CVector;
import sharp.utility.KinAnchor;
import sharp.utility.WrappedValue;
import sharp.utility.Transform;
import sharp.utility.Utility;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class LinkedUnit <T extends Projection> extends Unit<T> {

    private ArrayList<LinkedUnit<? extends Projection>> childLinks = new ArrayList<>();
    private LinkedUnit parentUnit;

    private WrappedValue<Double> rigidness = new WrappedValue<>(1.0);
    
    public LinkedUnit(T projection) {
	super(projection);
    }

    public LinkedUnit getParentUnit() {
	return parentUnit;
    }

    public LinkedUnit getRootParentUnit() {
	if (parentUnit != null) {
	    return parentUnit.getRootParentUnit();
	}
	return this;
    }

    public void setParentUnit(LinkedUnit parentUnit) {
	this.parentUnit = parentUnit;
    }

    public List<LinkedUnit<? extends Projection>> getSubUnits() {
	return childLinks;
    }

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

    public void addSubUnit(LinkedUnit<? extends Projection> u, CVector offset) {
	CVector diffPivot = CVector.subtract(getPivot(), u.getPivot());
	diffPivot.add(offset);
	Transform diff = new Transform(diffPivot.getX(), diffPivot.getY());
	u.applyTransform(diff);
	childLinks.add(u);
	u.setParentUnit(this);
    }

    public void addSubUnit(LinkedUnit<? extends Projection> u) {
	childLinks.add(u);
	u.setParentUnit(this);
    }
    
    public ArrayList<Collidable> getCollidables() {
	if (parentUnit != null) {
	    return getRootParentUnit().getCollidables();
	    
	}
	return super.getCollidables();
    }

    public void applyTransform(Transform t) {
	t.apply(this);
	for (LinkedUnit<? extends Projection> u: childLinks) {
	    t.apply(u);
	}
    }

    public void applyUnitTransform(Transform t) {
	super.applyTransform(t);
    }

    public void revertTransform(Transform t) {
	t.revert(this);
	for (LinkedUnit u: childLinks) {
	    t.revert(u);
	}
    }

    public void revertUnitTransform(Transform t) {
	super.revertTransform(t);
    }

    public double getRigidnessValue() {
	return rigidness.getValue();
    }

    public void setRigidnessValue(double value) {
	value = Utility.checkBounds(value, 0.0, 1.0);
	rigidness.setValue(value);
    }

    public void queueBounceForce(Force f) {
	getPivot().queueForce(f);
    }

    public Collidable applyContinuousTransform(Transform t) {
	boolean revert = false;
	for (int i = 0; i < childLinks.size(); i++) {
	    if (!revert) {
		Collidable c = childLinks.get(i).applyContinuousTransform(t);
		if (c != null) {
		    revert = true;
		}
	    } else {
		childLinks.get(i).revertTransform(t);
	    }
	    if (revert) {
		i -= 2;
		if (i < 1) {
		    break;
		}
	    }
	}
	applyUnitTransform(t);
	for (Collidable c: getCollidables()) {
	    CVector collPoint = Collision.collides(this, c, true);
	    if (collPoint != null) {
		applyRebound(c, t, 1.0 - rigidness.getValue());
		if (tryPushback(c, t, collPoint)) {
		    continue;
		}
		this.revertTransform(t);
		return c;
	    }
	}
	return null;
    }

    public boolean tryPushback(Collidable c, Transform t, CVector collPoint) {
	if (parentUnit == null || t.isTranslation()) {
	    return false;
	}
	
	revertTransform(t);
	
	CVector closePoint = Collision.getClosestPoint(this, collPoint, c);

	Transform pivotRotation = new Transform(closePoint, t.getRot() * rigidness.getValue());
	LinkedUnit<? extends Projection> rootParent = getRootParentUnit();
	CVector acceleration = new CVector(rootParent.getPivot());
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
	    applyTransform(t);
	    return false;
	} else {
	    acceleration.set(CVector.subtract(rootParent.getPivot(), acceleration));
	    rootParent.getPivot().queueForce(e -> e.getAcceleration().add(acceleration));
	    rootParent.getPivot().queueForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + pivotRotation.getRot() * 0.8));
	    return true;
	}
    }

    public void applyRebound(Collidable c, Transform t, double multiplier) {
	WrappedValue<Double> elastics = new WrappedValue<>(multiplier * c.getElasticity().getValue() + this.getElasticity().getValue());
	double massElastics = elastics.getValue() / (c.getMass().getValue() + this.getMass().getValue());
	double thisMult = c.getMass().getValue() * massElastics;
	double thatMult = this.getMass().getValue() * massElastics;
	if (t.isTranslation()) {
	    KinAnchor k = c.getKinAnchor();
	    if (k == null) {
		Force f = e -> e.getAcceleration().add(CVector.mult(e.getVelocity(), -elastics.getValue()));
		this.getPivot().queueForce(f);
	    } else {
		CVector trans = new CVector(t.getX(), t.getY());
		Force thisForce = e -> e.getAcceleration().add(CVector.mult(CVector.mult(trans, -2), thisMult));
		Force thatForce = e -> e.getAcceleration().add(CVector.mult(CVector.mult(trans, 2), thatMult));
		queueBounceForce(thisForce);
		c.queueBounceForce(thatForce);
	    }
	    
	} else {
	    if (elastics.getValue() <= 0.5) {
		elastics.setValue(0.501);
	    }
	    queueBounceForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + (-e.getRotVelocity().getValue() * thisMult * 2)));
	    KinAnchor k = c.getKinAnchor();
	    if (k != null) {
		c.queueBounceForce(e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + (e.getRotVelocity().getValue() * thatMult * 2)));
	    }
	}
    }
    
}