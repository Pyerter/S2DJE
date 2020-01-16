/*package sharp.player;

import sharp.utility.CVector;
import sharp.game.App;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class PlayerControl {

    private Group group = new Group();
    private PlayerUnit p = null;

    private boolean pressRight = false;
    private boolean pressLeft = false;

    public PlayerControl() {
	p = null;
    }

    public PlayerControl(CVector place) {
	p = new PlayerUnit(place);
	group.getChildren().add(p.getNode());
    }

    public void setPlayer(CVector place) {
	if (p != null && group.getChildren().contains(p.getNode())) {
	    group.getChildren().remove(p.getNode());
	}
	p = new PlayerUnit(place);
	p.setGrav(true);
	group.getChildren().add(p.getNode());
    }

    public PlayerUnit getPlayer() {
	return p;
    }

    public Node getNode() {
	return group;
    }

    public void update() {
	p.update();
    }

    public void receiveInput(KeyEvent e) {
	if (e.getEventType().equals(KeyEvent.KEY_PRESSED)) {
	    if (p == null) {
		// do nothing
	    } else if (e.getText().equals("d")) {
		pressRight = true;
	    } else if (e.getText().equals("a")) {
		pressLeft = true;
	    }
	    if (pressRight && !pressLeft) {
		p.walk(1);
	    } else if (pressLeft && !pressRight) {
		p.walk(-1);
	    }
	} else if (e.getEventType().equals(KeyEvent.KEY_RELEASED)) {
	    if (e.getText().equals("a")) {
		pressLeft = false;
	    } else if (e.getText().equals("d")) {
		pressRight = false;
	    }
	    if (!pressRight && !pressLeft) {
		p.stand();
	    }
	}
    }

}
*/
