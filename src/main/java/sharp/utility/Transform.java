package sharp.utility;

import sharp.game.App;
import sharp.unit.Force;

public class Transform {

    private double x = 0.0;
    private double y = 0.0;
    private double rot = 0.0;
    private CVector pivot = null;
    private boolean translation = false;
    private boolean rotation = false;
    
    public Transform(double x, double y) {
	translation = true;
	this.x = x;
	this.y = y;
    }

    public Transform(CVector pivot, double rot) {
	rotation = true;
	this.rot = rot;
	this.pivot = pivot;
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public double getRot() {
	return rot;
    }

    public CVector getPivot() {
	return pivot;
    }

    public boolean isTranslation() {
	return translation;
    }

    public boolean isRotation() {
	return rotation;
    }

    private void apply(Translatable t, int dir) {
	if (translation) {
	    t.setX(t.getX() + (x * dir));
	    t.setY(t.getY() + (y * dir));
	} else if (rotation) {
	    t.rotateAround(pivot, rot * dir);
	} else {
	    App.print("Uh oh! Empty transform: " + this);
	}
    }
    
    public void apply(Translatable t) {
	this.apply(t, 1);
    }

    public void revert(Translatable t) {
	this.apply(t, -1);
    }

    public Force getAsForce(double multiplier) {
	if (isTranslation()) {
	    return (e -> e.getAcceleration().add(new CVector(x * multiplier, y * multiplier)));
	} else {
	    return (e -> e.getRotAcceleration().setValue(e.getRotAcceleration().getValue() + rot * multiplier));
	}
    }

    public String toString() {
	String type = "Transform: ";
	if (translation) {
	    type += "translation in ";
	    type += "x=" + x + ", ";
	    type += "y=" + y;
	} else if (rotation) {
	    type += "rotation around ";
	    type += pivot.toString() + " ";
	    type += "by " + rot + " radians";
	}
	return type;
    }

}
