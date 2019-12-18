package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.Updatable;
import sharp.collision.Projection;

public interface Unit extends Updatable {

    public static final Force GRAVITY = (e) -> e.add(new CVector(0.0, 0.05));
    
    public Projection getProjection();
    public CVector getVelocity();
    public CVector getAcceleration();
    public Double getRotVelocity();
    public Double getRotAcceleration();
    public void setRotVelocity(double set);
    public void setRotAcceleration(double set);

    public default void update() {
	setRotVelocity(getRotVelocity() + getRotAcceleration());
	setRotAcceleration(0.0);
	getVelocity().add(getAcceleration());
	getAcceleration().mult(0.0);
	getProjector().update(getVelocity(), getRotVelocity());
    }
    
}
