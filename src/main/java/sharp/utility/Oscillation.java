package sharp.utility;

import java.util.function.Predicate;

public class Oscillation extends WrappedValue<Double> implements Updatable {

    private double start;
    private double inc;
    private double highThreshold;
    private double lowThreshold;
    private boolean lerp;
    private int direction = 1;
    private double previousValue = 0;
    private double accuracy = 0.01;
    
    public Oscillation(double start, double inc, double lowThreshold, double highThreshold, boolean lerp) {
	super(start);
	this.start = start;
	previousValue = start;
	this.inc = inc;
	this.lowThreshold = lowThreshold;
	this.highThreshold = highThreshold;
	this.lerp = lerp;
    }

    public void setAccuracy(double accuracy) {
	this.accuracy = accuracy;
    }

    public int update() {
	previousValue = super.getValue();
	if (lerp) {
	    double threshold = highThreshold;
	    if (direction == -1) {
		threshold = lowThreshold;
	    }
	    super.setValue(Utility.lerp(super.getValue(), threshold * direction, inc));
	    if (direction == 1) {
		checkThresholdLerp(highThreshold);
	    } else {
		checkThresholdLerp(lowThreshold);
	    }
	} else {
	    super.setValue(super.getValue() + (inc * direction));
	    if (direction == -1) {
		checkThresholdNorm(e -> e < lowThreshold);
	    } else {
		checkThresholdNorm(e -> e > highThreshold);
	    }
	}
	return 0;
    }

    private void checkThresholdLerp(double threshold) {
	if (Utility.isAbout(super.getValue(), threshold, accuracy)) {
	    direction *= -1;
	    update();
	}
    }

    private void checkThresholdNorm(Predicate<Double> p) {
	if (p.test(super.getValue())) {
	    direction *= -1;
	    update();
	}
    }

    public Double getOscillated() {
	update();
	return super.getValue() - previousValue;
    }

    public void reset() {
	setValue(start);
	direction = 1;
    }

    public String toString() {
	return "Oscillator: start(" + start + "), low(" + lowThreshold + "), high(" +
	    highThreshold + "), lerps(" + lerp + "), value(" + super.getValue() + ")";
    }
    
}
