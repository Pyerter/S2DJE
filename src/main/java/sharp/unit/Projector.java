package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Updatable;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;

public class Projector extends Polygon implements Updatable {

    private CVector position;
    private Double rotation = new Double(0.0);
    private ArrayList<CVector> outline = new ArrayList<>();
    private Double collisionRadius = 0.0;
    
    public Projector() {
	super();
	position = new CVector(App.HALF_WIDTH, App.HALF_HEIGHT);
    }

    public Projector(CVector position) {
	super();
	this.position = new CVector(position);
    }

    public Projector(ArrayList<CVector> outline) {
	this();
	this.outline = outline;
	for (CVector v: outline) {
	    this.getPoints().addAll(v.getX(), v.getY());
	}
	update();
    }

    public Projector(CVector position, ArrayList<CVector> outline) {
	this(outline);
	this.position = new CVector(position);
	update();
    }

    public CVector getPosition() {
	return position;
    }

    public Double getRotation() {
	return rotation;
    }

    public void setOutline(List<CVector> outline) {
	this.outline.clear();
	this.getPoints().clear();
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
	rotation += rot;
    }

    public void update() {
	if (getPoints().size() * 2 != outline.size()) {
	    System.out.println(this.toString() + ": points do not match outline.");
	    return;
	}
	for (int i = 0; i < outline.size(); i++) {
	    this.getPoints().set(i * 2, outline.get(i).getX());
	    this.getPoints().set(i * 2 + 1, outline.get(i).getY());
	    if (outline.get(i).getMag() > collisionRadius) {
		collisionRadius = outline.get(i).getMag();
	    }
	}
    }

    public void update(CVector movement, double rot) {
	move(movement);
	rotate(rot);
	update();
    }

    public void update(CVector movement, double rot, List<Polygon> polies, List<ImageView> images) {
	update(movement, rot);
	if (polies != null) {
	    projectPolies(movement, rot, polies);
	}
	if (images != null) {
	    projectImages(movement, rot, images);
	}
    }

    public void projectPolies(CVector movement, double rot, List<Polygon> polies) {
	for (Polygon p: polies) {
	    for (int i = 0; i < p.getPoints().size(); i += 2) {
		CVector point = new CVector(p.getPoints().get(i), p.getPoints().get(i + 1));
		point.rotate(rot);
		point.add(movement);
		p.getPoints().set(i, point.getX());
		p.getPoints().set(i + 1, point.getY());
	    }
	}
    }

    public void projectImages(CVector movement, double rot, List<ImageView> images) {
	for (ImageView iv: images) {
	    iv.setX(iv.getX() + movement.getX());
	    iv.setY(iv.getY() + movement.getY());
	    iv.setRotate(iv.getRotate() + rot);
	}
    }
    
}
