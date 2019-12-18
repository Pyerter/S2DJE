package sharp.unit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public Img extends ImageView implements Collidable {

    private ArrayList<Collidable> collidables;
    private Projection projection;
    
    public Img(String image, double x, double y, double width, double height) {
	super(new Image(image));
	Anchor pivot = new Anchor(0, 0);
	setFitWidth(width);
	setFitHeight(height);
	projection = new Projection(pivot,
				    new CVector(x - (width / 2), y - (height / 2)),
				    new CVector(x + (width / 2), y - (height / 2)),
				    new CVector(x + (width / 2), y + (height / 2)),
				    new CVector(x - (width / 2), y + (height / 2)));
    }

    public void setX()

    public ArrayList<Collidable> getCollidables() {
	return collidables;
    }

    public void addCollidable(Collidable c) {
	if (collidables == null) {
	    collidables = new ArrayList<>();
	}
	if (!collidables.contains(c)) {
	    collidables.add(c);
	}
    }

    
    
}
