package sharp.collision;

import sharp.untility.Projection;

public interface Collidable extends Translatable {

    public ArrayList<Collidable> getCollidables();
    
    public Projection getCollider();
    
    private List<Collidable> discreteUpdate();
    
    private void fineUpdate(List<Collidable> collidables) {
	
    }
    
    public int getPriority();

    public void setPriority(int priority);
    
    public default CVector getTotalTranslation() {
	CVector sum = new CVector(0.0, 0.0);
	for (Transform t: getTransforms()) {
	    t.apply(sum);
	}
	return sum;
    }
    
    public default double getMass() {
	return 1.0;
    }

}
