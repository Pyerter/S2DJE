package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Transform;
import sharp.utility.Utility;
import sharp.collision.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ComplexUnit implements Unit, Collidable {

    private Group unitGroup = new Group();
    private Projection rootProjection = new Projection();
    private Projection[] projections;
    private ArrayList<Unit> childUnits = new ArrayList<>();;
    private CVector previousPosition = new CVector();
    private CVector velocity = new CVector();
    private CVector acceleration = new CVector();
    private Double rotVelocity = 0.0;
    private Double rotAcceleration = 0.0;
    private boolean grav = true;
    private ArrayList<Collidable> collidables = new ArrayList<>();
    private int priority;
    private boolean show = true;
    
    public ComplexUnit(CVector position, SimpleUnit ... units) {
	this(new Projection(), position, units);
    }

    protected ComplexUnit(Projection rootProjection, CVector position, SimpleUnit ... units) {
	this.rootProjection = rootProjection;
	for (SimpleUnit u: units) {
	    u.setGrav(false);
	    childUnits.add(u);
	}
	rootProjection.getPivot().addConnections(units);
	rootProjection.getPivot().set(position);
	Collision.setPriority(this);
	previousPosition.set(position);
	checkProjections();
    }

    protected ComplexUnit(Projection rootProjection, SimpleUnit ... units) {
	this.rootProjection = rootProjection;
	for (SimpleUnit u: units) {
	    childUnits.add(u);
	    rootProjection.getPivot().addConnections(u);
	}
	Collision.setPriority(this);
	previousPosition.set(rootProjection.getPivot());
    }

    public Group getGroup() {
	return unitGroup;
    }

    public List<Unit> getChildUnits() {
	return childUnits;
    }

    public boolean getGrav() {
	return grav;
    }

    public void setGrav(boolean grav) {
	this.grav = grav;
    }

    public void setPreviousPosition(CVector previousPosition) {
	this.previousPosition.set(previousPosition);
    }

    public CVector getPreviousPosition() {
	return previousPosition;
    }
    
    public Node getNode() {
	return unitGroup;
    }

    public void setX(double x) {
	rootProjection.getPivot().setX(x);
    }

    public void setY(double y) {
	rootProjection.getPivot().setY(y);
    }

    public double getX() {
	return rootProjection.getPivot().getX();
    }

    public double getY() {
	return rootProjection.getPivot().getY();
    }

    public void rotate(double rot) {
	rootProjection.getPivot().rotateAnchor(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	rootProjection.rotateAround(pivot, rot);
    }

    public boolean canLocallyRotate() {
	return true;
    }

    public List<Transform> getTransforms() {
	return rootProjection.getTransforms();
    }

    public void addTransform(Transform t) {
	rootProjection.addTransform(t);
    }

    public void applyTransform(Transform t) {
	rootProjection.applyTransform(t);
    }

    public void revertTransform(Transform t) {
	rootProjection.revertTransform(t);
    }

    public boolean getHasTransformed() {
	return rootProjection.getHasTransformed();
    }

    public void setHasTransformed(boolean hasTransformed) {
	rootProjection.setHasTransformed(hasTransformed);
    }
    
    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public int getPriority() {
	return priority;
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }

    public Projection[] getCollider() {
	return projections;
    }

    public Projection getProjection() {
	return rootProjection;
    }
    
    public CVector getVelocity() {
	return velocity;
    }

    public CVector getAcceleration() {
	return acceleration;
    }

    public Double getRotVelocity() {
	return rotVelocity;
    }
    
    public Double getRotAcceleration() {
	return rotAcceleration;
    }

    public void setRotVelocity(double rotVelocity, double max) {
	if (Math.abs(rotVelocity) > max) {
	    rotVelocity = max * Utility.sign(rotVelocity);
	}
	this.rotVelocity = rotVelocity;
    }
    
    public void setRotVelocity(double rotVelocity) {
	setRotVelocity(rotVelocity, Unit.MAX_SPIN);
    }

    public void setRotAcceleration(double rotAcceleration) {
	this.rotAcceleration = rotAcceleration;
    }

    public void checkUnitChildren() {
	boolean projectionCheck = false;
	for (Unit u: childUnits) {
	    if (checkUnitChild(u)) {
		projectionCheck = true;
	    }
	}
	if (projectionCheck) {
	    checkProjections();
	}
    }

    public boolean checkUnitChild(Unit u) {
	boolean needsCheck = unitGroup.getChildren().contains(u.getNode()) != u.getShow();
	/*
	if (!unitGroup.getChildren().contains(u.getNode()) && u.getShow()) {
	    unitGroup.getChildren().add(u.getNode());
	} else if (unitGroup.getChildren().contains(u.getNode()) && !u.getShow()) {
	    
	}*/
	if (needsCheck) {
	    if (u.getShow()) {
		unitGroup.getChildren().add(u.getNode());
	    } else {
		unitGroup.getChildren().remove(u.getNode());
	    }
	}
	if (!rootProjection.getPivot().getConnections().contains(u)) {
	    rootProjection.getPivot().addConnections(u);
	}
	return needsCheck;
    }

    public void checkProjections() {
	int properLength = childUnits.size();
	if (projections != null && projections.length == properLength) {
	    return;
	}
	projections = childUnits.stream()
	    .filter(e -> e.getShow())
	    .map(e -> e.getCollider())
	    .reduce(new Projection[0],
		    (Projection[] a, Projection[] b) -> {
		    Projection[] reduction = Arrays.copyOf(a, a.length + b.length);
		    for (int i = 0; i < b.length; i++) {
			reduction[a.length + i] = b[i];
		    }
		    return reduction;
		});
	projections = Arrays.copyOf(projections, projections.length + 1);
	projections[projections.length - 1] = rootProjection;
    }

    public void addChildUnit(Unit u) {
	if (!childUnits.contains(u)) {
	    u.setGrav(false);
	    childUnits.add(u);
	    checkUnitChild(u);
	    checkProjections();
	}
    }

    public void update() {
	App.print("\nUpdating: " + this);
	checkUnitChildren();
	
	if (grav) {
	    Unit.GRAVITY.apply(this);
	}

	velocity.add(acceleration);
	acceleration.mult(0.0);

	setRotVelocity(rotVelocity + rotAcceleration);
	rotAcceleration = 0.0;

	rootProjection.addTransform(new Transform(velocity.getX(), 0.0));
	rootProjection.addTransform(new Transform(0.0, velocity.getY()));
	rootProjection.addTransform(new Transform(rootProjection.getPivot(), rotVelocity));

	if (collidables != null && collidables.size() > 0) {
	    boolean doneUpdating = !Unit.super.fineUpdate(Unit.super.discreteUpdate());
	    if (doneUpdating) {
		Unit.super.endUpdate();
	    }
	} else if (!getHasTransformed()) {
	    for (Transform t: getTransforms()) {
		this.applyTransform(t);
	    }
	    setHasTransformed(true);
	}
	
	for (Unit u: childUnits) {
	    u.update();
	}

	App.print("Ending update of " + this + "\n");
    }

    public Collidable applyFineTransform(Transform t) {
	Collidable c = Unit.super.applyFineTransform(t);
	App.print("Fine transform collected collision with: " + c);
	if (c != null) {
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
	}
	return null;
    }

    public void endUpdate() {
	Unit.super.endUpdate();
    }

    public String toString() {
	return "Complex Unit: sub-units(" + getChildUnits().size() + "), Priority(" + getPriority() + ")";
    }

    public boolean getShow() {
	return show;
    }

    public void setShow(boolean show) {
	this.show = show;
    }

}
