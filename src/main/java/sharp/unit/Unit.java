package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Updatable;
import sharp.utility.Transform;
import sharp.collision.Projection;
import sharp.collision.Collidable;

import javafx.scene.Node;

public interface Unit extends Collidable {

    public static final Force GRAVITY = (e) -> e.getAcceleration().add(new CVector(0.0, 0.01));

    public Node getNode();
    public Projection getProjection();
    public CVector getVelocity();
    public CVector getAcceleration();
    public Double getRotVelocity();
    public Double getRotAcceleration();
    public void setRotVelocity(double set);
    public void setRotAcceleration(double set);

    public default void update() {
	System.out.println("Default unit update...");
	setRotVelocity(getRotVelocity() + getRotAcceleration());
	setRotAcceleration(0.0);
	getVelocity().add(getAcceleration());
	getAcceleration().mult(0.0);
	Transform moveTransform = new Transform(getVelocity().getX(), getVelocity().getY());
	Transform rotTransform = new Transform(getProjection().getPivot(), getRotVelocity());
	getProjection().addTransform(moveTransform);
	getProjection().addTransform(rotTransform);
	System.out.println("Starting position: " + getProjection().getPivot());
    }
    
}
