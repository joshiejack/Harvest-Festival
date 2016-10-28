package joshie.harvest.npc;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.INPCRegistry.Age;
import joshie.harvest.api.npc.INPCRegistry.Gender;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.npc.entity.*;
import joshie.harvest.npc.greeting.GreetingCarpenter;
import joshie.harvest.npc.greeting.GreetingLocation;
import joshie.harvest.npc.greeting.GreetingTime;
import joshie.harvest.npc.greeting.GreetingWeather;
import joshie.harvest.npc.item.ItemNPCSpawner;
import joshie.harvest.npc.item.ItemNPCTool;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
import joshie.harvest.npc.render.NPCItemRenderer;
import joshie.harvest.npc.render.NPCItemRenderer.NPCTile;
import joshie.harvest.npc.render.RenderNPC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.SHOP;
import static joshie.harvest.api.npc.INPCRegistry.Age.*;
import static joshie.harvest.api.npc.INPCRegistry.Gender.FEMALE;
import static joshie.harvest.api.npc.INPCRegistry.Gender.MALE;
import static joshie.harvest.core.helpers.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFNPCS;
import static joshie.harvest.town.BuildingLocations.*;

@HFLoader(priority = HFNPCS)
public class HFNPCs {
    public static final NPC NULL_NPC = new NPC();
    public static INPC GODDESS; //The Goddess                        (SPAWN)
    public static INPC ANIMAL_OWNER; // Owner of the Animal Barn     (BARN)
    public static INPC CAFE_OWNER; // Owner of the Cafe              (CAFE)
    public static INPC CAFE_GRANNY;// Granny of Cafe Owner           (CAFE)
    public static INPC FLOWER_GIRL; // Sister of Yulif                (CARPENTER)
    public static INPC BLACKSMITH; // Blacksmith                     (BLACKSMITH)
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
    public static INPC TRADER; //Trader                              (GENERAL STORE)

