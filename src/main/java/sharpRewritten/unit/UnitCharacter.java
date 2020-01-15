package sharp.unit;

public class UnitCharacter extends LinkedUnit<ProjectionCalculator> {
    
    public UnitCharacter(CVector pivot, LinkedUnit ... units) {
	super(new ProjectionCalculator(new Anchor(pivot)));
	addSubUnit(units);
    }

    public void addSubUnit(LinkedUnit u, CVector offset) {
	super.addSubUnit(u, offset);
	getProjection().calculateCollider(getAllSubUnits());
    }

    public void addSubUnit(LinkedUnit ... u) {
	for (LinkedUnit unit: u) {
	    super.addSubUnit(unit);
	}
	getProjection().calculateCollider(getSubUnits());
    }

    public LinkedUnit getParentUnit() {
	return null;
    }

    public LinkedUnit getRootParentUnit() {
	return this;
    }

    public Projection[] getCollider() {
	return getProjection().getCollider();
    }

}
