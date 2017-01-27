package joshie.harvest.festivals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.special.SpecialRuleFestivals;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.festivals.block.BlockStand;
import joshie.harvest.festivals.render.SpecialRendererStand;
import joshie.harvest.festivals.cooking.CookingContestScript;
import joshie.harvest.festivals.tile.TileStand;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.calendar.Season.SPRING;
import static joshie.harvest.api.calendar.Season.WINTER;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;

@HFLoader(priority = HFBUILDING)
@SuppressWarnings("unchecked")
public class HFFestivals {
    public static final Festival COOKING_FESTIVAL = registerFestival("cooking", 22, SPRING);
    public static final Festival STARRY_NIGHT_PRE = registerFestival("starry_pre", 23, WINTER);
    public static final Script COOKING1 = new CookingContestScript();
    public static final Building FESTIVAL_GROUNDS = HFBuildings.registerBuilding("festivals", BuildingFestival.class).setSpecialRules(new SpecialRuleFestivals()).setInhabitants(HFNPCs.TRADER).setOffset(14, -1, 32);
    public static final BlockStand STAND = new BlockStand().register("stand");

    public static void preInit() {
        RegistryHelper.registerTiles(TileStand.class);
    }

    public static void init() {
        COOKING_FESTIVAL.setQuest(QuestHelper.getQuest("festival.cooking")).setNote(HFNotes.FESTIVAL_COOKING);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileStand.class, new SpecialRendererStand());
    }

    private static Festival registerFestival(String name, int day, Season season) {
        ResourceLocation resource = new ResourceLocation(MODID, name);
        Festival festival = new Festival(resource);
        HFApi.calendar.registerHoliday(festival, new CalendarDate().setDay(day).setSeason(season));
        return festival;
    }

    private static Script registerScript(NPC npc, String name) {
        return new Script(new ResourceLocation(MODID, name)).setNPC(npc);
    }
}
