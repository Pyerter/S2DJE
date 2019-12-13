package sharp.unit;

public interface Unit {

    public Projector getProjector();
    public CVector getVelocity();
    public CVector getAcceleration();
    public Double getRotVelocity();
    public Double getRotAcceleration();
    public void setRotVelocity(double set);
    public void setRotAcceleration(double set);

    public default void update() {
	setRotVelocity(getRotVelocity() + getRotAcceleration());
	setRotAcceleration(0.0);
	getVelocity.add(acceleration);
	acceleration.mult(0.0);
	getProjector().update(getVelocity(), getRotVelocity());
    }
    
}
