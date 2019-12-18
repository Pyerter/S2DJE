package sharp.unit;

import sharp.utility.Anchor;

public class SimpleImgUnit extends SimpleUnit {

    private Img img;
    
    public SimpleImgUnit(String  image, double x, double y, double width, double height) {
	super();
	img = new Img(image, x, y, width, height);
	super.setProjection(img.getCollider());
    }

    public Node getNode() {
	return img.getIV();
    }

}
