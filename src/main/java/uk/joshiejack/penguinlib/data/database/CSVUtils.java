package uk.joshiejack.penguinlib.data.database;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    private static String toString(List<Character> chars) {
        return chars.size() > 0 ? chars.stream().map(Object::toString).reduce((acc, e) -> acc  + e).get() : "";
    }

    public static List<String> parse(String line) {
        List<String> list = Lists.newArrayList();
        char[] chars = line.toCharArray();

        boolean previousWasQuotes = false;
        boolean insideQuotes = false;
        List<Character> characters = Lists.newArrayList();

        for (char c: chars) {
            if (c == '"' && !previousWasQuotes) {  //Inside, Outside
                insideQuotes = !insideQuotes;  // r   ""
                previousWasQuotes = true;
            } else if (c == ',' && !insideQuotes) {
                list.add(toString(characters));
                characters = Lists.newArrayList();
                previousWasQuotes = false;

            } else {
                characters.add(c);
                previousWasQuotes = false;
            }
        }

        list.add(toString(characters)); //Add the last part
        return list;
    }


    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    private static List<String> parseLine(String cvsLine, char separators, char customQuote) {
        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

}
