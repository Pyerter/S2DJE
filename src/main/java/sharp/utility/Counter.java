package sharp.utility;

/**
 * This class represents a counter that can be used to tally or keep track of anything,
 * such at the amount of frames that have been updated.
 */
public class Counter implements Updatable {

    private final int UPDATES_TO_CRASH = 5;
    private int count = 0;
    private int inc = 1;
    private int reset = 0;
    private boolean updated = false;

    /**
     * Create the counter with the following presets.
     * count = 0, increment = 1, reset = 0
     */
    public Counter() {
	// Base stuff is set
    }

    /**
     * Create the counter by giving the basic values.
     * @param count - the current number of counts
     * @param reset - the number to reset to when the counter resets
     * @param inc - the increment value for each update
     */
    public Counter(int count, int reset, int inc) {
	this.count = count;
	this.reset = reset;
	this.inc = inc;
    }

    /**
     * Reset the count value to reset.
     */
    public void reset() {
	count = reset;
    }

    /**
     * Get the reset value.
     */
    public int getReset() {
	return reset;
    }

    /**
     * Update the counter.
     */
    public int update() {
	return update(false);
    }

    /**
     * Update the counter, given a value of wether or not to force
     * the update this frame.
     * @param force - if true, regardless of if this has been updated, it will update
     * @return 0 if the update was succesfull, -1 if not
     */
    public int update(boolean force) {
	if (force || !updated) {
	    count += inc;
	    updated = true;
	    return 0;
	}
	return -1;
    }

    /**
     * This call ends the update, allowing update to be called again with no issue.
     * updated is a variable in this class that is set to true when update is called,
     * and this method sets it to false. If updated is true when update is false,
     * this counter will not update.
     */
    public void endUpdate() {
	updated = false;
    }

    /**
     * Get the current count value.
     */
    public int getCount() {
	return count;
    }

    /**
     * Returns true if this counter needs to be reset. This counter needs to be reset
     * if the count value is close to the max value (Integer.MAX_VALUE).
     *
     * @return true if this counter needs to reset
     */
    public boolean needsSync() {
	return count >= Integer.MAX_VALUE - UPDATES_TO_CRASH;
    }

    /**
     * This method returns the reset threshold. If count is this close to the
     * max value or closer, it needs to be reset.
     *
     * @return the reset threshold
     */
    public int getResetThreshold() {
	return UPDATES_TO_CRASH;
    }

}
