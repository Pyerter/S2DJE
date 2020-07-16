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

public class LinkedUnit <T extends Projection> extends Unit<T> {

    private ArrayList<LinkedUnit<? extends Projection>> childLinks = new ArrayList<>();
    private LinkedUnit<? extends Projection> parentUnit;

    private WrappedValue<Double> rigidness = new WrappedValue<>(1.0);

    private Group nodes = new Group();

    private LinkedList<Transform> pushbacks = new LinkedList<>();
    
    public LinkedUnit(T projection) {
	super(projection);
	nodes.getChildren().add(super.getNode());
    }

    public Node getNode() {
	return nodes;
    }

    public LinkedUnit<? extends Projection> getParentUnit() {
	return parentUnit;
    }

    public LinkedUnit<? extends Projection> getRootParentUnit() {
	if (parentUnit != null) {
	    return parentUnit.getRootParentUnit();
	}
	return this;
    }

    public Collidable[] getPersonalContinuousCollidables() {
	return new Collidable[]{getRootParentUnit()};
    }

    public void setParentUnit(LinkedUnit<? extends Projection> parentUnit) {
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
	nodes.getChildren().add(u.getNode());
	addSubUnitColliders(u);
    }

    public void addSubUnit(LinkedUnit<? extends Projection> u) {
	childLinks.add(u);
	u.setParentUnit(this);
	nodes.getChildren().add(u.getNode());
	addSubUnitColliders(u);
    }

    public void addSubUnitColliders(LinkedUnit<? extends Projection> u) {
	super.addColliders(u.getCollider());
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
	    u.applyTransform(t);
	}
    }

    public void applyUnitTransform(Transform t) {
	super.applyTransform(t);
    }

    public void revertTransform(Transform t) {
	t.revert(this);
	for (LinkedUnit u: childLinks) {
	    u.revertTransform(t);
	}
    }

    public void revertUnitTransform(Transform t) {
	super.revertTransform(t);
    }

    public LinkedList<Transform> getPushbacks() {
	return pushbacks;
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

    public void continuousUpdate(int tIndex) {
	if (getTransforms().size() > tIndex) {
	    Collidable c = applyContinuousTransform(getTransforms().get(tIndex));
	}
    }

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
