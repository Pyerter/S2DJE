package sharp.collision;

public class CollisionCalculator {

        /**
     * This method takes two polygons and splits up their parts
     * and checks with other methods to see if the two collide.
     *
     * @param p1 - the first polygon to check with
     * @param p2 - the second polygon to check with
     * @return true if the two overlap at all
     */
    public static CVector collides(Projection p1, Projection p2) {
	App.print("Checking collision (with projections) between " + p1 + " and " + p2);
	CVector diff = CVector.subtract(p1.getPivot(), p2.getPivot());
	if (p1.getCollisionRadius() + p2.getCollisionRadius() < diff.getMag()) {
	    App.print("The distances were too far. They don't collide.");
	    return null;
	}

	int end = 0;
	for (int start = 0; start < p1.getOutline().size(); start++) {
	    end = start + 1;
	    if (end == p1.getOutline().size()) {
		end = 0;
	    }

	    CVectorPair vp = new CVectorPair(p1.getOutline().get(start), p1.getOutline().get(end));

            CVector collidePoint = collides(p2, vp);
            if (collidePoint != null) {
                return collidePoint;
            }
        }

	for (int start = 0; start < p1.getOutline().size(); start++) {
	    CVector collidePoint = collides(p2, p1.getOutline().get(start));
            if (collidePoint != null) {
                return collidePoint;
            }
        }

	for (int start = 0; start < p2.getOutline().size(); start++) {
	    CVector collidePoint = collides(p1, p2.getOutline().get(start));
            if (collidePoint != null) {
                return collidePoint;
            }
        }
	
        return null;
    }

    
    /**
     * This method takes a polygon and a line and checks if any of the polygon's
     * lines collide with the specified line.
     *
     * @param p - the polygon to check with
     * @param l1 - the line to check with
     * @return true if any parts collided
     */
    public static CVector collides(Projection p, CVectorPair vp2) {
        int end = 0;
        for (int start = 0; start < p.getOutline().size(); start++) {
            end = start + 1;
            if (end == p.getOutline().size()) {
                end = 0;
            }

	    CVectorPair vp1 = new CVectorPair(p.getOutline().get(start), p.getOutline().get(end));

            CVector collidePoint = collides(vp1, vp2);
            if (collidePoint != null) {
                return collidePoint;
            }
        }

        return null;
    }

    /**
     * This method takes two lines and uses a special formula to check
     * if the two lines overlap at all.
     *
     * @param l1 - the first line to check with
     * @param l2 - the second line to check with
     * @return true if the lines overlap
     */
    public static CVector collides(CVectorPair p1, CVectorPair p2) {
        // This equation is found to be similar as the divider, so it's only calculated once
        double divider = ((p2.getEndY() - p2.getStartY()) * (p1.getEndX() - p1.getStartX()) -
                         (p2.getEndX() - p2.getStartX()) * (p1.getEndY() - p1.getStartY()));

        double uA = ((p2.getEndX() - p2.getStartX()) * (p1.getStartY() - p2.getStartY()) -
                    (p2.getEndY() - p2.getStartY()) * (p1.getStartX() - p2.getStartX())) /
            divider;

        double uB = ((p1.getEndX() - p1.getStartX()) * (p1.getStartY() - p2.getStartY()) -
                    (p1.getEndY() - p1.getStartY()) * (p1.getStartX() - p2.getStartX())) /
            divider;


        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
	    double intersectionX = p1.getStartX() + (uA * (p1.getEndX() - p1.getStartX()));
	    double intersectionY = p1.getStartY() + (uA * (p1.getEndY() - p1.getStartY()));
	    return new CVector(intersectionX, intersectionY);
        }
        return null;
    }

    
    /**
     * This method takes a point and a polygon and checks if the point
     * is inside of the polygon at all.
     *
     * @param p - the polygon to check inside of
     * @param v - the point to check inside the polygon
     * @return true if the point is inside the polygon
     */
    public static CVector collides(Projection p, CVector v) {
        boolean doesCollide = false;

	// App.print("Checking poly/point collisions with " + p + " and " + v);

        int end = 0;
        for (int start = 0; start < p.getOutline().size(); start++) {
            end = start + 1;
            if (end == p.getOutline().size()) {
                end = 0;
            }

	    CVectorPair vp = new CVectorPair(p.getOutline().get(start), p.getOutline().get(end));
	    
            if (((vp.getStartY() >= v.getY()) != (vp.getEndY() > v.getY())) &&
                (v.getX() < (vp.getEndX() - vp.getStartX()) *
                 (v.getY() - vp.getStartY()) /
                 (vp.getEndY() - vp.getStartY()) +
                  vp.getStartX())) {
                doesCollide = !doesCollide;
            }
        }

	if (doesCollide) {
	    return new CVector(v);
	}
        return null;
    }

    /**
     * This method is used to find the minimum distance from a point to a collidable.
     *
     * @param v - if this point comes from a unit/collidable, pass it in as CVector.subtract(v, getPivot())
     * @param c -
     * @return the distance
     */
    public static Double getDistanceFromPoint(CVector v, Collidable c) {
	WrappedValue<Double> closestDistance = new WrappedValue<>(null);
	for (Projection p: c.getCollider()) {
	    closestDistance = getDistanceFromPoint(v, p, closestDistance);
	}
	return closestDistance.getValue();
    }

    public static Double getDistanceFromPoint(CVector v, Projection p) {
	return getDistanceFromPoint(v, p, new WrappedValue<Double>(null)).getValue();
    }

    public static WrappedValue<Double> getDistanceFromPoint(CVector v, Projection p, WrappedValue<Double> closestDist) {
	int end = 0;
	for (int i = 0; i < p.getOutline().size(); i++) {
	    end = i + 1;
	    if (end == p.getOutline().size()) {
		end = 0;
	    }
	    CVectorPair diff = new CVectorPair(p.getOutline().get(i), p.getOutline().get(end));
	    double lengthSq = Math.pow(diff.getLength(), 2);
	    
	    
	    double check = -1;
	    double dotProduct = CVector.dot(v, CVector.subtract(diff.getV2(), diff.getV1()));
	    if (lengthSq != 0) {
		check = dotProduct / lengthSq;
	    }

	    CVector dist = new CVector();
	    if (check < 0) {
		dist.setX(diff.getStartX());
		dist.setY(diff.getStartY());
	    } else if (check > 1) {
		dist.setX(diff.getEndX());
		dist.setX(diff.getEndY());
	    } else {
		dist.setX(diff.getStartX() + (check * (diff.getEndX() - diff.getStartX())));
		dist.setY(diff.getStartY() + (check * (diff.getEndY() - diff.getStartY())));
	    }

	    dist.setX(v.getX() - dist.getX());
	    dist.setY(v.getY() - dist.getY());

	    double distance = dist.getMag();
	    if (closestDist.getValue() == null) {
		closestDist.setValue(distance);
	    } else if (distance < closestDist.getValue()) {
		closestDist.setValue(distance);
	    }
	}

	return closestDist;
    }
    
    
}
