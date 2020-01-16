package sharp.collision;

import sharp.unit.Poly;
import sharp.unit.Img;

public class ProjectionGenerator {

    public static Poly createRect(CVector center, CVector dimensions) {
	return createRect(center, dimensions, false);
    }
    
    public static Poly createRect(CVector center, CVector dimensions, boolean rnd) {
	Poly p =  new Poly(new Anchor(center),
			   new CVector(center.getX() - (dimensions.getX() / 2),
				       center.getY() - (dimensions.getY() / 2)),
			   new CVector(center.getX() + (dimensions.getX() / 2),
				       center.getY() - (dimensions.getY() / 2)),
			   new CVector(center.getX() + (dimensions.getX() / 2),
				       center.getY() + (dimensions.getY() / 2)),
			   new CVector(center.getX() - (dimensions.getX() / 2),
				       center.getY() + (dimensions.getY() / 2)));
	p.round(rnd);
	return p;
    }

    public static Poly createSquare(CVector center, double size) {
	return createRect(center, new CVector(size, size));
    }

    public static Poly createSquare(CVector center, double size, boolean rnd) {
	return createRect(center, new CVector(size, size), rnd);
    }

}
