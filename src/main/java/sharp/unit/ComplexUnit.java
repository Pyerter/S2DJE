package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Transform;
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

    protected Group getGroup() {
	return unitGroup;
    }

    protected List<Unit> getChildUnits() {
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

    public void setRotVelocity(double rotVelocity) {
	this.rotVelocity = rotVelocity;
    }

    public void setRotAcceleration(double rotAcceleration) {
	this.rotAcceleration = rotAcceleration;
    }

    public void checkUnitChildren() {
	for (Unit u: childUnits) {
	    checkUnitChild(u);
	}
    }

    public void checkUnitChild(Unit u) {
	if (!unitGroup.getChildren().contains(u.getNode())) {
	    unitGroup.getChildren().add(u.getNode());
	}
	if (!rootProjection.getPivot().getConnections().contains(u)) {
	    rootProjection.getPivot().addConnections(u);
	}
    }

    public void checkProjections() {
	int properLength = childUnits.size();
	if (projections != null && projections.length == properLength) {
	    return;
	}
	projections = childUnits.stream()
	    .map(e -> e.getCollider())
	    .reduce(new Projection[0],
		    (Projection[] a, Projection[] b) -> {
		    Projection[] reduction = Arrays.copyOf(a, a.length + b.length);
		    for (int i = a.length; i < reduction.length; i++) {
			reduction[i] = b[reduction.length - a.length];
		    }
		    return reduction;
		});
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
	checkUnitChildren();
	
	if (grav) {
	    Unit.GRAVITY.apply(this);
	}

	velocity.add(acceleration);
	acceleration.mult(0.0);

	rotVelocity += rotAcceleration;
	rotAcceleration = 0.0;

	rootProjection.addTransform(new Transform(velocity.getX(), velocity.getY()));
	rootProjection.addTransform(new Transform(rootProjection.getPivot(), rotVelocity));

	boolean doneUpdating = !Unit.super.fineUpdate(Unit.super.discreteUpdate());
	if (doneUpdating) {
	    Unit.super.endUpdate();
	}

	for (Unit u: childUnits) {
	    u.update();
	}
    }
    

}
