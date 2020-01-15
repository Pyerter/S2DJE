package sharp.unit;

public class LinkedUnit <T extends Projection> extends Unit<T> {

    private ArrayList<LinkedUnit> childLinks = new ArrayList<>();
    private LinkedUnit parentUnit;

    private WrappedValue<Double> rigidness = 1.0;
    
    public LinkedUnit() {
	
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

    public List<LinkedUnit> getAllSubUnits() {
	LinkedList<LinkedUnit> units = new LinkedList<>();
	for (LinkedUnit u: childLinks) {
	    units.add(u);
	    for (LinkedUnit subs: u.getAllSubUnits()) {
		units.add(subs);
	    }
	}
	return units;
    }
    
    public ArrayList<Collidable> getCollidables() {
	if (parentUnit != null) {
	    return getRootParentUnit().getCollidables();
	    
	}
	return super.getCollidables();
    }

    public void applyTransform(Transform t) {
	t.apply(this);
	for (LinkedUnit u: childLinks) {
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

    public Collidable applyContinuousTransform(Transform t) {
	boolean revert = false;
	for (int i = 0; i < childLinks.size(); i++) {
	    if (!revert) {
		Collidable c = childLinks.get(i).applyContinuousTransform(t);
		if (c != null) {
		    revert = true;
		    i -= 2;
		    if (i < 1) {
			break;
		    }
		}
	    } else {
		childLinks.get(i).revertTransform(t);
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

	Transform pivotRotation = new Transform(closePoint, t.getRot() * ridigness.getValue());
	LinkedUnit rootParent = getRootParentUnit();
	CVector acceleration = new CVector(rootUnit.getProjection().getPivot());
	rootParent.applyTransform(pivotRotation);
	boolean revert = false;
	for (Collidable coll: rootParent.getCollidables()) {
	    if (Collision.collides(rootParent, coll)) {
		revert = true;
		break;
	    }
	    for (LinkedUnit u: rootParent.getAllSubUnits()) {
		if (Collision.collides(u, coll)) {
		    return = true;
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
	    // apply this as a queued force for acceleration in x/y and rot
	    return true;
	}
    }

    public void applyRebound(Collidable c, Transform t, double multiplier) {
	
    }
    
}
