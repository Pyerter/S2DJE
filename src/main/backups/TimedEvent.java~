package sharp.utility;

import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.event.ActionEvent;

public class TimedEvent implements Updatable {

    private EventHandler<Event> event;
    private Counter counter;
    private boolean privCounter;
    private int syncCount;
    private int threshold;

    private int handleCount = 0;
    private boolean meeseeks = false;
    private boolean garbage = false;
    
    public TimedEvent(EventHandler<Event> event, int threshold) {
	this.event = event;
	this.threshold = threshold;
	counter = new Counter();
	privCounter = true;
	syncCount = 0;
    }

    public TimedEvent(EventHandler<Event> event, int threshold, Counter counter) {
	this.event = event;
	this.threshold = threshold;
	this.counter = counter;
	privCounter = false;
	syncCount = counter.getCount();
    }

    public void update() {
	counter.update();
	if (privCounter) {
	    if (counter.getCount() % threshold == 0 ) {
		event.handle(new ActionEvent());
		handleCount++;
		if (meeseeks) {
		    garbage = true;
		}
		counter.reset();
	    }
	} else {
	    if ((counter.getCount() - syncCount) % threshold == 0) {
		event.handle(new ActionEvent());
	    }
	}
	
    }

    public void setMeeseeks(boolean meeseeks) {
	this.meeseeks = meeseeks;
    }

    public boolean isGarbage() {
	return garbage;
    }

    public int getHandleCount() {
	return handleCount;
    }

    /**
     * This method sets the new counter variable for this timed event and
     * sync this event so that it does not interrupt any updated frames.
     *
     * @param counter - the new counter
     */
    public void setCounter(Counter counter) {
	privCounter = false;
	int currentCount = (this.counter.getCount() - syncCount) % threshold;
	int newCount = counter.getCount() % threshold;
	syncCount = currentCount - newCount + counter.getCount();
	this.counter = counter;
    }

    /**
     * This method syncs this timed event to this event's counter, 
     * assuming the counter is going to reset before the next update.
     */
    public void sync() {
	int currentCount = (counter.getCount() - syncCount) % threshold; // get how many updates have passed
	int resetCount = counter.getReset() % threshold; // get how many updates would have passed on reset
	syncCount = currentCount - resetCount + counter.getReset(); // fill in the difference and add to reset
    }

}
