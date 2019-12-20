package sharp.utility;

import sharp.collision.Collision;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class TimedEventRunner implements Updatable {

    private Timeline eventTimeline = new Timeline();
    private ArrayList<TimedEvent> events = new ArrayList<>();
    private Counter counter;
    private boolean endCheckCollision = false;

    public TimedEventRunner(TimedEvent ... te) {
	eventTimeline.setCycleCount(Timeline.INDEFINITE);
	counter = new Counter();
	for (TimedEvent e: te) {
	    e.setCounter(counter);
	    events.add(e);
	}
    }

    public void setCheckCollision(boolean set) {
	endCheckCollision = set;
    }

    public int getCount() {
	return counter.getCount();
    }

    public void update() {
	System.out.println("\n\n - - - - - - Frame " + counter.getCount() + ":\n\n");
	if (events.size() == 0) {
	    return;
	}
	for (int i = 0; i < events.size(); i++) {
	    System.out.println("Running event: " + events.get(i));
	    events.get(i).update();
	    if (events.get(i).isGarbage()) {
		events.remove(i);
		i--;
		System.out.println("Removing garbage...");
	    }
	}
	if (endCheckCollision) {
	    System.out.println("Making final collision updates.");
	    Collision.update();
	}
	System.out.println(" - - - - - - Ending Frame Update");
	counter.endUpdate();
	if (counter.needsSync()) {
	    System.out.println("Resyncing counter...");
	    for (TimedEvent e: events) {
		e.sync();
	    }
	    counter.reset();
	}
    }

    public ArrayList<TimedEvent> getEvents() {
	return events;
    }

    public void addTimedEvent(TimedEvent te) {
	te.setCounter(counter);
	events.add(te);
    }

    public void clear() {
	events.clear();
	counter.reset();
    }

    public void startRunning(int fr) {
	eventTimeline.stop();
	eventTimeline.getKeyFrames().clear();
	KeyFrame kf = new KeyFrame(new Duration(1000 / fr), e -> this.update());
	eventTimeline.getKeyFrames().add(kf);
	eventTimeline.play();
    }

    public void stopRunning() {
	eventTimeline.stop();
    }

}
