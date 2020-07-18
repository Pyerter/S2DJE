package sharp.unit;

import sharp.utility.CVector;
import sharp.utility.CVectorPair;
import sharp.utility.Anchor;
import sharp.utility.Transform;
import sharp.utility.Utility;

import sharp.unit.Poly;

import sharp.collision.CollisionCalculator;

/**
 * This class represents a line segment that can be used for things like
 * a person walking up a ramp. The vector pair is used to represent the ends
 * and there are methods to determine if a given point is on a surface and can
 * traverse up the surface, depending on the angle.
 * Due to this object not extending {@code Collidable}, it is recommended to
 * extend collidable with a personal class and add a surface check method into
 * the update methods, or use this class with personalized collision checking.
 */
public class Surface {

    private CVectorPair points;
    private Poly projection;
    private float restingThreshold = 0.5f;

    /**
     * Creates a surface class with given end points.
     *
     * @param end1 - the starting end point
     * @param end2 - the ending end point
     */
    public Surface(CVector end1, CVector end2) {
	points = new CVectorPair(end1, end2);
	projection = new Poly(new Anchor(CVector.mult(CVector.add(end1, end2), 0.5)), end1, end2);
    }

    /**
     * This method returns true if the distance from the given point is less than
     * the resting threshold to this surface.
     *
     * @param point - the point to measure the distance to
     * @return returns true if the point is considered resting on this surface
     */
    public boolean isOnSurface(CVector point) {
	return CollisionCalculator.getDistanceFromPoint(point, projection) < 0.5;
    }

    /**
     * Returns an angle, in radians, that the surface is heading.
     *
     * @return directly returns CVectorPair.getAngle()
     */
    public double heading() {
	return points.getAngle();
    }

    /**
     * This returns true if this surface is perpendicular to the given direction
     * within a certain threshold.
     *
     * @param perpDir - should typically be gravity if the unit is a player trying to walk
     * up this surface as if it were a slope
     * @param threshold - a value in radians which defines how close the angle of this
     * surface can be to the direction given
     */
    public boolean canTread(CVector perpDir, double threshold) {
	double dirSurface = heading();
	double dirGoal = perpDir.heading();
	return Utility.isAbout(dirSurface, dirGoal + Math.PI / 4, threshold)
	    || Utility.isAbout(dirSurface, dirGoal - Math.PI / 4, threshold);
    }

    /**
     * Takes a transformation and, if it is a translation, creates a new vector of the
     * same magnitude with the angle of this surface. If this is not the projection that
     * fits what you need, it is recommended to use an operation involving the dot product
     * of the surface vector pair and the translation as a vector.
     *
     * @param t - the transformation to project over the surface
     * @return the new transformation translation
     */
    public Transform projectTransformation(Transform t) {
	if (!t.isTranslation()) {
	    return t;
	}

	CVector trans = new CVector(t.getX(), t.getY());
	double distance = trans.getMag();
	double dirSurface = heading();
	if (!Utility.isAbout(dirSurface, trans.heading(), Math.PI / 4)) {
	    dirSurface += Math.PI / 2;
	}

	CVector newTrans = new CVector(dirSurface);
	newTrans.setMag(distance);

	return new Transform(newTrans.getX(), newTrans.getY());
    }
    
}
