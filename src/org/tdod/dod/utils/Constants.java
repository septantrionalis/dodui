package org.tdod.dod.utils;

public class Constants {

    public static final int X_RESOLUTION = 1024;
    public static final int Y_RESOLUTION = 768;
    
    public static final int MAX_FILENAME_SIZE = 11;
    public static final int MAX_MAPNAME_SIZE = 32;

    
    public static final String MAP_DIRECTORY = "maps/";
    public static final String MAP_EXTENSION = ".dat";
    public static final String MAP_LIST_FILENAME = "maps.txt";
    
    public static String getFullMapFile(String filename) {
        return MAP_DIRECTORY + filename + MAP_EXTENSION;
    }

    public static String getMapIndexFile() {
        return MAP_DIRECTORY + MAP_LIST_FILENAME;
    }
    
    private Constants() {
    }

}
