package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.Updatable;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;

public class Projection implements Updatable, Translatable {

    private Anchor pivot;
    private Double rotation = new Double(0.0);
    private ArrayList<CVector> outline = new ArrayList<>();
    private Double collisionRadius = 0.0;
    
    public Projector() {
	pivot = new Anchor(App.HALF_WIDTH, App.HALF_HEIGHT);
	outline = new ArrayList<>();
    }

    public Projector(CVector pivot, ArrayList<CVector> outline) {
	this.pivot = pivot;
	this.outline = new ArrayList<CVector>();
	setOutline(outline);
    }

    public CVector getPivot() {
	return pivot;
    }

    public Double getRotation() {
	return rotation;
    }

    public void setOutline(List<CVector> outline) {
	for (CVector v: this.outline) {
	    if (pivot.getConnections().contains(v)) {
		pivot.getConnections().remove(v);
	    }
	}
	this.outline.clear();
	collisionRadius = 0.0;
	for (CVector v: outline) {
	    CVector newV = new CVector(v);
	    this.outline.add(newV);
	    if (v.getMag() > collisionRadius) {
		collisionRadius = v.getMag();
	    }
	    pivot.addConnections(newV);
	}
    }

    public Double getCollisionRadius() {
	return collisionRadius;
    }

    public void move(CVector movement) {
	pivot.add(movement);
    }
    
    public void rotate(double rot) {
	position.rotateAnchor(rot);
	rotation += rot;
    }

    public void update() {
	/* if (getPoints().size() * 2 != outline.size()) {
	    System.out.println(this.toString() + ": points do not match outline.");
	    return;
	}
	for (int i = 0; i < outline.size(); i++) {
	    this.getPoints().set(i * 2, outline.get(i).getX());
	    this.getPoints().set(i * 2 + 1, outline.get(i).getY());
	    if (outline.get(i).getMag() > collisionRadius) {
		collisionRadius = outline.get(i).getMag();
	    }
	    }*/
    }

    /*public void update(CVector movement, double rot) {
	move(movement);
	rotate(rot);
	update();
	}*/

    /*public void update(CVector movement, double rot, List<Polygon> polies, List<ImageView> images) {
	update(movement, rot);
	if (polies != null) {
	    projectPolies(movement, rot, polies);
	}
	if (images != null) {
	    projectImages(movement, rot, images);
	}
	}*/

    /*public void projectPolies(CVector movement, double rot, List<Polygon> polies) {
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
	}*/
    
}
