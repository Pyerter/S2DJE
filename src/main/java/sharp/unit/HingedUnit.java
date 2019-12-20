package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Transform;
import sharp.utility.Utility;
import sharp.collision.*;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class HingedUnit extends ComplexUnit {


    private CVector initialPosition = new CVector();
    private ArrayList<HingedUnit> childHingedUnits = new ArrayList<>();
    private HingedUnit parentHinge = null;
    private SimpleUnit rootUnit;
    private int hinge;
    
    public HingedUnit(SimpleUnit rootUnit) {
	super(rootUnit.getProjection());
	this.rootUnit = rootUnit;
	initialPosition.set(rootUnit.getProjection().getPivot());
	getGroup().getChildren().add(rootUnit.getNode());
    }

    public CVector getInitialPosition() {
	return initialPosition;
    }
    
    public HingedUnit getParentHinge() {
	return parentHinge;
    }
    
    public int getHinge() {
	return hinge;
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
    }    

    public void setRootUnit(SimpleUnit u) {
	if (getChildUnits().size() > 0) {
	    System.out.println("Warning! HingedUnit losing child units.");
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
    }

    public void applyTransform(Transform t) {
	for (HingedUnit u: childHingedUnits) {
	    u.applyTransform(t);
	}
	super.applyTransform(t);
    }

    public void revertTransform(Transform t) {
	for (HingedUnit u: childHingedUnits) {
	    u.revertTransform(t);
	}
	super.revertTransform(t);
    }

    public Collidable applyFineTransform(Transform t) {
	return applyFineTransform(t, getCollidables());
    }

    public Collidable applyFineTransform(Transform t, List<Collidable> collidables) {
	boolean reverting = false;
	Collidable c = null;
	for (int i = 0; i < childHingedUnits.size(); i++) {
	    if (!reverting) {
		c = childHingedUnits.get(i).applyFineTransform(t, collidables);
	    } else {
		childHingedUnits.get(i).revertTransform(t);
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
	super.applyTransform(t);
	for (Collidable coll: collidables) {
	    CVector collidePoint = Collision.collides(this, coll, true);
	    if (collidePoint != null) {
		if (tryHingePushback(coll, t, true, collidePoint)) {
		    continue;
		}
		c = coll;
		break;
	    }
	}
	if (c != null) {
	    super.revertTransform(t);
	}
	return c;
    }

    public boolean tryHingePushback(Collidable c, Transform t, boolean sourceHinge, CVector pivot) {
	if (!t.isRotation() || (sourceHinge && parentHinge == null)) {
	    return false;
	}
	if (sourceHinge) {
	    super.revertTransform(t);
	    CVector revertedPoint = new CVector(pivot);
	    revertedPoint.revertTransform(t);
	    CVector pivotPoint = new CVector(pivot);
	    revertedPoint.subtract(rootUnit.getProjection().getPivot());
	    pivotPoint.subtract(rootUnit.getProjection().getPivot());
	    double revertedHeading = revertedPoint.heading();
	    double diffAngle = pivotPoint.heading() - revertedHeading
		- (Utility.sign(revertedHeading) * (Double.MIN_NORMAL));
	    Transform tempRotation = new Transform(pivot, diffAngle);
	    super.applyTransform(tempRotation);
	    if (Collision.collides(this, c)) {
		super.revertTransform(tempRotation);
	    }
	    Transform remainingRotation = new Transform(pivot, t.getRot() - diffAngle);
	    if (parentHinge.tryHingePushback(c, remainingRotation, false, pivot)) {
		return false;
	    } else {
		super.revertTransform(tempRotation);
		return true;
	    }
	} else if (parentHinge == null) {
	    Transform pivotalRotation = new Transform(pivot, -t.getRot());
	    this.applyTransform(pivotalRotation);
	    if (checkAllCollision(getCollidables())) {
		this.revertTransform(pivotalRotation);
		return false;
	    } else {
		return true;
	    }
	}
	return parentHinge.tryHingePushback(c, t, false, pivot);
    }

    public void update() {
	for (HingedUnit u: childHingedUnits) {
	    System.out.println("Updating sub-hinged unit:");
	    u.update();
	}
	super.update();
    }

    public void endUpdate() {
	for (HingedUnit u: childHingedUnits) {
	    u.endUpdate();
	}
	super.endUpdate();
	initialPosition.set(rootUnit.getProjection().getPivot());
    }

    public void addTransform(Transform t) {
	if (t.isRotation()) {
	    super.addTransform(t);
	}
    }
    
}
