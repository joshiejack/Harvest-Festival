package uk.joshiejack.penguinlib.util.helpers.minecraft;

import uk.joshiejack.penguinlib.PenguinLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import org.apache.logging.log4j.Level;

public class ChatHelper {
    private static final int DELETION_ID = 2525277;
    private static int lastAdded;

    private static void displayChatMessages(ITextComponent[] messages) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        if (chat.getLineCount() > 0) {
            for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++) {
                chat.deleteChatLine(i);
            }
        }

        for (int i = 0; i < messages.length; i++) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
        }

        lastAdded = DELETION_ID + messages.length - 1;
    }

    private static ITextComponent[] wrap(String... unlocalised) {
        //String[] localised = localise(unlocalised);
        ITextComponent[] ret = new ITextComponent[unlocalised.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = wrap(unlocalised[i]);
        }

        return ret;
    }

    @SuppressWarnings("deprecation")
    private static String[] localise(String... strings) {
        String[] newstrings = new String[strings.length];
        for (int i = 0; i < newstrings.length; i++) {
            if (strings[i].startsWith("harvestfestival.")) {
                newstrings[i] = I18n.translateToLocal(strings[i]);
            } else newstrings[i] = strings[i];
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
        for (String string: strings) {
            PenguinLib.logger.log(Level.INFO, string);
        }
    }
}
