package sharp.utility;

import java.util.List;

public interface Translatable extends Updatable {

    public double getX();
    
    public double getY();

    public default CVector getPivot() {
	System.out.println(this.toString() + " has not implemented getPivot()");
	return null;
    }
    
    public void setX(double x);
    
    public void setY(double y);
    
    public void rotate(double rot);
    
    public void rotateAround(CVector pivot, double rot);
    
    public boolean canLocallyRotate();
    
    public List<Transform> getTransforms();
    
    public void addTransform(Transform t);
    
    public default void applyTransform(Transform t) {
	t.apply(this);
    }

    public default void revertTransform(Transform t) {
	t.revert(this);
    }

    public boolean getHasTransformed();

    public void setHasTransformed(boolean hasTransformed);
    
    public default void reset() {
	for (int i = getTransforms().size() - 1; i >= 0; i--) {
	    revertTransform(getTransforms().get(i));
	}
	setHasTransformed(false);
    }

    public default void endUpdate() {
	this.getTransforms().clear();
	this.setHasTransformed(false);
    }

}
