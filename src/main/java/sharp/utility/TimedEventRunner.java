package sharp.utility;

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

    public TimedEventRunner(TimedEvent ... te) {
	counter = new Counter();
	for (TimedEvent e: te) {
	    e.setCounter(counter);
	    events.add(e);
	}
    }

    public void update() {
	if (events.size() == 0) {
	    System.out.println("Huh>");
	    return;
	}
	for (int i = 0; i < events.size(); i++) {
	    events.get(i).update();
	    System.out.println("Updating...");
	    if (events.get(i).isGarbage()) {
		events.remove(i);
		i--;
		System.out.println("Removing garbage...");
	    }
	}
	counter.endUpdate();
	if (counter.needsSync()) {
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
