package sharp.utility;

public class WrappedValue <T> {

    private T value;
    
    public WrappedValue(T value) {
	this.value = value;
    }

    public T getValue() {
	return value;
    }

    public void setValue(T value) {
	this.value = value;
    }

}
