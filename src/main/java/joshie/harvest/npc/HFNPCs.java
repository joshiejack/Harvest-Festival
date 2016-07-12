package joshie.harvest.npc;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.INPCRegistry.Age;
import joshie.harvest.api.npc.INPCRegistry.Gender;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.npc.greeting.GreetingLocation;
import joshie.harvest.npc.items.ItemNPCSpawner;
import joshie.harvest.gathering.items.ItemNPCTool;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import joshie.harvest.npc.entity.EntityNPCVillager;
import joshie.harvest.npc.render.RenderNPC;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.WORK;
import static joshie.harvest.api.npc.INPCRegistry.Age.*;
import static joshie.harvest.api.npc.INPCRegistry.Gender.FEMALE;
import static joshie.harvest.api.npc.INPCRegistry.Gender.MALE;
import static joshie.harvest.buildings.HFBuildings.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.town.TownData.*;

public class HFNPCs {
    public static final NPC NULL_NPC = new NPC();
    public static INPC GODDESS; //The Goddess                        (SPAWN)
    public static INPC ANIMAL_OWNER; // Owner of the Animal Barn     (BARN)
    public static INPC CAFE_OWNER; // Owner of the Cafe              (CAFE)
    public static INPC CAFE_GRANNY;// Granny of Cafe Owner           (CAFE)
    public static INPC SEED_OWNER; // Sister of Yulif                (CARPENTER)
    public static INPC TOOL_OWNER; // Blacksmith                     (BLACKSMITH)
    public static INPC PRIEST; //Married to mayor                    (CHURCH)
    public static INPC MAYOR; //Married to priest                    (TOWNHALL)
    public static INPC DAUGHTER_1; //Daughter of Mayor and Priest    (TOWNHALL)
    public static INPC DAUGHTER_2; //Daughter of Mayor and Priest    (TOWNHALL)
    public static INPC CLOCK_WORKER; //The clock worker              (CLOCKWORKERS)
    public static INPC CLOCKMAKER_CHILD; // Clockmakers Child        (CLOCKWORKERS)
    public static INPC GS_OWNER; //Owner of general store            (GENERAL STORE)
    public static INPC MINER; //Works in the mines                   (MINING HUT)
    public static INPC FISHERMAN; //Fisherman                        (FISHING HUT)
    public static INPC MILKMAID; //Works in the Barn, Milking Cows   (GENERAL STORE)
    public static INPC BUILDER; //Builds stuff for the players       (SPAWN)
    public static INPC POULTRY; //Poultry Farm Owner                 (POULTRY FARM)

