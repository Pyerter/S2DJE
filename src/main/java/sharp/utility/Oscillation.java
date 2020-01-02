package sharp.utility;

public class Oscillation extends WrappedValue<Double> implements Updatable {

    private double inc;
    private double threshold;
    private boolean lerp;
    private int direction = 1;
    private double previousValue = 0;
    private double accuracy = 0.01;
    
    public Oscillation(double start, double inc, double threshold, boolean lerp) {
	super(start);
	previousValue = start;
	this.inc = inc;
	this.threshold = threshold;
	this.lerp = lerp;
    }

    public void setAccuracy(double accuracy) {
	this.accuracy = accuracy;
    }

    public void update() {
	previousValue = super.getValue();
	if (lerp) {
	    System.out.println(super.getValue());
	    super.setValue(Utility.lerp(super.getValue(), threshold * direction, inc));
	    if (Utility.isAbout(super.getValue(), threshold * direction, accuracy)) {
		direction *= -1;
		update();
	    }
	} else {
	    super.setValue(super.getValue() + (inc * direction));
	    if (Math.abs(super.getValue()) >= threshold) {
		direction *= -1;
		update();
	    }
	}
    }

    public Double getOscillated() {
	update();
	return super.getValue() - previousValue;
    }
    
}
