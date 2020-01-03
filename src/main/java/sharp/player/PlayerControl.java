package sharp.player;

import sharp.utility.CVector;
import sharp.game.App;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class PlayerControl {

    private Group group = new Group();
    private PlayerUnit player = null;

    public PlayerControl() {
	player = null;
    }

    public PlayerControl(CVector place) {
	player = new PlayerUnit(place);
	group.getChildren().add(player.getNode());
    }

    public void setPlayer(CVector place) {
	if (player != null && group.getChildren().contains(player.getNode())) {
	    group.getChildren().remove(player.getNode());
	}
	player = new PlayerUnit(place);
	player.setGrav(true);
	group.getChildren().add(player.getNode());
    }

    public PlayerUnit getPlayer() {
	return player;
    }

    public Node getNode() {
	return group;
    }

    public void update() {
	System.out.println("Player while updating: " + player);
	player.update();
    }

    public void receiveInput(KeyEvent e) {
	System.out.println("Received: " + e);
	if (player == null) {
	    System.out.println("Player is null? " + player);
	} else if (e.getText().equals("d")) {
	    player.rotateRightLeg(Math.PI / 32);
	}
    }

}
