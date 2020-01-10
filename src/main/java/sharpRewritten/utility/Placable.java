package sharp.utility;

import sharp.game.App;

public interface Placable {

    public default boolean setCoords(double x, double y) {
	return false;
    }
    
}
