package sharp.unit;

import sharp.utility.Transform;
import sharp.utility.Translatable;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.collision.Collidable;
import sharp.collision.Projection;
import sharp.collision.Collision;

import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Img extends Projection implements Collidable {

    private ImageView iv;
    private LinkedList<Transform> transforms = new LinkedList<>();
    private boolean hasTransformed;
    private ArrayList<Collidable> collidables = new ArrayList<>();
    private int priority;
    private Projection[] projections;
    private CVector previousPosition = new CVector();
    
    public Img(String image, Anchor pivot, CVector dimensions) {
	super(pivot,
	      new CVector(pivot.getX() - (dimensions.getX() / 2),
			  pivot.getY() - (dimensions.getY() / 2)),
	      new CVector(pivot.getX() + (dimensions.getX() / 2),
			  pivot.getY() - (dimensions.getY() / 2)),
	      new CVector(pivot.getX() + (dimensions.getX() / 2),
			  pivot.getY() + (dimensions.getY() / 2)),
	      new CVector(pivot.getX() - (dimensions.getX() / 2),
			  pivot.getY() + (dimensions.getY() / 2)));
	iv = new ImageView("file:resources\\images\\" + image);
	iv.setFitWidth(dimensions.getX());
	iv.setFitHeight(dimensions.getY());
	iv.setX(getOutline().get(0).getX());
	iv.setY(getOutline().get(0).getY());
	Collision.setPriority(this);
	projections = new Projection[]{this};
	previousPosition.set(pivot);
	System.out.println("Image coords: " + iv.getX() + ", " + iv.getY());
    }

    public void setPreviousPosition(CVector previousPosition) {
	this.previousPosition.set(previousPosition);
    }

    public CVector getPreviousPosition() {
	return previousPosition;
    }

    public void setX(double x) {
	double diff = x - getX();
	super.setX(x);
	iv.setX(iv.getX() + diff);
	System.out.println("New image x: " + iv.getX());
    }

    public void setY(double y) {
	double diff = y - getY();
	super.setY(y);
	iv.setY(iv.getY() + diff);
	System.out.println("New image y: " + iv.getY());
    }

    public void rotate(double rot) {
	super.rotate(rot);
	iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
	// System.out.println("New image rotation: " + iv.getRotate());
    }

    public void rotateAround(CVector pivot, double rot) {
	CVector temp = CVector.subtract(getPivot(), pivot);
	super.rotateAround(pivot, rot);
	temp.rotate(rot);
	iv.setX(iv.getX() + temp.getX());
	iv.setY(iv.getY() + temp.getY());
	iv.setRotate(iv.getRotate() + Math.toDegrees(rot));
	System.out.println("New image rotation: " + iv.getRotate());
	System.out.println("Rot was: " + rot);
    }
    
    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public Projection[] getCollider() {
	return projections;
    }

    public Projection getProjection() {
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
	setPreviousPosition(getPivot());
	boolean doneMoving = !fineUpdate(discreteUpdate());
	if (doneMoving) {
	    endUpdate();
	}
    }

    public void endUpdate() {
	Collidable.super.endUpdate();
    }

    public ImageView getIV() {
	return iv;
    }
    
}
