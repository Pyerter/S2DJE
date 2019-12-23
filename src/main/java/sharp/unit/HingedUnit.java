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

public class HingedUnit extends ComplexUnit {


    private CVector initialPosition = new CVector();
    private ArrayList<HingedUnit> childHingedUnits = new ArrayList<>();
    private HingedUnit parentHinge = null;
    private SimpleUnit rootUnit;
    private int hinge;
    private ArrayList<Projection> projections;
    
    public HingedUnit(SimpleUnit rootUnit) {
	super(rootUnit.getProjection());
	this.rootUnit = rootUnit;
	initialPosition.set(rootUnit.getProjection().getPivot());
	System.out.println(initialPosition);
	getGroup().getChildren().add(rootUnit.getNode());
	checkProjections();
    }

    /*public Projection[] getCollider() {
	return new Projection[] { rootUnit.getProjection() };
	}*/

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
	    System.out.println(parentHinge.toString() + " is bad hinge unit.");
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

    /*public Projection getProjection() {
	return rootUnit.getProjection();
	}*/

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
    }

    public void revertTransform(Transform t) {
	for (HingedUnit u: childHingedUnits) {
	    u.revertTransform(t);
	}
	revertUnitTransform(t);
    }

    public void revertUnitTransform(Transform t) {
	t.revert(rootUnit);
    }

    public Collidable applyFineTransform(Transform t) {
	return applyFineTransform(t, getCollidables());
    }

    public List<Transform> getTransforms() {
	return rootUnit.getTransforms();
    }

    public Collidable applyFineTransform(Transform t, List<Collidable> collidables) {
	boolean reverting = false;
	Collidable c = null;
	App.print("Checking fine transform on: " + this);
	applyUnitTransform(t);
	for (Collidable coll: collidables) {
	    CVector collidePoint = Collision.collides(this, coll, true);
	    if (collidePoint != null) {
		/*if (tryHingePushback(coll, t, true, collidePoint)) {
		    continue;
		    }*/
		c = coll;
		break;
	    }
	}
	if (c != null) {
	    App.print("Reverting transform on " + this + ": " + t);
	    revertUnitTransform(t);
	    return c;
	}
	for (int i = 0; i < childHingedUnits.size(); i++) {
	    if (!reverting) {
		App.print("Applying transform on " + childHingedUnits.get(i) + ": " + t);
		c = childHingedUnits.get(i).applyFineTransform(t, collidables);
	    } else {
		App.print("Reverting transform on " + childHingedUnits.get(i) + ": " + t);
		// childHingedUnits.get(i).revertTransform(t);
	    }
	    if (c != null) {
		reverting = true;
	    }
	    if (reverting) {
		i -= 2;
		if (i < -1) {
		    return c;
		}
	    }
	}
	if (c != null) {
	    revertUnitTransform(t);
	}
	return c;
    }

    public boolean tryHingePushback(Collidable c, Transform t, boolean sourceHinge, CVector pivot) {
	if (!t.isRotation() || (sourceHinge && parentHinge == null)) {
	    return false;
	}
	if (sourceHinge) {
	    App.print("Attempting hinge push-back");
	    revertUnitTransform(t);
	    CVector revertedPoint = new CVector(pivot);
	    revertedPoint.revertTransform(t);
	    CVector pivotPoint = new CVector(pivot);
	    revertedPoint.subtract(rootUnit.getProjection().getPivot());
	    pivotPoint.subtract(rootUnit.getProjection().getPivot());
	    double revertedHeading = revertedPoint.heading();
	    double diffAngle = pivotPoint.heading() - revertedHeading
		- (Utility.sign(revertedHeading) * (Double.MIN_NORMAL));
	    Transform tempRotation = new Transform(pivot, diffAngle);
	    applyUnitTransform(tempRotation);
	    if (Collision.collides(this, c)) {
		revertUnitTransform(tempRotation);
		return false;
	    }
	    Transform remainingRotation = new Transform(pivot, t.getRot() - diffAngle);
	    if (parentHinge.tryHingePushback(c, remainingRotation, false, pivot)) {
		App.print("Hinge push-back success");
		return true;
	    } else {
		App.print("Hinge push-back failed");
		revertUnitTransform(tempRotation);
		return false;
	    }
	} else if (parentHinge == null) {
	    Transform pivotalRotation = new Transform(pivot, -t.getRot());
	    applyUnitTransform(pivotalRotation);
	    if (checkAllCollision(getCollidables())) {
		revertUnitTransform(pivotalRotation);
		return false;
	    } else {
		return true;
	    }
	}
	return parentHinge.tryHingePushback(c, t, false, pivot);
    }

    public void update() {
	for (HingedUnit u: childHingedUnits) {
	    u.update();
	}
	// System.out.println("Updating: " + this);
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

	setRotVelocity(getRotVelocity() + getRotAcceleration());
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
	    } else if (!Collision.willFineUpdate(this)) {
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
	// rootUnit.update();

	App.print("Ending update of " + this + "\n");
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
    }

    public void addTransform(Transform t) {
	if (parentHinge == null || t.isRotation()) {
	    rootUnit.addTransform(t);
	}
    }

    

    public String toString() {
	return "Hinged Unit: RootUnit[" + rootUnit + "], sub-units(" + getChildUnits().size() +
	    "), Priority(" + getPriority() + "), Hinged-Children(" + childHingedUnits.size() + ")";
    }
    
}
