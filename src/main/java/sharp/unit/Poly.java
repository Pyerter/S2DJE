package sharp.unit;

import sharp.utility.Transform;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.collision.Collidable;
import sharp.collision.Projection;
import sharp.collision.Collision;

import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.shape.Polygon;

public class Poly extends Projection implements Collidable {

    private Polygon p;
    private LinkedList<Transform> transforms;
    private boolean hasTransformed;
    private ArrayList<Collidable> collidables;
    private int priority;
    
    public Poly(Anchor pivot, CVector ... points) {
	super(pivot, points);
	p = new Polygon();
	pivot.addConnections(this);
	Collision.setPriority(this);
    }
    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public Projection getCollider() {
	return this;
    }

    public void addCollidables(Collidable ... c) {
	if (collidables == null) {
	    collidables = new ArrayList<>();
	}
	for (Collidable coll: c) {
	    if (!collidables.contains(coll)) {
		collidables.add(coll);
	    }
	}
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }

    public int getPriority() {
	return priority;
    }

    public void update() {
	boolean doneMoving = !fineUpdate(discreteUpdate());
	if (doneMoving) {
	    endUpdate();
	}
    }

    public void endUpdate() {
	adjustPoly(true);
    }

    public Polygon getPolygon() {
	return p;
    }
    
    public void correctPoly() {
	while (p.getPoints().size() != getOutline().size() * 2) {
	    if (p.getPoints().size() < getOutline().size() * 2) {
		p.getPoints().add(0.0);
	    } else if (p.getPoints().size() > getOutline().size() * 2) {
		p.getPoints().remove(p.getPoints().size() - 1);
	    }
	}
    }

    public void adjustPoly() {
	for (int i = 0; i < p.getPoints().size(); i += 2) {
	    CVector point = getOutline().get(i / 2);
	    p.getPoints().set(i, point.getX());
	    p.getPoints().set(i + 1, point.getY());
	}
    }

    public void adjustPoly(boolean checkSize) {
	if (checkSize) {
	    correctPoly();
	}
	adjustPoly();
    }
    
}
