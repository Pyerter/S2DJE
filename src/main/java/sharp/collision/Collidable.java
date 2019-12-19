package sharp.collision;

import sharp.utility.Translatable;
import sharp.utility.Transform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface Collidable extends Translatable {

    public ArrayList<Collidable> getCollidables();

    public default void addCollidables(Collidable ... c) {
	for (Collidable coll: c) {
	    if (!getCollidables().contains(coll)) {
		getCollidables().add(coll);
	    }
	}
    }
    
    public Projection getCollider();
    
    public default List<Collidable> discreteUpdate() {
	if (Collision.willFineUpdate(this)) {
	    return null;
	}
	for (Transform t: getTransforms()) {
	    applyTransform(t);
	}
	LinkedList<Collidable> collidedWith = new LinkedList<>();
	for (Collidable c: getCollidables()) {
	    if (Collision.collides(this, c)) {
		collidedWith.add(c);
	    }
	}
	if (collidedWith.size() == 0) {
	    setHasTransformed(true);
	    return null;
	}
	return collidedWith;
    }
    
    public default boolean fineUpdate(List<Collidable> collidables) {
	if (collidables == null) {
	    return false;
	}
	Collidable[] arr = new Collidable[collidables.size() + 1];
	for (int i = 0; i < collidables.size(); i++) {
	    arr[i] = collidables.get(i);
	}
	arr[arr.length - 1] = this;
	Collision.addFineColliders(arr);
	return true;
    }
    
    public int getPriority();

    public void setPriority(int priority);

    // This might be tricky... problem for later when conservation of momentum
    // might become a concern
    // public default CVector getTotalTranslation() {  }
    
}
