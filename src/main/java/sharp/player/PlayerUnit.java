package sharp.player;

import sharp.unit.ComplexUnit;
import sharp.unit.SimplePolyUnit;
import sharp.unit.SimpleImgUnit;
import sharp.utility.CVector;
import sharp.configurations.*;

public class PlayerUnit extends ComplexUnit {

    private static final String PLAYER_CONFIGS = "player.txt";
    private ConfigSet configs;
    
    public PlayerUnit(CVector position) {
	super(position);
	configs = ConfigReader.getConfigs(App.currentConfigs + PLAYER_CONFIGS);
    }

    public void updateConfigs() {
	
    }

}