    //Item
    public static final ItemNPCSpawner SPAWNER_NPC = new ItemNPCSpawner().register("spawner_npc");
    public static final ItemNPCTool TOOLS = new ItemNPCTool().register("tool_npc");

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityNPCVillager.class, "villager", EntityIDs.VILLAGER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "builder", EntityIDs.BUILDER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "shopkeeper", EntityIDs.SHOPKEEPER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCGoddess.class, "goddess", EntityIDs.GODDESS, HarvestFestival.instance, 80, 3, true);
        registerSounds("goddess", "blessing");
        GODDESS = register("goddess", FEMALE, ADULT, 8, SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.6F);
        ANIMAL_OWNER = register("jim", MALE, ADULT, 26, SPRING, 0xDE7245, 0x722B19);
        CAFE_OWNER = register("liara", FEMALE, ADULT, 17, SPRING, 0xBEC8EE, 0x8091D0);
        FLOWER_GIRL = register("jade", FEMALE, ADULT, 14, SPRING, 0x653081, 0x361840);
        DAUGHTER_1 = register("cloe", FEMALE, ADULT, 3, SPRING, 0xFFFF99, 0xB2B200);
        PRIEST = register("thomas", MALE, ELDER, 9, SUMMER, 0x006666, 0x00B2B20);
        CLOCKMAKER_CHILD = register("fenn", MALE, CHILD, 25, SUMMER, 0x228C00, 0x003F00);
        CAFE_GRANNY = register("katlin", FEMALE, ELDER, 12, SUMMER, 0xDDDDDD, 0x777777);
        MAYOR = register("jamie", FEMALE, ELDER, 8, SUMMER, 0xA8AC9A, 0x3B636D);
        BUILDER = register("yulif", MALE, ADULT, 19, SUMMER, 0x313857, 0x121421).setIsBuilder();
        BLACKSMITH = register("daniel", MALE, ADULT, 1, WINTER, 0x613827, 0x23150E);
        DAUGHTER_2 = register("abi", FEMALE, CHILD, 27, WINTER, 0xFF99FF, 0xFF20FF);
        CLOCK_WORKER = register("tiberius", MALE, ADULT, 15, WINTER, 0x305A2E, 0x142419);
        GS_OWNER = register("jenni", FEMALE, ADULT, 7, WINTER, 0xDDD0AD, 0xE79043);
        MINER = register("brandon", MALE, ADULT, 13, AUTUMN, 0xC28D48, 0x5F5247);
        FISHERMAN = register("jacob", MALE, ADULT, 28, AUTUMN, 0x7396FF, 0x0036D9);
        MILKMAID = register("candice", FEMALE, ADULT, 5, AUTUMN, 0xF65FAB, 0xF21985);
        POULTRY = register("ashlee", FEMALE, ADULT, 16, AUTUMN, 0xC62D2D, 0x571111);
        TRADER = register("girafi", MALE, ADULT, 2, AUTUMN,  0xFFFFFF, 0xC60C30);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(SPAWNER_NPC, new MeshIdentical(SPAWNER_NPC));
        registerNPCRendering(EntityNPCVillager.class);
        registerNPCRendering(EntityNPCBuilder.class);
        registerNPCRendering(EntityNPCShopkeeper.class);
        registerNPCRendering(EntityNPCGoddess.class);
    }

    public static void init() {
        GODDESS.setLocation(HOME, GODDESSFRONT).setHasInfo(TOOLS.getStackFromEnum(NPCTool.WEATHER), new GreetingWeather());
        ANIMAL_OWNER.setLocation(HOME, BARNBUILDING).setLocation(SHOP, BARNBUILDING);
        CAFE_OWNER.setLocation(HOME, CAFEBALCONY).setLocation(SHOP, CAFETILL);
        FLOWER_GIRL.setLocation(HOME, CARPENTERUP).setLocation(SHOP, CARPENTERUP);
        DAUGHTER_1.setLocation(HOME, TOWNHALLTEENBED);
        PRIEST.setLocation(HOME, TOWNAHLLADULT);
        CLOCKMAKER_CHILD.setLocation(HOME, CLOCKMAKERUPSTAIRS);
        CAFE_GRANNY.setLocation(HOME, CAFEKITCHEN);
        MAYOR.setLocation(HOME, TOWNHALLSTAGE);
        BUILDER.setLocation(HOME, CARPENTERDOWN).setLocation(SHOP, CARPENTERFRONT).addGreeting(new GreetingCarpenter());
        BLACKSMITH.setLocation(HOME,  BLACKSMITHFURNACE).setLocation(SHOP, BLACKSMITHFURNACE);
        DAUGHTER_2.setLocation(HOME, TOWNHALLCHILDBED);
        CLOCK_WORKER.setLocation(HOME, CLOCKMAKERDOWNSTAIRS);
        GS_OWNER.setLocation(HOME, GENERALBASEMENTBACK).setLocation(SHOP, GENERALTILL);
        MINER.setLocation(HOME, MINEHUTSIDE).setLocation(SHOP, MINEHUTGRAVEL);
        FISHERMAN.setLocation(HOME, FISHINGHUTUPSTAIRS).setLocation(SHOP, FISHINGHUTDOWNSTAIRS).addGreeting(new GreetingLocation(POND));
        MILKMAID.setLocation(HOME, GENERALBEDROOM).setLocation(SHOP, BARNLEFT);
        POULTRY.setLocation(HOME, POULTRYBUILDING).setLocation(SHOP, POULTRYBUILDING);
        TRADER.setLocation(HOME, TOWNHALLRIGHT).setLocation(SHOP, GENERALENTRANCE);
        CLOCK_WORKER.setHasInfo(TOOLS.getStackFromEnum(NPCTool.CALENDAR), new GreetingTime());
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        HFClientProxy.RENDER_MAP.put(SPAWNER_NPC, NPCTile.INSTANCE);
        ClientRegistry.bindTileEntitySpecialRenderer(NPCTile.class, new NPCItemRenderer());
        for (NPC npc: NPCRegistry.REGISTRY) {
            ItemStack stack = SPAWNER_NPC.getStackFromObject(npc);
            ForgeHooksClient.registerTESRItemStack(stack.getItem(), stack.getItemDamage(), NPCTile.class);
        }
    }

    private static INPC register(String name, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor) {
        return HFApi.npc.register(new ResourceLocation(MODID, name), gender, age, dayOfBirth, seasonOfBirth, insideColor, outsideColor);
    }

    private static <E extends EntityNPC> void registerNPCRendering(Class<E> entityClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, RenderNPC :: new);
    }

    //Configure
    public static int MAX_FRIENDSHIP;
    public static int MARRIAGE_REQUIREMENT;
    public static double TOWN_DISTANCE;

    public static void configure() {
        MAX_FRIENDSHIP = getInteger("Maximum friendship", 65535);
        MARRIAGE_REQUIREMENT = getInteger("Marriage requirement", 60000);
        TOWN_DISTANCE = getDouble("Distance between towns", 256D);
    }
}