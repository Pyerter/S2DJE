package sharp.configurations;

import sharp.utility.ID;
import sharp.game.App;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NumberFormatException;

public class ConfigReader {

    public static final String MALFORMED_CONFIG = "Malformed config; does not fit proper format";

    /**
     * This method takes a file name and creates a scanner using
     * the given file name. Must include path starting from the scifirpg directory.
     * If the requested file does not exist, it creates the requested file.
     *
     * @param fileName - the file to search for/create
     * @return a scanner using the requested file
     */
    public static Scanner getFileScanner(String fileName) {
	File newFile = new File(fileName);
	if (!newFile.exists()) {
	    App.print("Requested file not found... creating file: " + fileName);
	    try {
		if (newFile.createNewFile()) {
		    try {
			Scanner reader = new Scanner(newFile);
			return reader;
		    } catch (FileNotFoundException e) {
			throw new IOException("File created but is unreadable: " + fileName);
		    }
		}
	    } catch (IOException ioe) {
		App.print(ioe.getMessage());
	    }
	} else {
	    try {
		Scanner reader = new Scanner(new File(fileName));
		return reader;
	    } catch (FileNotFoundException fnfe) {
		App.print("Huh... something went really wrong.");
	    }
	}
	App.print("Cannot make new file: " + fileName);
	return null;
    }

    /**
     * This method takes a file name and creates a file writer using the
     * given file name (which starts from the scifirpg directory).
     * If the requested file does not exist, it is created.
     *
     * @param fileName - the file to search for/create
     * @param the requested writer
     */
    public static FileWriter getFileWriter(String fileName) {
	File file = new File(fileName);
	try {
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    FileWriter fw = new FileWriter(fileName, true);
	    return fw;
	} catch (IOException ioe) {
	    App.print("IOException when creating FileWriter for: " + fileName + ", " + ioe.getMessage());
	    return null;
	}
    }

    /**
     * This method takes a file name and returns a config set by creating
     * it through a reader of the file.
     *
     * @param fileName - the file to retrieve the configs in
     * @return the requested set of configs
     */
    public static ConfigSet getConfigs(String fileName) {
	Scanner reader = ConfigReader.getFileScanner(fileName);
	if (reader == null) {
	    return ConfigSet.EMPTY;
	}
	ConfigSet cs = ConfigSet.create(ConfigReader.getFileScanner(fileName));
	if (cs == null) {
	    App.print("Returning null config set under: " + fileName);
	}
	return cs;
    }

    public static boolean setConfigs(String fileName, Config ... c) {
	ConfigSet cs = getConfigs(fileName);
	try {
	    if (cs == null) {
		FileWriter fw = getFileWriter(fileName);
		for (Config con: c) {
		    fw.write(con.formatted());
		}
		fw.close();
		return true;
	    } else {
		ConfigSet addingSet = new ConfigSet(ID.TEMP, c);
		ConfigSet newSet = ConfigSet.combine(cs, addingSet);
		FileWriter fw = getFileWriter(fileName);
		fw.write(newSet.formatted());
		fw.close();
		return true;
	    }
	} catch (IOException e) {
	    return false;
	}
    }

}
