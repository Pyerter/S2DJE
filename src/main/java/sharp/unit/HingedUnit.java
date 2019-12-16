package sharp.unit;

public class HingedUnit extends ComplexUnit {

    private HingedUnit(Unit ... units) {
	
    }
    
    private HingedUnit(CVector position, Unit ... units) {
	
    }
    
    public HingedUnit(boolean hasPolies, boolean hasImages, Unit ... units) {
	super(hasPolies, hasImages);
    }

    public HingedUnit(boolean hasPolies, boolean hasImages, CVector position, Unit ... units) {
	super(hasPolies, hasImages);
	
    }

}
