package sharp.unit;

import sharp.utility.Transform;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.collision.Collidable;
import sharp.collision.Projection;
import sharp.collision.Collision;

import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Img implements Collidable {

    private ImageView iv;
    private LinkedList<Transform> transforms;
    private boolean hasTransformed;
    private ArrayList<Collidable> collidables;
    private Projection projection;
    private int priority;
    
    public Img(String image, double x, double y, double width, double height) {
	iv = new ImageView(new Image(image));
	Anchor pivot = new Anchor(x, y);
	iv.setFitWidth(width);
	iv.setFitHeight(height);
	projection = new Projection(pivot,
				    new CVector(x - (width / 2), y - (height / 2)),
				    new CVector(x + (width / 2), y - (height / 2)),
				    new CVector(x + (width / 2), y + (height / 2)),
				    new CVector(x - (width / 2), y + (height / 2)));
	iv.setX(x - (width / 2));
	iv.setY(y - (height / 2));
	pivot.addConnections(this);
	Collision.setPriority(this);
    }

    public void setX(double x) {
	iv.setX(x - (iv.getFitWidth() / 2));
    }

    public void setY(double y) {
	iv.setY(y - (iv.getFitHeight() / 2));
    }

    public double getX() {
	return iv.getX() + (iv.getFitWidth() / 2);
    }

    public double getY() {
	return iv.getY() + (iv.getFitHeight() / 2);
    }

    public void rotate(double rot) {
	iv.setRotate(iv.getRotate() + rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	CVector diff = CVector.subtract(projection.getPivot(), pivot);
	double angle = diff.heading();
	diff.rotate(rot);
	CVector end = CVector.add(diff, pivot);
	this.setX(end.getX());
	this.setY(end.getY());
	this.rotate(rot);
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

    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public Projection getCollider() {
	return projection;
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
    }

    public ImageView getIV() {
	return iv;
    }
    
}
