package joshie.harvest.npc;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.npc.NPC.Age;
import joshie.harvest.npc.NPC.Gender;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import joshie.harvest.npc.render.RenderNPC;
import joshie.harvest.npc.town.TownData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.npc.NPC.Age.*;
import static joshie.harvest.npc.NPC.Gender.FEMALE;
import static joshie.harvest.npc.NPC.Gender.MALE;

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
    public static final ItemNPCSpawner SPAWNER_NPC = new ItemNPCSpawner().setUnlocalizedName("spawner.npc");

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "NPCBuilder", 2, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "NPCShopkeeper", 3, HarvestFestival.instance, 80, 3, true);
        //EntityRegistry.registerModEntity(EntityNPCMiner.class, "NPCMiner", 4, HarvestFestival.instance, 80, 3, true);

        GODDESS = register("goddess", FEMALE, ADULT, 8, Season.SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.05F);
        ANIMAL_OWNER = register("jim", MALE, ADULT, 26, Season.SPRING, 0x888888, 0x000000);
        CAFE_OWNER = register("liara", FEMALE, ADULT, 17, Season.SPRING, 0xA64DFF, 0x46008C);
        SEED_OWNER = register("jade", FEMALE, ADULT, 14, Season.SPRING, 0x9326FF, 0x46008C);
        DAUGHTER_1 = register("cloe", FEMALE, ADULT, 3, Season.SPRING, 0xFFFF99, 0xB2B200);
        PRIEST = register("thomas", MALE, ELDER, 9, Season.SUMMER, 0x006666, 0x00B2B20);
        CLOCKMAKER_CHILD = register("fenn", MALE, CHILD, 25, Season.SUMMER, 0x228C00, 0x003F00);
        CAFE_GRANNY = register("katlin", FEMALE, ELDER, 12, Season.SUMMER, 0xDDDDDD, 0x777777);
        MAYOR = register("jamie", FEMALE, ELDER, 8, Season.SUMMER, 0xFF9326, 0xB25900);
        BUILDER = register("yulif", MALE, ADULT, 19, Season.SUMMER, 0x8C001A, 0x3C0000).setIsBuilder();
        TOOL_OWNER = register("daniel", MALE, ADULT, 1, Season.WINTER, 0xD9D916, 0x6D14C7);
        DAUGHTER_2 = register("abi", FEMALE, CHILD, 27, Season.WINTER, 0xFF99FF, 0xFF20FF);
        CLOCK_WORKER = register("tiberius", MALE, ADULT, 15, Season.WINTER, 0xFF4C4C, 0x700606);
        GS_OWNER = register("jenni", FEMALE, ADULT, 7, Season.WINTER, 0x2858E0, 0x000000);
        MINER = register("brandon", MALE, ADULT, 13, Season.AUTUMN, 0xC28D48, 0x5F5247);
        FISHERMAN = register("jacob", MALE, ADULT, 28, Season.AUTUMN, 0x7396FF, 0x0036D9);
        MILKMAID = register("candice", FEMALE, ADULT, 5, Season.AUTUMN, 0xF65FAB, 0xF21985);
        POULTRY = register("ondra", MALE, ADULT, 16, Season.AUTUMN, 0xFF8000, 0x46008C);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        registerNPCRendering(EntityNPC.class);
        registerNPCRendering(EntityNPCBuilder.class);
        registerNPCRendering(EntityNPCShopkeeper.class);
    }

    public static void init() {
        GODDESS.setHome(HFBuildings.GODDESS_POND, TownData.GODDESS);
        ANIMAL_OWNER.setHome(HFBuildings.BARN, TownData.JIM);
        CAFE_OWNER.setHome(HFBuildings.CAFE, TownData.LIARA);
        SEED_OWNER.setHome(HFBuildings.CARPENTER, TownData.JADE);
        DAUGHTER_1.setHome(HFBuildings.TOWNHALL, TownData.CLOE);
        PRIEST.setHome(HFBuildings.TOWNHALL, TownData.TOWNHALL_ADULT_BEDROOM);
        CLOCKMAKER_CHILD.setHome(HFBuildings.CLOCKMAKER, TownData.FENN);
        CAFE_GRANNY.setHome(HFBuildings.CAFE, TownData.KATLIN);
        MAYOR.setHome(HFBuildings.TOWNHALL, TownData.JAMIE);
        BUILDER.setHome(HFBuildings.CARPENTER, TownData.CARPENTER_DOWNSTAIRS);
        TOOL_OWNER.setHome(HFBuildings.BLACKSMITH, TownData.DANIEL);
        DAUGHTER_2.setHome(HFBuildings.TOWNHALL, TownData.ABI);
        CLOCK_WORKER.setHome(HFBuildings.CLOCKMAKER, TownData.TIBERIUS);
        GS_OWNER.setHome(HFBuildings.SUPERMARKET, TownData.JENNI);
        MINER.setHome(HFBuildings.MINING_HUT, TownData.BRANDON);
        FISHERMAN.setHome(HFBuildings.FISHING_HUT, TownData.JACOB);
        MILKMAID.setHome(HFBuildings.SUPERMARKET, TownData.CANDICE);
        POULTRY.setHome(HFBuildings.POULTRY_FARM, TownData.ONDRA);
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

    private static <E extends EntityNPC> void registerNPCRendering(Class<E> entityClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<E>() {
            @Override
            public Render<? super E> createRenderFor(RenderManager manager) {
                return new RenderNPC(manager);
            }
        });
    }
}