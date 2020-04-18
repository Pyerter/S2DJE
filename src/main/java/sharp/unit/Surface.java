package sharp.unit;

public class Surface {

    private CVectorPair points;

    public Surface(CVector end1, CVector end2) {
	points = new CVectorPair(end1, end2);
    }
    
    public boolean isOnSurface(CVector point) {
	double lengthSq = Math.pow(diff.getLength(), 2);

	double check = -1;
	double dotProduct = CVector.dot(point, CVector.subtract(points.getV2(), points.getV1()));
	if (lengthSq != 0) {
	    check = dotProduct / lengthSq;
	}

	CVector dist = new CVector();
	if (check < 0) {
	    dist.setX(points.getStartX());
	    dist.setY(points.getStartY());
	} else if (check > 1) {
	    dist.setX(points.getEndX());
	    dist.setY(points.getEndY());
	} else {
	    dist.setX(points.getStartX() + (check * (points.getEndX() - points.getStartX())));
	    dist.setY(points.getStartY() + (check * (points.getEndY() - points.getStartY())));
	}

	dist.setX(point.getX() - dist.getX());
	dist.setY(point.getY() - dist.getY());

	double distance = dist.getMag();
	return distance <= 0.5;
    }

    public double heading() {
	return points.getAngle();
    }

    /**
     * This returns true if this surface is perpendicular to the given direction
     * within a certain threshold.
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
