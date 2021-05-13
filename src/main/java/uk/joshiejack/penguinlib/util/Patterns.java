package uk.joshiejack.penguinlib.util;

import java.util.regex.Pattern;

public class Patterns {
    public final static Pattern DOUBLE_PATTERN = Pattern.compile("[\\+\\-]?\\d+\\.\\d+(?:[eE][\\+\\-]?\\d+)?");
    public final static Pattern INTEGER_PATTERN = Pattern.compile ("[\\+\\-]?\\d+");
    public final static Pattern BOOLEAN_PATTERN = Pattern.compile ("^(?i)(true|false)$");
}
