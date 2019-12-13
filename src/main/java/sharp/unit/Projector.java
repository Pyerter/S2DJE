package sharp.unit;

import sharp.game;
import sharp.utility;
import javafx.scene.shape.Polygon;

public class Projector extends Polygon implements Updatable {

    private CVector position;
    private ArrayList<CVector> outline = new ArrayList<>();
    private Double collisionRadius = 0.0;
    
    public Projector() {
	position = new CVector(halfScalar.getX() * Game.WIDTH,
			       halfScalar.getY() * Game.HEIGHT);
    }

    public Projector(CVector position) {
	this.position = new CVector(position);
    }

    public Projector(Polygon poly) {
	this();
	this.setPoints(poly.getPoints());
	update();
    }

    public Projector(CVector position, Polygon poly) {
	this.position = new CVector(position);
	this.setPoints(poly.getPoints());
	update();
    }

    public void setOutline(List<CVector> outline) {
	this.outline.clear();
	collisionRadius = 0.0;
	for (CVector v: outline) {
	    this.outline.add(new CVector(v));
	    if (v.getMag() > collisionRadius) {
		collisionRadius = v.getMag();
	    }
	    this.getPoints().addAll(0.0, 0.0);
	}
    }

    public Double getCollisionRadius() {
	return collisionRadius;
    }

    public void move(CVector movement) {
	position.add(movement);
    }
    
    public void rotate(double rot) {
	for (CVector v: outline) {
	    v.rotateAround(position, rot);
	}
    }

    public void update() {
	if (getPoints().size() * 2 != outline.size()) {
	    System.out.println(this.toString() + ": points do not match outline.");
	    return;
	}
	for (int i = 0; i < outline.size(); i++) {
	    this.getPoints().set(i * 2, outline.get(i).getX());
	    this.getPoints().set(i * 2 + 1, outline.get(i).getY());
	}
    }

    public void update(CVector movement, double rot) {
	move(movement);
	rotate(rot);
	update();
    }
    
}
