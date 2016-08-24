package joshie.harvest.core.helpers;

import joshie.harvest.HarvestFestival;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import org.apache.logging.log4j.Level;

public class ChatHelper {
    /** Borrowed from Blood Magic by WayofTime **/
    private static final int DELETION_ID = 2525277;
    private static int lastAdded;

    private static void displayChatMessages(ITextComponent[] messages) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++) {
            chat.deleteChatLine(i);
        }

        for (int i = 0; i < messages.length; i++) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
        }

        lastAdded = DELETION_ID + messages.length - 1;
    }

    private static ITextComponent[] wrap(String... unlocalised) {
        String[] localised = localise(unlocalised);
        ITextComponent[] ret = new ITextComponent[localised.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = wrap(localised[i]);
        }

        return ret;
    }

    private static String[] localise(String... strings) {
        String[] newstrings = new String[strings.length];
        for (int i = 0; i < newstrings.length; i++) {
            if (strings[i].startsWith("harvestfestival.")) newstrings[i] = I18n.translateToLocal(strings[i]);
            else newstrings[i] = strings[i];
        }

        return newstrings;
    }

    private static ITextComponent wrap(String s) {
        return new TextComponentTranslation(s);
    }

    public static void displayChat(String... strings) {
        displayChatMessages(wrap(strings));
    }

    public static void displayChatAndLog(String... strings) {
        displayChat(strings); //YO DISPLAY YO CHAT
        for (String string: strings) HarvestFestival.LOGGER.log(Level.INFO, string);
    }
}
