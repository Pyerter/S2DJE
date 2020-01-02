package sharp.player;

import sharp.game.App;
import sharp.unit.ComplexUnit;
import sharp.unit.HingedUnit;
import sharp.unit.SimplePolyUnit;
import sharp.unit.SimpleImgUnit;
import sharp.unit.Img;
import sharp.utility.CVector;
import sharp.utility.Anchor;
import sharp.configurations.*;

public class PlayerUnit extends HingedUnit {

    private static final String PLAYER_CONFIGS = "player.txt";
    private ConfigSet configs;
    private HingedUnit face;
    
    public PlayerUnit(CVector position) {
	super(new SimplePolyUnit(new Anchor(0, 0),
				 new CVector(-20, -40),
				 new CVector(0, -40),
				 new CVector(20, -40),
				 new CVector(20, -35),
				 new CVector(20, 40),
				 new CVector(8, 40),
				 new CVector(-8, 40),
				 new CVector(-20, 40),
				 new CVector(-20, -35)));
	configs = ConfigReader.getConfigs(App.getConfigsPath() + PLAYER_CONFIGS);
	loadPresets();
	setX(App.HALF_WIDTH);
	setY(App.HALF_HEIGHT / 2);
    }

    public void loadPresets() {
	CVector imgOffset = new CVector(-0.5, -0.5);
	SimpleImgUnit faceBase = new SimpleImgUnit(App.getImagesPath() + "base_0.png", 0, 0, imgOffset);
	if (configs.contains("base")) {
	    faceBase = new SimpleImgUnit(App.getImagesPath() + configs.getConfig("base").getValue(), 0, 0, imgOffset);
	    // configs.getConfig("base_")
	}
	face = new HingedUnit(faceBase);
	if (configs.contains("left")) {
	    face.addChildUnit(new SimpleImgUnit(App.getImagesPath() + configs.getConfig("left").getValue(), 0, 0, imgOffset));
	}
	if (configs.contains("right")) {
	    face.addChildUnit(new SimpleImgUnit(App.getImagesPath() + configs.getConfig("right").getValue(), 0, 0, imgOffset));
	}
	if (configs.contains("brow")) {
	    face.addChildUnit(new SimpleImgUnit(App.getImagesPath() + configs.getConfig("brow").getValue(), 0, 0, imgOffset));
	}
	if (configs.contains("hair")) {
	    face.addChildUnit(new SimpleImgUnit(App.getImagesPath() + configs.getConfig("hair").getValue(), 0, 0, imgOffset));
	}
	addHingedUnit(face, 1);

	
    }

}
