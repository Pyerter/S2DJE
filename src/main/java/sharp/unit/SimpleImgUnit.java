package sharp.unit;

import sharp.utility.Anchor;

import javafx.scene.Node;

public class SimpleImgUnit extends SimpleUnit {

    private Img img;
    
    public SimpleImgUnit(String  image, double x, double y, double width, double height) {
	super();
	img = new Img(image, x, y, width, height);
    }

    public Projection getProjection() {
	return img.getCollider();
    }
    
    public Projection getCollider() {
	return img.getCollider();
    }

    public Node getNode() {
	return img.getIV();
    }

    public void setX(double x) {
	img.setX(x);
    }

    public void setY(double y) {
	img.setY(y);
    }

    public double getX() {
	return img.getX();
    }

    public double getY() {
	return img.getY();
    }

    public void rotate(double rot) {
	img.rotate(rot);
    }

    public void rotateAround(CVector pivot, double rot) {
	img.rotateAround(pivot, rot);
    }

    public boolean canLocallyRotate() {
	return img.canLocallyRotate();
    }

    public List<Transform> getTransforms() {
	return img.getTransforms();
    }

    public void addTransform(Transform t) {
	img.addTransform(t);
    }

    public void applyTransform(Transform t) {
	img.applyTransform(t);
    }

    public void revertTransform(Transform t) {
	img.revertTransform(t);
    }

    public boolean getHasTransformed() {
	return img.getHasTransformed();
    }

    public void setHasTransformed(boolean hasTransformed) {
	img.setHasTransormed(hasTransformed);
    }

    public ArrayList<Collidable> getCollidables() {
	return img.getCollidables();
    }

}
