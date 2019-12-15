package sharp.unit;

import sharp.collision.Collidable;
import sharp.utility.CVector;

import javafx.scene.shape.Polygon;

public class SimpleUnit extends Projector implements Unit, Collidable {

    private Projector projection;
    private CVector velocity;
    private CVector acceleration;
    private Double rotVelocity;
    private Double rotAcceleration;
    
    public SimpleUnit() {
	super();
    }

    public Projector getCollider() {
	return this;
    }

    public Projector getProjector() {
	return this;
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

    
}
