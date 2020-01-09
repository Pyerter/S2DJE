package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Transform;
import sharp.utility.Utility;
import sharp.collision.*;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Group;

public class HingedUnit extends ComplexUnit {

    private CVector initialPosition = new CVector();
    private ArrayList<HingedUnit> childHingedUnits = new ArrayList<>();
    private HingedUnit parentHinge = null;
    private SimpleUnit rootUnit;
    private int hinge;
    private ArrayList<Projection> projections;

    private boolean rigid = false;

    private LinkedList<Transform> queuedTransforms = new LinkedList<>();
    private LinkedList<Force> queuedForces = new LinkedList<>();

    private double rotation = 0;
    
    public HingedUnit(SimpleUnit rootUnit) {
	super(rootUnit.getProjection());
	this.rootUnit = rootUnit;
	initialPosition.set(rootUnit.getProjection().getPivot());
	getGroup().getChildren().add(rootUnit.getNode());
	checkProjections();
    }

    public void setRigid(boolean rigid) {
	this.rigid = rigid;
    }

    public boolean getRigid() {
	return rigid;
    }

    public CVector getInitialPosition() {
	return initialPosition;
    }
    
    public HingedUnit getParentHinge() {
	return parentHinge;
    }

    public HingedUnit getRootParent() {
	if (parentHinge == null) {
	    return this;
	}
	return parentHinge;
    }
    
    public int getHinge() {
	return hinge;
    }

    public ArrayList<Collidable> getCollidables() {
	if (parentHinge == null) {
	    return super.getCollidables();
	}
	return parentHinge.getCollidables();
    }
    
    public void setParentHinge(HingedUnit parentHinge, int hinge) {
	if (parentHinge.getRootUnit().getProjection().getOutline().size() == 0) {
	    App.print(parentHinge.toString() + " is bad hinge unit.");
	}
	this.parentHinge = parentHinge;
	this.hinge = hinge;
	if (parentHinge.getRootUnit().getProjection().getOutline().size() <= hinge) {
	    hinge = parentHinge.getRootUnit().getProjection().getOutline().size() - 1;
	} else if (hinge < 0) {
	    hinge = 0;
	}
	rootUnit.getProjection().getPivot()
	    .set(parentHinge.getRootUnit().getProjection().getOutline().get(hinge));
	App.print("New pivot: " + rootUnit.getProjection().getPivot());
	setGrav(false);
    }    

    public void setRootUnit(SimpleUnit u) {
	if (getChildUnits().size() > 0) {
	    App.print("Warning! HingedUnit losing child units.");
	}
	for (HingedUnit hu: getAllChildHinges()) {
	    if (getChildUnits().contains(hu)) {
		getChildUnits().remove(hu);
	    }
	}
	if (getChildUnits().contains(rootUnit)) {
	    getChildUnits().remove(rootUnit);
	}
	rootUnit = u;
	super.addChildUnit(u);
    }

    public SimpleUnit getRootUnit() {
	return rootUnit;
    }

    public List<HingedUnit> getChildHinges() {
	return childHingedUnits;
    }

    public List<HingedUnit> getAllChildHinges() {
	ArrayList<HingedUnit> allChildren = new ArrayList<>();
	for (HingedUnit childU: childHingedUnits) {
	    allChildren.add(childU);
	    List<HingedUnit> childs = childU.getAllChildHinges();
	    for (HingedUnit u: childs) {
		allChildren.add(u);
	    }
	}
	return allChildren;
    }

    public boolean checkAllCollision(Collidable ... collidables) {
	List<HingedUnit> all = getAllChildHinges();
	for (Collidable c: collidables) {
	    for (HingedUnit u: all) {
		if (Collision.collides(u, c)) {
		    return true;
		}
	    }
	    if (Collision.collides(this, c)) {
		return true;
	    }
	}
	return false;
    }
    
    public boolean checkAllCollision(List<Collidable> collidables) {
	List<HingedUnit> all = getAllChildHinges();
	for (Collidable c: collidables) {
	    for (HingedUnit u: all) {
		if (Collision.collides(u, c)) {
		    return true;
		}
	    }
	    if (Collision.collides(this, c)) {
		return true;
	    }
	}
	return false;
    }

    public void rotate(double rot) {
	Transform t = new Transform(getRootUnit().getProjection().getPivot(), rot);
	addTransform(t);
    }

    public void resetRotation() {
	Transform t = new Transform(getRootUnit().getProjection().getPivot(), -rotation + getRootParent().getRotation());
	addTransform(t);
    }

    public double getRotation() {
	return rotation;
    }

    public void addHingedUnit(HingedUnit hu, int hinge) {
	hu.setParentHinge(this, hinge);
	getGroup().getChildren().add(hu.getNode());
	childHingedUnits.add(hu);
    }

    public void applyTransform(Transform t) {
	for (HingedUnit u: childHingedUnits) {
	    u.applyTransform(t);
	}
        applyUnitTransform(t);
    }

