package sharp.unit;

import sharp.collision.Collidable;
import sharp.utility.CVector;

import javafx.scene.shape.Polygon;

public class SimpleUnit extends Projection implements Unit, Collidable {

    private Img img;
    private Poly poly;
    private boolean polyUnit;
    private boolean imgUnit;
    private CVector velocity;
    private CVector acceleration;
    private Double rotVelocity;
    private Double rotAcceleration;
    
    public SimpleUnit() {
	super();
	poly = new Poly();
	polyUnit = true;
    }

    public Projection getCollider() {
	return this;
    }

    public Projection getProjection() {
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
