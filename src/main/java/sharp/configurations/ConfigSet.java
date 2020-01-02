package sharp.configurations;

import sharp.game.App;
import sharp.utility.WrappedValue;
import sharp.utility.ID;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class ConfigSet extends WrappedValue<Config[]> {

    private ID id;

    public ConfigSet(ID id, List<Config> list) {
	super(list.stream().toArray(Config[]::new));
	this.id = id;
    }
    
    public ConfigSet(ID id, Config[] arr) {
	super(arr);
	this.id = id;
    }
    
    public ConfigSet(ID id) {
	this(id, new Config[0]);
    }

    public ID getID() {
	return id;
    }

    public void sort() {
	Arrays.sort(getValue(), Config.COMP);
	/*setValue(Arrays.<Config>stream(getValue())
		 .sorted(Config.COMP)
		 .toArray(Config[]::new));*/
    }

    public boolean contains(Config c) {
	for (Config con: getValue()) {
	    if (c.equals(con)) {
		return true;
	    }
	}
	return false;
    }

    public boolean contains(String idName) {
	for (Config c: getValue()) {
	    if (c.getID().getName().equals(idName)) {
		return true;
	    }
	}
	return false;
    }

    public Config getConfig(String idName) {
	for (Config c: getValue()) {
	    if (c.getID().getName().equals(idName)) {
		return c;
	    }
	}
	return null;
    }

    public String formatted() {
	String s = id.formatted() + "\n";
	for (Config c: getValue()) {
	    s += c.formatted() + "\n";
	}
	return s;
    }

    public static ConfigSet create(String lines) {
	Scanner reader = new Scanner(lines);
	return create(reader);
    }
    
    public static ConfigSet create(Scanner reader) {
	try {
	    try {
		ID id = ID.create(reader.nextLine());
		LinkedList<Config> configs = new LinkedList<>();
		while (reader.hasNextLine()) {
		    try {
			configs.add(Config.create(reader.nextLine()));
		    } catch (InputMismatchException e) {
			App.print(e.getMessage());
		    }
		}
		reader.close();
		return new ConfigSet(id, configs);
	    } catch (InputMismatchException e) {
		App.print(e.getMessage());
	    }
	} catch (NoSuchElementException e) {
	    App.print("Config set file was improperly formatted. " +
		      "Place ID at top of file and configs on subsequent lines.");
	}
	return null;
    }

    public static ConfigSet combine(ConfigSet c1, ConfigSet c2) {
	Config[] c = Arrays.copyOf(c1.getValue(), c1.getValue().length + c2.getValue().length);
	for (int i = 0; i < c2.getValue().length; i++) {
	    c[i + c1.getValue().length] = c2.getValue()[i];
	}
	ConfigSet cs = new ConfigSet(c1.getID(), c);
	return cs;
    }
    
}
