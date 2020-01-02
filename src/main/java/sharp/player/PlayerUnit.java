package sharp.player;
 
import sharp.game.App;
import sharp.unit.*;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.utility.Transform;
import sharp.configurations.*;

import javafx.scene.paint.Color;

public class PlayerUnit extends HingedUnit {

    private static final String PLAYER_CONFIGS = "player.txt";
    private static final SimplePolyUnit PLAYER_BASE_UNIT = new SimplePolyUnit(new Anchor(0, 0),
									new CVector(-20, -40),
									new CVector(0, -40),
									new CVector(20, -40),
									new CVector(20, -35),
									new CVector(20, 40),
									new CVector(8, 40),
									new CVector(-8, 40),
									new CVector(-20, 40),
									new CVector(-20, -35));
    private ConfigSet configs;
    private HingedUnit face;
    private SimpleImgUnit faceBase = null;
    
    public PlayerUnit(CVector position) {
	super(PLAYER_BASE_UNIT);
	//PLAYER_BASE_UNIT.getPolygon().setFill(new Color(0.0, 0.0, 0.0, 0.0));
	//getGroup().getChildren().remove(getRootUnit().getNode());
	configs = ConfigReader.getConfigs(App.getConfigsPath() + PLAYER_CONFIGS);
	loadPresets();
	applyTransform(new Transform(position.getX(), position.getY()));
    }

    public void loadPresets() {
	String playerFrontFacePath = "player character" + App.getFileSeperator() + "front face" + App.getFileSeperator();
	CVector imgOffset = new CVector(-0.5, -0.5);
	faceBase = new SimpleImgUnit(playerFrontFacePath + "base_0.png", 0, 0, imgOffset);
	if (configs.contains("base")) {
	    faceBase = new SimpleImgUnit(playerFrontFacePath + configs.getConfig("base").getValue(), 0, 0, imgOffset);
	} else {
	    App.print("Creating base config img: " + configs.getConfig("base").getValue());
	}
	// face = new HingedUnit(faceBase);
	face = new HingedUnit(SimplePolyUnit.square(10));
	face.addChildUnit(faceBase);
	if (configs.contains("left")) {
	    face.addChildUnit(new SimpleImgUnit(playerFrontFacePath + configs.getConfig("left").getValue(), 0, 0, imgOffset));
	}
	if (configs.contains("right")) {
	    face.addChildUnit(new SimpleImgUnit(playerFrontFacePath + configs.getConfig("right").getValue(), 0, 0, imgOffset));
	}
	if (configs.contains("brow")) {
	    face.addChildUnit(new SimpleImgUnit(playerFrontFacePath + configs.getConfig("brow").getValue(), 0, 0, imgOffset));
	}
	if (configs.contains("hair")) {
	    face.addChildUnit(new SimpleImgUnit(playerFrontFacePath + configs.getConfig("hair").getValue(), 0, 0, imgOffset));
	}
	addHingedUnit(face, 1);
    }

    public void update() {
	super.update();
    }

}
