package joshie.harvest.knowledge;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderCursedTools;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderRepairing;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderUpgrading;
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
    public static final Note MAILBOX = registerNote(TOWNSHIP, "mailbox");
    public static final Note SUPERMARKET = registerNote(TOWNSHIP, "supermarket");
    public static final Note CHICKEN_CARE = registerNote(FARMING, "care.chickens");
    public static final Note COW_CARE = registerNote(FARMING, "care.cows");
    public static final Note SHEEP_CARE = registerNote(FARMING, "care.sheep");
    public static final Note ANIMAL_HAPPINESS = registerNote(FARMING, "care.happiness");
    public static final Note ANIMAL_STRESS = registerNote(FARMING, "care.stress");

    public static final Note HAMMER = registerNote(ACTIVITIES, "hammer");
    public static final Note AXE = registerNote(ACTIVITIES, "axe");
    public static final Note MINING = registerNote(ACTIVITIES, "mining");
    public static final Note UPGRADING = registerNote(TOWNSHIP, "upgrading");
    public static final Note COOKING = registerNote(ACTIVITIES, "cooking");
    public static final Note RECIPES = registerNote(ACTIVITIES, "recipes");
    public static final Note RECIPE_BOOK = registerNote(ACTIVITIES, "counter");
    public static final Note KITCHEN_COUNTER = registerNote(ACTIVITIES, "recipebook");
    public static final Note FRIDGE = registerNote(ACTIVITIES, "fridge");
    public static final Note OVEN = registerNote(ACTIVITIES, "oven");
    public static final Note MIXER = registerNote(ACTIVITIES, "mixer");
    public static final Note POTPAN = registerNote(ACTIVITIES, "potpan");
    public static final Note ELEVATOR = registerNote(ACTIVITIES, "elevator");

    public static final Note REPAIRING = registerNote(TOWNSHIP, "repairing");
    public static final Note SECRET_CURSED_TOOLS = registerNote(ACTIVITIES, "secret.cursed").setSecretNote();
    public static final Note SECRET_CHICKENS = registerNote(FARMING, "secret.chickens").setSecretNote();
    public static final Note SECRET_RELATIONSHIPS = registerNote(FARMING, "secret.relationships").setSecretNote();
    public static final Note SECRET_LIVESTOCK = registerNote(FARMING, "secret.livestock").setSecretNote();

    public static final Note FESTIVAL_COOKING = registerNote(TOWNSHIP, "festival.cooking");

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        UPGRADING.setRender(new NoteRenderUpgrading());
        REPAIRING.setRender(new NoteRenderRepairing());
        SECRET_CURSED_TOOLS.setRender(new NoteRenderCursedTools());
    }

    private static Note registerNote(Category category, String name) {
        return new Note(category, new ResourceLocation(MODID, name));
    }
}
