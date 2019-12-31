package sharp.utility;

import java.util.Comparator;
import java.util.InputMismatchException;

public class ID extends WrappedValue<String[]> implements Savable {

    public static final String ID_DELIM = ":";
    public static final Comparator<ID> COMP = (ID a, ID b) -> ID.compare(a.getValue(), b.getValue());

    public static final TEMP = new ID(new String[]{"temp"});
    
    private String name;
    
    public ID(String[] value) {
	super(value);
	this.name = getValue()[getValue().length - 1];
    }

    public String getName() {
	return name;
    }

    public String toString() {
	return "ID " + formatted();
    }

    public String formatted() {
	String save = "";
	for (int i = 0; i < getValue().length; i++) {
	    save += getValue()[i];
	    if (i < getValue().length - 1) {
		save += ID_DELIM;
	    }
	}
	return save;
    }

    public static ID create(String line) {
	String[] arr = line.split(ID_DELIM);
	if (arr.length < 2) {
	    throw new InputMismatchException("ID does not follow proper format (category:name)");
	}
	return new ID(arr);
    }

    public static int compare(String[] a, String[] b) {
	if (a.length > b.length) {
	    return 1;
	} else if (a.length < b.length) {
	    return -1;
	}
	for (int i = 0; i < a.length; i++) {
	    int comp = a[i].compareTo(b[i]);
	    if (comp != 0) {
		return comp;
	    }
	}
	return 0;
    }

    public boolean equals(ID id) {
	return id.toString().equals(this.toString());
    }
    
}
