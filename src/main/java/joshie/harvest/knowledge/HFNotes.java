package joshie.harvest.knowledge;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.knowledge.Category;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.block.BlockCookware.Cookware;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderCursedTools;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderRepairing;
import joshie.harvest.knowledge.gui.stats.notes.render.NoteRenderUpgrading;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockElevator.Elevator;
import joshie.harvest.mining.block.BlockOre.Ore;
import joshie.harvest.tools.HFTools;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
    public static final Note RECIPE_BOOK = registerNote(ACTIVITIES, "recipebook");
    public static final Note KITCHEN_COUNTER = registerNote(ACTIVITIES, "counter");
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

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        UPGRADING.setRender(new NoteRenderUpgrading());
        REPAIRING.setRender(new NoteRenderRepairing());
        SECRET_CURSED_TOOLS.setRender(new NoteRenderCursedTools());
    }

    @SuppressWarnings("unused")
    public static void postInit() {
        BLUEPRINTS.setIcon(HFBuildings.BLUEPRINTS.getStackFromObject(HFBuildings.CARPENTER));
        CROP_FARMING.setIcon(new ItemStack(Items.CARROT));
        SHIPPING.setIcon(HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING));
        MAILBOX.setIcon(HFCore.STORAGE.getStackFromEnum(Storage.MAILBOX));
        CHICKEN_CARE.setIcon(HFAnimals.ANIMAL.getStackFromEnum(Spawner.CHICKEN));
        COW_CARE.setIcon(HFAnimals.ANIMAL.getStackFromEnum(Spawner.COW));
        SHEEP_CARE.setIcon(HFAnimals.ANIMAL.getStackFromEnum(Spawner.SHEEP));
        HAMMER.setIcon(HFTools.HAMMER.getStack(ToolTier.BASIC));
        AXE.setIcon(HFTools.AXE.getStack(ToolTier.BASIC));
        SICKLE.setIcon(HFTools.SICKLE.getStack(ToolTier.BASIC));
        MINING.setIcon(HFMining.ORE.getStackFromEnum(Ore.COPPER));
        RECIPES.setIcon(CookingHelper.getRecipe("salad"));
        RECIPE_BOOK.setIcon(new ItemStack(HFCooking.COOKBOOK));
        KITCHEN_COUNTER.setIcon(HFCooking.COOKWARE.getStackFromEnum(Cookware.COUNTER));
        FRIDGE.setIcon(HFCooking.COOKWARE.getStackFromEnum(Cookware.FRIDGE));
        OVEN.setIcon(HFCooking.COOKWARE.getStackFromEnum(Cookware.OVEN_OFF));
        MIXER.setIcon(HFCooking.COOKWARE.getStackFromEnum(Cookware.MIXER));
        POTPAN.setIcon(HFCooking.COOKWARE.getStackFromEnum(Cookware.POT));
        ELEVATOR.setIcon(HFMining.ELEVATOR.getStackFromEnum(Elevator.JUNK));
        SUPERMARKET.setIcon(HFBuildings.STRUCTURES.getStackFromObject(HFBuildings.SUPERMARKET));
    }

    public static Note registerNote(Category category, String name) {
        return new Note(category, new ResourceLocation(MODID, name));
    }
}
