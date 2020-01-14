package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Updatable;
import sharp.utility.Transform;
import sharp.utility.Translatable;
import sharp.utility.KinAnchor;
import sharp.collision.Projection;
import sharp.collision.Collidable;

import javafx.scene.Node;

import java.util.Arrays;

public class Unit <T extends Projection> implements Collidable, Translatable {

    public static final Double MAX_SPIN = Math.PI / 6;

    private T projection;
    private T[] collider;
    private ArrayList<Collidable> collidables;
    private KinAnchor kinematics;
    private boolean show;
    
    public Unit(T projection) {
	this.projection = projection;
	collider = new T[]{projection};
    }

    public Node getNode() {
	t.getNode();
    }
    
    public T[] getCollider() {
	return collider;
    }

    public void setCollider(T[] collider) {
	this.collider = collider;
    }

    public void addColliders(T ... colliders) {
	int oldLength = this.collider.length;
	this.collider = Arrays.copyOf(this.collider, oldLength + colliders.length);
	for (int i = 0; i < this.collider.length; i++) {
	    this.collider[i + oldLength] = colliders[i];
	}
    }

    public void addColliders(T[] ... colliders) {
	for (T[] colls: colliders) {
	    addColliders(colls);
	}
    }

    public T getProjection() {
	return projection;
    }
    
    public void setProjection(T projection) {
	this.projection = projection;
    }

    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public void setCollidables(ArrayList<Collidable> collidables) {
	this.collidables = collidables;
    }

    public void getKinAnchor() {
	return kinematics;
    }
    
    public boolean getGrav() {
	return grav;
    }
    
    public void setGrav(boolean grav) {
	this.grav = grav;
    }
    
    public boolean getShow() {
	return show;
    }
    
    public void setShow(boolean show) {
	this.show = show;
    }

    public void setX(double x) {
	projection.setX(x);
    }

    public void setY(double y) {
	projection.setY(y);
    }

    public void set(CVector v) {
	setX(v.getX());
	setY(v.getY());
    }

    public double getX() {
	return projection.getX();
    }

    public double getY() {
	return projection.getY();
    }

    public void rotate(double rot) {
	projection.rotate(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	projection.rotateAround(pivot, rot);
    }

    public double getRotation() {
	return projection.getRotation();
    }

    public void update() {
	
    }
    
}
