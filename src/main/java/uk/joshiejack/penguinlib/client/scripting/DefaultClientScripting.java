package uk.joshiejack.penguinlib.client.scripting;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.client.scripting.wrappers.BookLabelJS;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID, value = Side.CLIENT)
public class DefaultClientScripting {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        event.registerExtensible(BookLabelJS.class, LabelBook.class);
    }
}
