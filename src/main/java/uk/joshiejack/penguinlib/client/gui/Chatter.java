package uk.joshiejack.penguinlib.client.gui;

import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class Chatter {
    private TextFormatting formatting = TextFormatting.BOLD;
    private final String unlocalized;
    private int maxLines = 4;
    private int maxWidth = 171;
    private int height = 10;
    private String[][] script;
    private char[] fordisplay;
    private int page; //Current page displayed
    private int line; //Current lines displayed
    private double character; //A ticker, Determines what character we should be displaying
    private boolean finished; //Whether the text has finished displaying
    private long previous;
    private boolean isScriptInit;
    private boolean isInstant;

    public Chatter(String text) {
        this.unlocalized = text;
    }

    public Chatter setInstant() {
        this.isInstant = true;
        return this;
    }

    public Chatter withHeight(int height) {
        this.height = height;
        return this;
    }

    public Chatter withFormatting(@Nullable TextFormatting formatting) {
        this.formatting = formatting;
        return this;
    }

    public Chatter withLines(int lines) {
        this.maxLines = lines;
        return this;
    }

    public Chatter withWidth(int width) {
        this.maxWidth = width;
        return this;
    }

    private static String modify(String text) {
        String localized = StringHelper.localize(text);
        if (!localized.equals(text)) return localized; //Translation key instead, if it exists
        else return text;
    }

    public static String modify(String text, String... formatting) {
        if (formatting.length > 0) {
            String[] adjusted = new String[formatting.length];
            for (int i = 0; i < adjusted.length; i++) {
                adjusted[i] = modify(formatting[i]);
            }

            return StringHelper.format(text, adjusted);
        } else return StringHelper.localize(text);
    }

    private long start;

    public void update(FontRenderer fontRenderer, String... formatting) {
        if (!isScriptInit) {
            isScriptInit = true;
            script = buildScript(fontRenderer, modify(unlocalized, formatting));
            start = System.currentTimeMillis();
        }

        //Instant Draw
        if (isInstant) line = maxLines;

        //Cancel the drawing if the script is null
        if (script == null) {
            return;
        }

        //Set the text to finished if we've reached the last line
        if (!finished) {
            if (line >= 2) {
                finished = true;
            }
        }

        long current = System.currentTimeMillis();
        //If the page we are trying to parse, has a string for the line we're trying to display
        if (line < maxLines) {//If the current line, is less than the length of the lines, And we have less pages than max
            if (script[page][line] != null) {
                //Convert the next line in to a char array
                char[] todisplay = script[page][line].toCharArray();
                if (todisplay.length > 0) {
                    if (("" + todisplay[0]).equals("@")) {
                        character = todisplay.length;
                    }

                    if (character < todisplay.length && current - previous > 1.9) { //If the current position of the char array, is less than it's maximum
                        character += 0.2D; //Increase the tick, slowly
                    }
                }

                //Create a new set of chars, this is what we will display
                fordisplay = new char[(int) Math.ceil(character)];
                for (int i = 0; i < fordisplay.length; i++) {
                    if (i < todisplay.length) {
                        fordisplay[i] = todisplay[i]; //Copy all the characters over to the new array
                    }
                }

                //Now if we have completed the entire array, let's reset the position and increase the line
                if (fordisplay.length >= todisplay.length) {
                    character = 0;
                    line++;
                }
            } else finished = true;
        }

        previous = current;
    }

    public void draw(FontRenderer fontRenderer, int x, int y, int color) {
        if (!isScriptInit) return;
        //Draws all the current 'completed' strings to the gui
        for (int i = 0; i < line; i++) {
            String text = script[page][i];
            if (text != null) {
                if (this.formatting == null) fontRenderer.drawString(text, x, y + (i * height), color);
                else fontRenderer.drawString(this.formatting + text, x, y + (i * height), color);
            }
        }

        //If the page we are trying to parse, has a string for the line we're trying to display
        if (line < maxLines) {//If the current line, is less than the length of the lines, And we have less pages than max
            if (script[page][line] != null) {
                //Draw the characters as we go.
                if (this.formatting == null) fontRenderer.drawString(new String(fordisplay), x, y + (line * height), color);
                else fontRenderer.drawString(this.formatting + new String(fordisplay), x, y + (line * height), color);
            } else finished = true;
        }
    }

    private String[][] buildScript(FontRenderer fontRenderer, String text) {
        List<String> formatted = fontRenderer.listFormattedStringToWidth(text, maxWidth);
        String[] original = formatted.toArray(new String[0]);
        int size = original.length / maxLines;
        boolean isRemainder = original.length % maxLines == 0;
        if (!isRemainder) {
            size++;
        }

        int start = 0;
        String[][] script = new String[size][maxLines];
        for (int i = 0; i < size; i++) {
            int length = Math.min((start + maxLines), original.length);
            String[] subtext = Arrays.copyOfRange(original, start, length);
            System.arraycopy(subtext, 0, script[i], 0, subtext.length);
            start = start + maxLines;
        }

        return script;
    }

    public int getPage() {
        return page;
    }

    private boolean previousChat() {
        if (!finished) {
            finished = true;
            line = maxLines;
        } else if (page > 0) {
            finished = false; //Reset the page being finished
            //line = 0; //Reset the line we are currently reading
            page--; //Reset the page we are currently reading
        }

        return false;
    }

    private boolean nextChat() {
        if (!finished) {
            finished = true;
            line = maxLines;
            return false;
        } else if (page < (script.length - 1)) {
            finished = false; //Reset the page being finished
            line = 0; //Reset the line we are currently reading
            page++; //Reset the page we are currently reading
            return false;
        } else return true;
    }

    public boolean mouseClicked(int mouseButton) {
        return mouseButton == 0 && nextChat() || mouseButton == 1 && previousChat();
    }
}
