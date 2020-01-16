package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Updatable;
import sharp.utility.Transform;
import sharp.collision.Projection;
import sharp.collision.Collidable;

import javafx.scene.Node;

public interface Unit extends Collidable {

    public static final Force GRAVITY = (e) -> e.getAcceleration().add(new CVector(0.0, 1));

    public static final Double MAX_SPIN = Math.PI / 6;

    public Node getNode();
    public Projection getProjection();
    public CVector getVelocity();
    public CVector getAcceleration();
    public Double getRotVelocity();
    public Double getRotAcceleration();
    public void setRotVelocity(double set);
    public void setRotAcceleration(double set);
    public void setGrav(boolean grav);
    public boolean getGrav();
    public boolean getShow();
    public void setShow(boolean show);

    public default void update() {
	setRotVelocity(getRotVelocity() + getRotAcceleration());
	setRotAcceleration(0.0);
	getVelocity().add(getAcceleration());
	getAcceleration().mult(0.0);
	Transform moveXTransform = new Transform(getVelocity().getX(), 0.0);
	Transform moveYTransform = new Transform(0.0, getVelocity().getY());
	Transform rotTransform = new Transform(getProjection().getPivot(), getRotVelocity());
	getProjection().addTransform(moveXTransform);
	getProjection().addTransform(moveYTransform);
	getProjection().addTransform(rotTransform);
    }
    
}
