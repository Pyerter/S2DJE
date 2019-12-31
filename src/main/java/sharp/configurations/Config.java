package sharp.configurations;

import sharp.utility.WrappedValue;
import sharp.utility.ID;
import sharp.utility.Savable;

import java.util.Comparator;
import java.util.InputMismatchException;

public class Config extends WrappedValue<String> implements Savable {

    public static final String CONFIG_DELIM = "/";
    public static final Comparator<Config> COMP = (Config a, Config b) -> ID.COMP.compare(a.getID(), b.getID());
    
    private ID id;
    
    public Config(ID id, String value) {
	super(value);
	this.id = id;
    }

    public ID getID() {
	return id;
    }

    public String formatted() {
	return id.formatted() + CONFIG_DELIM + getValue();
    }

    public String toString() {
	return getValue() + "(" + id.toString() + ")";
    }

    public static Config create(String line) {
	String[] values = line.split(CONFIG_DELIM);
	if (values.length != 2) {
	    throw new InputMismatchException("Config did not follow proper formatting (String[]" +
					     CONFIG_DELIM + "String)");
	}
	ID id = ID.create(values[0]);
	String value = values[1];
	return new Config(id, value);
    }

    public boolean equals(Config c) {
	return c.getID().equals(id) && c.getValue().equals(getValue());
    }

}
