package sharp.collision;

import sharp.unit.Poly;
import sharp.unit.Img;
import sharp.utility.CVector;
import sharp.utility.Anchor;

/** This class is used to generate often used projections to reduce redundancy. */
public class ProjectionGenerator {

    /**
     * Create a rectangle with a given center and dimensions, defaulting to no rounding.
     *
     * @param center - the center coord of the rectangle
     * @param dimensions - the width and height of the rectangle
     * @return the Poly created around the center using the dimensions
     */
    public static Poly createRect(CVector center, CVector dimensions) {
	return createRect(center, dimensions, false);
    }
    
    /**
     * Create a rectangle with a given center, dimensions, and rounding.
     *
     * @param center - the center coord of the rectangle
     * @param dimensions - the width and height of the rectangle
     * @param rnd - if the rectangle should round the corners
     * @return the Poly created around the center using the dimensions
     */
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

    /**
     * Create a square with a given center and size, defaulting to no rounding.
     *
     * @param center - the center coord of the rectangle
     * @param size - the width and height of the rectangle
     * @return the Poly created around the center using the dimensions
     */
    public static Poly createSquare(CVector center, double size) {
	return createRect(center, new CVector(size, size));
    }

    /**
     * Create a square with a given center, size, and rounding.
     *
     * @param center - the center coord of the rectangle
     * @param size - the width and height of the rectangle
     * @param rnd - if the rectangle should round the corners
     * @return the Poly created around the center using the dimensions
     */
    public static Poly createSquare(CVector center, double size, boolean rnd) {
	return createRect(center, new CVector(size, size), rnd);
    }

}