    public void applyUnitTransform(Transform t) {
	t.apply(rootUnit);
	if (t.isRotation()) {
	    rotation += t.getRot();
	}
    }

    public void revertTransform(Transform t) {
	for (HingedUnit u: childHingedUnits) {
	    u.revertTransform(t);
	}
	revertUnitTransform(t);
    }

    public void revertUnitTransform(Transform t) {
	t.revert(rootUnit);
	if (t.isRotation()) {
	    rotation -= t.getRot();
	}
    }

    public Collidable applyFineTransform(Transform t) {
	Collidable c = applyFineTransform(t, getCollidables());
	if(c != null && rigid && t.isTranslation() && parentHinge == null) {
	    if (t.getX() != 0) {
		queuedForces.add(e -> e.getAcceleration().add(new CVector(getVelocity().getX() * -0.33, 0.0)));
	    }
	    if (t.getY() != 0) {
		queuedForces.add(e -> e.getAcceleration().add(new CVector(0.0, getVelocity().getY() * -0.33)));
	    }
	}
	return c;
    }

    public List<Transform> getTransforms() {
	return rootUnit.getTransforms();
    }

    public Collidable applyFineTransform(Transform t, List<Collidable> collidables) {	
	Collidable c = null;
	App.print("Checking fine transform on: " + this);
	applyUnitTransform(t);
	CVector collidePoint = null;
	for (Collidable coll: collidables) {
	    collidePoint = Collision.collides(this, coll, true);
	    if (collidePoint != null) {
		c = coll;
		break;
	    }
	}
	boolean appliedPushback = false;
	boolean alreadyReverted = false;
	if (c != null) {
	    boolean worked = false;
	    if (rigid) {
		if (parentHinge != null && t.isRotation()) {
		    App.print("Cause of push back attempt: " + c);
		    worked = tryHingePushback(c, t, true, collidePoint);
		    appliedPushback = worked;
		}
	    }
	    if (!worked) {
		App.print("Reverting transform on " + this + ": " + t);
		revertUnitTransform(t);
		alreadyReverted = true;
		if (!rigid) {
		    applyReboundOnTransform(t, c);
		}
		return c;
	    }
	}

	boolean reverting = false;
	for (int i = 0; i < childHingedUnits.size(); i++) {
	    if (!reverting) {
		App.print("Applying transform on " + childHingedUnits.get(i) + ": " + t);
		c = childHingedUnits.get(i).applyFineTransform(t, collidables);
		// System.out.println("Applied fine transform on " + childHingedUnits.get(i));
	    } else {
		App.print("Reverting transform on " + childHingedUnits.get(i) + ": " + t);
		childHingedUnits.get(i).revertTransform(t);
		// System.out.println("Reverted transform on " + childHingedUnits.get(i));
	    }
	    if (!reverting && c != null) {
		// System.out.println("Beginning reverting transforms");
		reverting = true;
	    }
	    if (reverting) {
		i -= 2;
		if (i < -1) {
		    break;
		}
	    }
	}
	
	
	if (c != null && !appliedPushback && !alreadyReverted) {
	    revertUnitTransform(t);
	    if (!rigid) {
		applyReboundOnTransform(t, c);
	    }
	}
	return c;
    }

    public boolean applyReboundOnTransform(Transform t, Collidable c) {
	if (c != null && this.getTransforms().contains(t)) {
	    double elastics = c.getElasticity() + this.getElasticity();
	    if (t.isTranslation()) {
		// System.out.println("Old velocity: " + getVelocity());
		getAcceleration().add(CVector.mult(getVelocity(), -elastics));
		// System.out.println("New (undisturbed) velocity: " + CVector.add(getAcceleration(), getVelocity()));
	    }
	    if (t.isRotation()) {
		// System.out.println("Old rot velocity: " + getRotVelocity());
		if (elastics <= 0.5) {
		    elastics = 0.501;
		}
		setRotAcceleration(-getRotVelocity() * elastics * 2);
		// System.out.println("New rot velocity: " + getRotVelocity());
	    }
	    return true;
	}
	return false;
    }

