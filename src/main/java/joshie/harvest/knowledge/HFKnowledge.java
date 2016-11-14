package joshie.harvest.knowledge;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.item.ItemBook;
import joshie.harvest.knowledge.stats.notes.render.NoteRenderCursedTools;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.knowledge.Category.ACTIVITIES;
import static joshie.harvest.api.knowledge.Category.TOWNSHIP;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFLoader
public class HFKnowledge {
    public static final ItemBook BOOK = new ItemBook().register("book");
    public static final Note BLUEPRINTS = registerNote(TOWNSHIP, "blueprints");
    public static final Note SHOPPING = registerNote(TOWNSHIP, "shops");
    public static final Note SECRET_CURSED_TOOLS = registerNote(ACTIVITIES, "secret.cursed").setSecretNote();

    public static void preInit() {}
    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        SECRET_CURSED_TOOLS.setRender(new NoteRenderCursedTools());
    }

    private static Note registerNote(Category category, String name) {
        Note note = new Note(category).setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(note);
        return note;
    }
}
