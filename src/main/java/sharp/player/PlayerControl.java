package sharp.player;

import sharp.utility.CVector;
import sharp.game.App;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class PlayerControl {

    private Group group = new Group();
    private PlayerUnit p = null;

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
	System.out.println("Received: " + e);
	if (p == null) {
	    System.out.println("Player is null? " + p);
	} else if (e.getText().equals("d")) {
	    System.out.println("Sending leg rotate request");
	    p.rotateRightLeg(Math.PI / 32);
	}
    }

}
