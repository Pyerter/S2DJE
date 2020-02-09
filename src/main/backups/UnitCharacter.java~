package sharp.unit;

import sharp.collision.Projection;
import sharp.collision.ProjectionCalculator;
import sharp.utility.CVector;
import sharp.utility.Anchor;

public class UnitCharacter extends LinkedUnit<ProjectionCalculator> {
    
    public UnitCharacter(CVector pivot, LinkedUnit<? extends Projection> ... units) {
	super(new ProjectionCalculator(new Anchor(pivot)));
	addSubUnit(units);
    }

    public void addSubUnit(LinkedUnit<? extends Projection> u, CVector offset) {
	super.addSubUnit(u, offset);
	getProjection().calculateCollider(getAllSubUnits());
    }

    public void addSubUnit(LinkedUnit<? extends Projection> ... u) {
	for (LinkedUnit<? extends Projection> unit: u) {
	    super.addSubUnit(unit);
	}
	getProjection().calculateCollider(getSubUnits());
    }

    public LinkedUnit<? extends Projection> getParentUnit() {
	return null;
    }

    public LinkedUnit<? extends Projection> getRootParentUnit() {
	return this;
    }

    public Projection[] getCollider() {
	return getProjection().getCollider();
    }

}
