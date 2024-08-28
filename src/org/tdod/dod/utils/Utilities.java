package org.tdod.dod.utils;

public class Utilities {

    public static final String NUMBER_COMMA_REGEX = "[0-9,]*";
    public static final String COORDINATE_REGEX = "^[0-9]{1,3}[,][0-9]{1,3}";
    public static final String ALPHANUMERIC_REGEX = "[a-zA-Z0-9]*";
    public static final String NUMBER_REGEX = "[0-9]*";
    public static final String ALPHANUMERIC_UNDERSCORE_REGEX = "[a-zA-Z0-9_]*";
    
    public static boolean booleanFromString(String s) {
        return s.equals("1");
    }

}
