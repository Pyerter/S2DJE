package sharp.collision;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.Transform;
import sharp.utility.Translatable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;

public class Projection implements Translatable {

    private LinkedList<Transform> transforms = new LinkedList<>();
    private boolean hasTransformed;
    private Anchor pivot;
    private Double rotation = new Double(0.0);
    private ArrayList<CVector> outline = new ArrayList<>();
    private Double collisionRadius = 0.0;
    
    public Projection() {
	pivot = new Anchor(App.HALF_WIDTH, App.HALF_HEIGHT);
	outline = new ArrayList<>();
    }

    public Projection(Anchor pivot, ArrayList<CVector> outline) {
	this.pivot = pivot;
	this.outline = new ArrayList<CVector>();
	setOutline(outline);
    }

    public Projection(Anchor pivot, CVector ... outline) {
	this.pivot = pivot;
	this.outline = new ArrayList<CVector>();
	setOutline(outline);
    }
    
    public Anchor getPivot() {
	return pivot;
    }

    public double getX() {
	return pivot.getX();
    }

    public double getY() {
	return pivot.getY();
    }

    public void setX(double x) {
	pivot.setX(x);
    }

    public void setY(double y) {
	pivot.setY(y);
    }

    public void rotate(double rot) {
	pivot.rotateAnchor(rot);
	rotation += rot;
    }

    public void rotateAround(CVector pivot, double rot) {
	this.pivot.rotateAround(pivot, rot);
	rotation += rot;
    }

    public boolean canLocallyRotate() {
	return true;
    }

    public LinkedList<Transform> getTransforms() {
	return transforms;
    }

    public void addTransform(Transform t) {
	transforms.add(t);
    }

    public boolean getHasTransformed() {
	return hasTransformed;
    }

    public void setHasTransformed(boolean hasTransformed) {
	this.hasTransformed = hasTransformed;
    }

    public Double getRotation() {
	return rotation;
    }

    public void setOutline(CVector ... outline) {
	LinkedList<CVector> newOutline = new LinkedList<>();
	for (CVector v: outline) {
	    newOutline.add(v);
	}
	setOutline(newOutline);
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

    protected List<CVector> getOutline() {
	return outline;
    }

    public Double getCollisionRadius() {
	return collisionRadius;
    }

    public void update() {
	if (hasTransformed) {
	    return;
	}
	for (Transform t: transforms) {
	    applyTransform(t);
	}
	hasTransformed = true;
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
