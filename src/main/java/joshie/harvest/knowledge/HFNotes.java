package joshie.harvest.knowledge;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderCursedTools;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.knowledge.Category.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFLoader
public class HFNotes {
    public static final Note BLUEPRINTS = registerNote(TOWNSHIP, "blueprints");
    public static final Note SHOPPING = registerNote(TOWNSHIP, "shops");
    public static final Note CROP_FARMING = registerNote(FARMING, "farming");
    public static final Note SICKLE = registerNote(FARMING, "sickle");
    public static final Note SHIPPING = registerNote(OTHER, "shipping");
    public static final Note SUPERMARKET = registerNote(TOWNSHIP, "supermarket");

    public static final Note SECRET_CURSED_TOOLS = registerNote(ACTIVITIES, "secret.cursed").setSecretNote();

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        SECRET_CURSED_TOOLS.setRender(new NoteRenderCursedTools());
    }

    private static Note registerNote(Category category, String name) {
        return new Note(category, new ResourceLocation(MODID, name));
    }
}
