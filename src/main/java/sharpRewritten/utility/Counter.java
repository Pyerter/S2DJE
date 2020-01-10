package sharp.utility;

public class Counter implements Updatable {

    private final int UPDATES_TO_CRASH = 5;
    private int count = 0;
    private int inc = 1;
    private int reset = 0;
    private boolean updated = false;
    
    public Counter() {
	// Base stuff is set
    }

    public Counter(int count, int reset, int inc) {
	this.count = count;
	this.reset = reset;
	this.inc = inc;
    }

    public void reset() {
	count = reset;
    }

    public int getReset() {
	return reset;
    }

    public void update() {
	update(false);
    }

    public void update(boolean force) {
	if (force || !updated) {
	    count += inc;
	    updated = true;
	}
    }

    public void endUpdate() {
	updated = false;
    }

    public int getCount() {
	return count;
    }

    public boolean needsSync() {
	return count >= Integer.MAX_VALUE - UPDATES_TO_CRASH;
    }

}
