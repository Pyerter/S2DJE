package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Updatable;
import sharp.utility.Transform;
import sharp.collision.Projection;

import javafx.scene.Node;

public interface Unit extends Updatable {

    public static final Force GRAVITY = (e) -> e.getAcceleration().add(new CVector(0.0, 0.05));

    public Node getNode();
    public Projection getProjection();
    public CVector getVelocity();
    public CVector getAcceleration();
    public Double getRotVelocity();
    public Double getRotAcceleration();
    public void setRotVelocity(double set);
    public void setRotAcceleration(double set);
    
    public default Double getMass() {
	return 1.0;
    }

    public default void update() {
	setRotVelocity(getRotVelocity() + getRotAcceleration());
	setRotAcceleration(0.0);
	getVelocity().add(getAcceleration());
	getAcceleration().mult(0.0);
	Transform moveTransform = new Transform(getVelocity().getX(), getVelocity().getY());
	Transform rotTransform = new Transform(getProjection().getPivot(), getRotVelocity());
	getProjection().addTransform(moveTransform);
	getProjection().addTransform(rotTransform);
    }
    
}
