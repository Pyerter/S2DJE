package sharp.utility;

import java.util.List;

public interface Translatable extends Updatable{

    public double getX();
    
    public double getY();

    public default CVector getPivot() {
	System.out.println(this.toString() + " has not implemented getPivot()");
    }
    
    public void setX(double x);
    
    public void setY(double y);
    
    public void rotate(double rot);
    
    public void roateAround(CVector pivot, double rot);
    
    public boolean canLocallyRotate();
    
    public List<Transform> getTransforms();
    
    public void addTransform(Transform t);
    
    public default void applyTransform(Transform t) {
	transform.apply(this);
    }

    public default void revertTransform(Transform t) {
	transform.revert(this);
    }

    public boolean getHasTransformed();

    public void setHasTransformed(boolean hasTransformed);
    
    public default void reset() {
	for (int i = getTransforms().size() - 1; i >= 0; i--) {
	    getTransforms().get(i).revert(this);
	}
	setHasTransformed(false);
    }

}
