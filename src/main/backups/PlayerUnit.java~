package sharp.player;

import sharp.unit.HingedUnit;
import sharp.unit.SimplePolyUnit;
import sharp.unit.SimpleImgUnit;
import sharp.unit.Img;
import sharp.utility.CVector;
import sharp.configurations.*;

public class PlayerUnit extends HingedUnit {

    private static final String PLAYER_CONFIGS = "player.txt";
    private ConfigSet configs;
    private HingedUnit face;
    
    public PlayerUnit(CVector position) {
	super(position);
	configs = ConfigReader.getConfigs(App.currentConfigs + PLAYER_CONFIGS);
    }

    public void loadPresets() {
	SimpleImgUnit baseFace = new SimpleImgUnit(App.imageResources + "base_0", getRootUnit().getPivot().getX(), getRootUnit().getPivot().getY(), new CVector(0, 0));
	face = new HingedUnit(baseFace);
	if (configs.contains("base_")) {
	    // configs.getConfig("base_")
	}
    }

}
