package sharp.unit;

import sharp.game.App;
import sharp.utility.CVector;
import sharp.collision.Collidable;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;

public class ComplexUnit extends Group implements Unit, Collidable {

    private Projector projection;
    private ArrayList<Polygon> polies;
    private ArrayList<ImageView> images;
    private CVector velocity;
    private CVector acceleration;
    private Double rotVelocity;
    private Double rotAcceleration;
    
    public ComplexUnit(boolean hasPolies, boolean hasImages) {
	if (hasPolies) {
	    polies = new ArrayList<>();
	}
	if (hasImages) {
	    images = new ArrayList<>();
	}
    }

    public void setRotAcceleration(double rotAcceleration) {
	this.rotAcceleration = rotAcceleration;
    }

    public void setRotVelocity(double rotVelocity) {
	this.rotVelocity = rotVelocity;
    }

    public Double getRotAcceleration() {
	return rotAcceleration;
    }

    public Double getRotVelocity() {
	return rotVelocity;
    }

    public CVector getVelocity() {
	return velocity;
    }

    public CVector getAcceleration() {
	return acceleration;
    }

    public Projector getCollider() {
	return projection;
    }

    public Projector getProjector() {
	return projection;
    }

    public boolean addImage(ImageView iv) {
	if (!images.contains(iv)) {
	    images.add(iv);
	    return true;
	}
	return false;
    }

    public boolean addPoly(Polygon p) {
	if (!polies.contains(p)) {
	    polies.add(p);
	    return true;
	}
	return false;
    }

}