    public boolean tryHingePushback(Collidable c, Transform t, boolean sourceHinge, CVector pivot) {
	if (!t.isRotation() || (sourceHinge && (parentHinge == null))) {
	    App.print("Hinge push-back not valid action on this unit.");
	    return false;
	}
	if (sourceHinge) {
	    App.print("Attempting hinge push-back");
	    revertUnitTransform(t);
	    
	    CVector closePoint = Collision.getClosestPoint(getRootParent(), (t.getRot() > 0),  c);
	    
	    if (checkAllCollision(c)) {
		App.print("Attempted pushback failed on immediate pivot to");
		applyUnitTransform(t);
		multSourceRotation(t, 0.5);
		return false;
	    }
	    Transform pivotalRotation = new Transform(closePoint, t.getRot());
	    if (parentHinge.tryHingePushback(c, pivotalRotation, false, closePoint)) {
	        App.print("Hinge push-back success");
		return true;
	    } else {
		App.print("Hinge push-back failed");
		applyUnitTransform(t);
		multSourceRotation(t, 0.5);
		return false;
	    }
	} else if (parentHinge == null) {
	    CVector acceleration = new CVector(rootUnit.getProjection().getPivot());
	    Transform pivotalRotation = new Transform(pivot, t.getRot());
	    App.print("Applying " + pivotalRotation + " on " + this);
	    applyTransform(t);
	    if (checkAllCollision(getCollidables())) {
		revertTransform(pivotalRotation);
		return false;
	    } else {
		acceleration.set(CVector.subtract(rootUnit.getProjection().getPivot(), acceleration));
		queuedForces.add(e -> e.getAcceleration().add(CVector.mult(acceleration, 0.8)));
		queuedForces.add(e -> e.setRotAcceleration(e.getRotAcceleration() + t.getRot() * 0.8));
		return true;
	    }
	}
	return parentHinge.tryHingePushback(c, t, false, pivot);
    }

    public void multSourceRotation(Transform t, double amount) {
	List<HingedUnit> allHinges = getAllChildHinges();
	allHinges.add(this);
	for (HingedUnit u: allHinges) {
	    if (u.getTransforms().contains(t)) {
		u.setRotVelocity(u.getRotVelocity() * amount);
		break;
	    }
	}
    }

    public void update() {
	for (HingedUnit u: childHingedUnits) {
	    u.update();
	}
	App.print("Position: " + rootUnit.getProjection().getPivot());
	updateHinge();
    }

    public void updateHinge() {
	App.print("\nUpdating: " + this);
	checkUnitChildren();
	
	if (parentHinge == null && getGrav()) {
	    Unit.GRAVITY.apply(this);
	}

	getVelocity().add(getAcceleration());
	getAcceleration().mult(0.0);

	setRotVelocity(getRotVelocity() + getRotAcceleration(), Math.PI / 12);
	setRotAcceleration(0.0);
	
	rootUnit.addTransform(new Transform(getVelocity().getX(), 0.0));
	rootUnit.addTransform(new Transform(0.0, getVelocity().getY()));
	rootUnit.addTransform(new Transform(rootUnit.getProjection().getPivot(), getRotVelocity()));

	if (getCollidables() != null && getCollidables().size() > 0) {
	    App.print("There are collidables for hinged unit");
	    boolean willFineUpdate = fineUpdate(discreteUpdate());
	    if (willFineUpdate) {
		App.print("Adding it to fine updates");
		List<HingedUnit> allHinges = getRootParent().getAllChildHinges();
		for (int i = 0; i < allHinges.size(); i++) {
		    App.print(allHinges.get(i).toString());
		    Collision.addFineColliders(allHinges.get(i));
		}
		Collision.addFineColliders(getRootParent());
	    } else if (!Collision.willFineUpdate(this) && parentHinge == null) {
		App.print("Hinged unit ending updates of rootUnit and children");
		endUpdate();
	    }
	} else if (!getHasTransformed()) {
	    for (Transform t: getTransforms()) {
		this.applyTransform(t);
	    }
	    setHasTransformed(true);
	    endUpdate();
	}
	
	for (Unit u: getChildUnits()) {
	    u.update();
	}

	App.print("Ending update of " + this + "\n");
    }

    public List<Collidable> getCollisions() {
	LinkedList<Collidable> collidedWith = new LinkedList<>();
	for (Collidable c: getCollidables()) {
	    if (checkAllCollision(c)) {
		collidedWith.add(c);
	    }
	}
	return collidedWith;
    }

    public void endUpdate() {
	for (HingedUnit u: childHingedUnits) {
	    u.endUpdate();
	}
	rootUnit.endUpdate();
	App.print("Clearing hinged unit transforms");
	getTransforms().clear();
	setHasTransformed(true);
	initialPosition.set(rootUnit.getProjection().getPivot());
	if (parentHinge != null) {
	    getVelocity().mult(0);
	}
	for (Transform t: queuedTransforms) {
	    addTransform(t);
	}
	queuedTransforms.clear();
	for (Force f: queuedForces) {
	    f.apply(this);
	}
	queuedForces.clear();
    }

    public void addTransform(Transform t) {
	if (parentHinge == null || t.isRotation() || t.isTranslation()) {
	    rootUnit.addTransform(t);
	}
    }

    public List<Transform> getQueuedTransforms() {
	return queuedTransforms;
    }

    public List<Force> getQueuedForces() {
	return queuedForces;
    }

    public String toString() {
	return "Hinged Unit: RootUnit[" + rootUnit + "], sub-units(" + getChildUnits().size() +
	    "), Priority(" + getPriority() + "), Hinged-Children(" + childHingedUnits.size() + ")";
    }
    
}