    //Item
    public static final ItemNPCSpawner SPAWNER_NPC = new ItemNPCSpawner().register("spawner_npc");
    public static final ItemNPCTool TOOLS = new ItemNPCTool().register("tool_npc");

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityNPCVillager.class, "NPC", 0, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "NPCBuilder", 2, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "NPCShopkeeper", 3, HarvestFestival.instance, 80, 3, true);

        GODDESS = register("goddess", FEMALE, ADULT, 8, SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.05F);
        ANIMAL_OWNER = register("jim", MALE, ADULT, 26, SPRING, 0x888888, 0x000000);
        CAFE_OWNER = register("liara", FEMALE, ADULT, 17, SPRING, 0xA64DFF, 0x46008C);
        SEED_OWNER = register("jade", FEMALE, ADULT, 14, SPRING, 0x9326FF, 0x46008C);
        DAUGHTER_1 = register("cloe", FEMALE, ADULT, 3, SPRING, 0xFFFF99, 0xB2B200);
        PRIEST = register("thomas", MALE, ELDER, 9, SUMMER, 0x006666, 0x00B2B20);
        CLOCKMAKER_CHILD = register("fenn", MALE, CHILD, 25, SUMMER, 0x228C00, 0x003F00);
        CAFE_GRANNY = register("katlin", FEMALE, ELDER, 12, SUMMER, 0xDDDDDD, 0x777777);
        MAYOR = register("jamie", FEMALE, ELDER, 8, SUMMER, 0xFF9326, 0xB25900);
        BUILDER = register("yulif", MALE, ADULT, 19, SUMMER, 0x8C001A, 0x3C0000).setIsBuilder();
        TOOL_OWNER = register("daniel", MALE, ADULT, 1, WINTER, 0xD9D916, 0x6D14C7);
        DAUGHTER_2 = register("abi", FEMALE, CHILD, 27, WINTER, 0xFF99FF, 0xFF20FF);
        CLOCK_WORKER = register("tiberius", MALE, ADULT, 15, WINTER, 0xFF4C4C, 0x700606);
        GS_OWNER = register("jenni", FEMALE, ADULT, 7, WINTER, 0x2858E0, 0x000000);
        MINER = register("brandon", MALE, ADULT, 13, AUTUMN, 0xC28D48, 0x5F5247);
        FISHERMAN = register("jacob", MALE, ADULT, 28, AUTUMN, 0x7396FF, 0x0036D9);
        MILKMAID = register("candice", FEMALE, ADULT, 5, AUTUMN, 0xF65FAB, 0xF21985);
        POULTRY = register("ashlee", FEMALE, ADULT, 16, AUTUMN, 0xFF8000, 0x46008C);

    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        registerNPCRendering(EntityNPCVillager.class);
        registerNPCRendering(EntityNPCBuilder.class);
        registerNPCRendering(EntityNPCShopkeeper.class);
    }

    public static void init() {
        GODDESS.setLocation(HOME, GODDESS_POND, GODDESS_HOME);
        ANIMAL_OWNER.setLocation(HOME, BARN, JIM_HOME).setLocation(WORK, BARN, JIM_HOME);
        CAFE_OWNER.setLocation(HOME, CAFE, LIARA_HOME).setLocation(WORK, CAFE, CAFE_TILL);
        SEED_OWNER.setLocation(HOME, CARPENTER, JADE_HOME).setLocation(WORK, CARPENTER, JADE_HOME);
        DAUGHTER_1.setLocation(HOME, TOWNHALL, CLOE_HOME);
        PRIEST.setLocation(HOME, TOWNHALL, TOWNHALL_ADULT_BEDROOM).setLocation(WORK, CHURCH, THOMAS);
        CLOCKMAKER_CHILD.setLocation(HOME, CLOCKMAKER, FENN_HOME);
        CAFE_GRANNY.setLocation(HOME, CAFE, KATLIN_HOME);
        MAYOR.setLocation(HOME, TOWNHALL, JAMIE_HOME).setLocation(WORK, TOWNHALL, TOWNHALL_CENTRE);
        BUILDER.setLocation(HOME, CARPENTER, CARPENTER_DOWNSTAIRS).setLocation(WORK, CARPENTER, CARPENTER_DOOR);
        TOOL_OWNER.setLocation(HOME, BLACKSMITH, DANIEL_HOME).setLocation(WORK, BLACKSMITH, DANIEL_HOME);
        DAUGHTER_2.setLocation(HOME, TOWNHALL, ABI_HOME);
        CLOCK_WORKER.setLocation(HOME, CLOCKMAKER, TIBERIUS_HOME);
        GS_OWNER.setLocation(HOME, SUPERMARKET, JENNI_HOME).setLocation(WORK, SUPERMARKET, MARKET_TILL);
        MINER.setLocation(HOME, MINING_HUT, BRANDON_HOME).setLocation(WORK, MINING_HUT, MINER_GRAVEL);
        FISHERMAN.setLocation(HOME, FISHING_HUT, JACOB_HOME).addGreeting(new GreetingLocation(FISHING_HOLE, FISHING_POND));
        MILKMAID.setLocation(HOME, SUPERMARKET, CANDICE_HOME).setLocation(WORK, BARN, JIM_HOME);
        POULTRY.setLocation(HOME, POULTRY_FARM, ASHLEE_HOME).setLocation(WORK, POULTRY_FARM, ASHLEE_HOME);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        for (NPC npc: NPCRegistry.REGISTRY) {
            RegistryHelper.registerNPCRendererItem(npc);
        }
    }

    private static INPC register(String name, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor) {
        return HFApi.npc.register(new ResourceLocation(MODID, name), gender, age, dayOfBirth, seasonOfBirth, insideColor, outsideColor);
    }

    private static <E extends AbstractEntityNPC> void registerNPCRendering(Class<E> entityClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, RenderNPC :: new);
    }
}