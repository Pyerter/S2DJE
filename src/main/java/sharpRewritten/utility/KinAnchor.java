package sharp.utility;

public class KinAnchor extends Anchor {

    private static final Double MAX_SPEED = new Double(10.0);
    private static final Double MAX_SPIN = new Double(Math.PI / 30);
    
    private CVector velocity;
    private CVector acceleration;
    private WrappedValue<Double> rotVelocity;
    private WrappedValue<Double> rotAcceleration;
    private LinkedList<Force> queuedForces;

    public KinAnchor() {
	super();
    }

    public KinAnchor(double x, double y, Translatable ... cons) {
	super(x, y, cons);
    }

    public KinAnchor(CVector v, Translatable ... cons) {
	super(v, cons);
    }

    public KinAnchor(double angleFrom, Translatable ... cons) {
	super(angleFrom, cons);
    }

    public CVector getVelocity() {
	return velocity;
    }

    public CVector getAcceleration() {
	return acceleration;
    }

    public WrappedValue<Double> getRotVelocity() {
	return rotVelocity;
    }

    public WrappedValue<Double> getRotAcceleration() {
	return rotAcceleration;
    }

    public void applyKinematics() {
	velocity.add(acceleration);
	double velMag = velocity.getMag();
	double checkedMag = Utility.checkBounds(velMag, -MAX_SPEED, MAX_SPEED);
	if (velMag != checkedMag) {
	    velocity.setMag(checkedMag);
	}
	acceleration.mult(0);
	rotVelocity.setValue(Utility.checkBounds(rotVelocity.getValue() + rotAcceleration.getValue(), -MAX_SPIN, MAX_SPIN));
	rotAcceleration.setValue(0.0);
	addTransform(new Transform(velocity.getX(), 0.0));
	addTransform(new Transform(0.0, velocity.getY()));
	addTransform(new Transform(this, rotVelocity));
    }

    public void update() {
	applyKinematics();
	super.update();
    }

}
