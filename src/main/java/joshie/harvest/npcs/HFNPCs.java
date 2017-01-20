package joshie.harvest.npcs;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.ScheduleBuilder;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.npcs.entity.*;
import joshie.harvest.npcs.greeting.*;
import joshie.harvest.npcs.item.ItemNPCSpawner;
import joshie.harvest.npcs.item.ItemNPCTool;
import joshie.harvest.npcs.npc.NPCHolidayStore;
import joshie.harvest.npcs.render.NPCItemRenderer;
import joshie.harvest.npcs.render.NPCItemRenderer.NPCTile;
import joshie.harvest.npcs.render.RenderNPC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.InvocationTargetException;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.api.calendar.Weekday.MONDAY;
import static joshie.harvest.api.calendar.Weekday.SUNDAY;
import static joshie.harvest.api.npc.INPCHelper.Age.*;
import static joshie.harvest.api.npc.INPCHelper.Gender.FEMALE;
import static joshie.harvest.api.npc.INPCHelper.Gender.MALE;
import static joshie.harvest.api.npc.NPC.Location.HOME;
import static joshie.harvest.api.npc.NPC.Location.SHOP;
import static joshie.harvest.core.helpers.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.*;
import static joshie.harvest.core.lib.LoadOrder.HFNPCS;
import static joshie.harvest.town.BuildingLocations.*;

@HFLoader(priority = HFNPCS)
@SuppressWarnings("unchecked")
public class HFNPCs {
    public static final NPC GODDESS = register("goddess", FEMALE, ADULT, 8, SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.6F); //The Goddess                        (SPAWN)
    public static final NPC BUILDER = register("yulif", MALE, ADULT, 19, SUMMER, 0x313857, 0x121421); //Builds stuff            (SPAWN)
    public static final NPC FLOWER_GIRL = register("jade", FEMALE, ADULT, 14, SPRING, 0x653081, 0x361840); // Sister of Yulif                  (CARPENTER)
    public static final NPC GS_OWNER = register("jenni", FEMALE, ADULT, 7, WINTER, 0xDDD0AD, 0xE79043); //Owner of general store               (GENERAL STORE)
    public static final NPC MILKMAID = register("candice", FEMALE, ADULT, 5, AUTUMN, 0xF65FAB, 0xF21985); //Works in the Barn, Milking Cows    (GENERAL STORE)
    public static final NPC BARN_OWNER = register("jim", MALE, ADULT, 26, SPRING, 0xDE7245, 0x722B19); // Owner of the Animal Barn             (BARN)
    public static final NPC POULTRY = register("ashlee", FEMALE, ADULT, 16, AUTUMN, 0xC62D2D, 0x571111); //Poultry Farm Owner                  (POULTRY FARM)
    public static final NPC TRADER = register("girafi", MALE, ADULT, 2, AUTUMN,  0xFFFFFF, 0xC60C30); //Trader                                 (GENERAL STORE)
    public static final NPC FISHERMAN = register("jacob", MALE, ADULT, 28, AUTUMN, 0x7396FF, 0x0036D9); //Fisherman                            (FISHING HUT)
    public static final NPC MINER = register("brandon", MALE, ADULT, 13, AUTUMN, 0xC28D48, 0x5F5247); //Works in the mines                     (MINE)
    public static final NPC CAFE_OWNER = register("liara", FEMALE, ADULT, 17, SPRING, 0xBEC8EE, 0x8091D0, NPCHolidayStore.class); // Owner of the Cafe                (CAFE)
    public static final NPC CAFE_GRANNY = register("katlin", FEMALE, ELDER, 12, SUMMER, 0xDDDDDD, 0x777777, NPCHolidayStore.class);// Granny of Cafe Owner            (CAFE)
    public static final NPC BLACKSMITH = register("daniel", MALE, ADULT, 1, WINTER, 0x613827, 0x23150E); // Blacksmith                         (BLACKSMITH)
    public static final NPC CLOCKMAKER = register("tiberius", MALE, ADULT, 15, WINTER, 0x305A2E, 0x142419); //The clock worker                 (CLOCKWORKERS)
    public static final NPC CLOCKMAKER_CHILD = register("fenn", MALE, CHILD, 25, SUMMER, 0x228C00, 0x003F00); // Clockmakers Child             (CLOCKWORKERS)
    public static final NPC PRIEST = register("thomas", MALE, ELDER, 9, SUMMER, 0x006666, 0x00B2B20); //Married to mayor                       (CHURCH)
    public static final NPC MAYOR = register("jamie", FEMALE, ELDER, 8, SUMMER, 0xA8AC9A, 0x3B636D); //Married to priest                       (TOWNHALL)
    public static final NPC DAUGHTER_ADULT = register("cloe", FEMALE, ADULT, 3, SPRING, 0xFFFF99, 0xB2B200); //Daughter of Mayor and Priest        (TOWNHALL)
    public static final NPC DAUGHTER_CHILD = register("abi", FEMALE, CHILD, 27, WINTER, 0xFF99FF, 0xFF20FF); //Daughter of Mayor and Priest        (TOWNHALL)

    //Item
    public static final ItemNPCSpawner SPAWNER_NPC = new ItemNPCSpawner().register("spawner_npc");
    public static final ItemNPCTool TOOLS = new ItemNPCTool().register("tool_npc");

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityNPCVillager.class, "villager", EntityIDs.VILLAGER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "builder", EntityIDs.BUILDER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "shopkeeper", EntityIDs.SHOPKEEPER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCGoddess.class, "goddess", EntityIDs.GODDESS, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCTrader.class, "trader", EntityIDs.TRADER, HarvestFestival.instance, 80, 3, true);
        registerSounds("goddess", "blessing");
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(SPAWNER_NPC, new MeshIdentical(SPAWNER_NPC));
        registerNPCRendering(EntityNPCVillager.class);
        registerNPCRendering(EntityNPCBuilder.class);
        registerNPCRendering(EntityNPCShopkeeper.class);
        registerNPCRendering(EntityNPCGoddess.class);
        registerNPCRendering(EntityNPCTrader.class);
    }

    public static void init() {
        GODDESS.setLocation(HOME, GODDESS_POND_FRONT).setHasInfo(new GreetingWeather());
        BARN_OWNER.setLocation(HOME, BARN_INSIDE).setLocation(SHOP, BARN_INSIDE);
        CAFE_OWNER.setLocation(HOME, CAFEBALCONY).setLocation(SHOP, CAFETILL);
        FLOWER_GIRL.setLocation(HOME, CARPENTERUP).setLocation(SHOP, CARPENTERUP).setHasInfo(new GreetingFlowerBuyer());
        DAUGHTER_ADULT.setLocation(HOME, TOWNHALLTEENBED);
        PRIEST.setLocation(HOME, TOWNAHLLADULT);
        CLOCKMAKER_CHILD.setLocation(HOME, CLOCKMAKERUPSTAIRS);
        CAFE_GRANNY.setLocation(HOME, CAFEKITCHEN);
        MAYOR.setLocation(HOME, TOWNHALLSTAGE);
        BUILDER.setLocation(HOME, CARPENTERDOWN).setLocation(SHOP, CARPENTERFRONT).addGreeting(new GreetingCarpenter());
        BLACKSMITH.setLocation(HOME,  BLACKSMITHFURNACE).setLocation(SHOP, BLACKSMITHFURNACE);

        ScheduleBuilder.create(DAUGHTER_CHILD, TOWNHALLCHILDBED)
                        .add(SPRING, MONDAY, 0L, TOWNHALLCHILDBED)
                        .add(SPRING, MONDAY, 8000L, BARN_DOOR)
                        .add(SPRING, MONDAY, 10000L, POULTRY_DOOR)
                        .add(SPRING, MONDAY, 12000L, TOWNHALLLEFT)
                        .add(SPRING, MONDAY, 15000L, TOWNHALLCHILDBED)
                        .add(SPRING, SUNDAY, 0L, TOWNHALLCHILDBED)
                        .add(SPRING, SUNDAY, 8000L, CHURCHPEWFRONTLEFT)
                        .add(SPRING, SUNDAY, 10000L, POULTRY_DOOR)
                        .add(SPRING, SUNDAY, 12000L, BARN_DOOR)
                        .add(SPRING, SUNDAY, 14000L, TOWNHALLLEFT)
                        .add(SPRING, SUNDAY, 15000L, TOWNHALLCHILDBED)
                        .build();


        DAUGHTER_CHILD.setLocation(HOME, TOWNHALLCHILDBED);
        CLOCKMAKER.setLocation(HOME, CLOCKMAKERDOWNSTAIRS).setHasInfo(new GreetingTime());
        GS_OWNER.setLocation(HOME, GENERALBEDROOM).setLocation(SHOP, GENERALTILL).setHasInfo(new GreetingSupermarket(GS_OWNER.getRegistryName()));
        FISHERMAN.setLocation(HOME, FISHING_HUT_UPSTAIRS).setLocation(SHOP, FISHING_HUT_DOWNSTAIRS).addGreeting(new GreetingLocation(FISHING_POND_PIER));
        MILKMAID.setLocation(HOME, GENERALBED).setLocation(SHOP, BARN_LEFT_PEN);
        POULTRY.setLocation(HOME, POULTRY_CENTRE).setLocation(SHOP, POULTRY_CENTRE);
        TRADER.setLocation(HOME, TOWNHALLRIGHT).setLocation(SHOP, GENERALCUSTOMER);
        CLOCKMAKER.setHasInfo(new GreetingTime());
        for (NPC npc: NPCRegistry.REGISTRY) {
            if (npc != NPC.NULL_NPC) {
                setupGifts(npc);
                setupSchedules(npc);
            }
        }
    }

    private static void setupGifts(NPC npc) {
        npc.setGiftHandler(new IGiftHandler() {});
        if (npc.getRegistryName().getResourceDomain().equals(MODID)) {
            try {
                IGiftHandler handler = (IGiftHandler) Class.forName(GIFTPATH + WordUtils.capitalize(npc.getRegistryName().getResourcePath())).newInstance();
                if (handler != null) npc.setGiftHandler(handler);
            } catch (Exception e) { /**/}
        }
    }

    private static void setupSchedules(NPC npc) {
        if (npc.getScheduler() == null) npc.setScheduleHandler(new ISchedule() {});
        if (npc.getRegistryName().getResourceDomain().equals(MODID)) {
            try {
                ISchedule schedule = (ISchedule) Class.forName(SCHEDULEPATH + WordUtils.capitalize(npc.getRegistryName().getResourcePath())).newInstance();
                if (schedule != null) npc.setScheduleHandler(schedule);
            } catch (Exception e) {}
        }
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

    @SuppressWarnings("unchecked")
    private static <N extends NPC> N register(String name, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor, Class<N>... clazzes) {
        Class<N> clazz = clazzes.length == 1 ? clazzes[0] : null;
        NPC npc = null;
        try {
            if (clazz != null) npc = clazz.getConstructor(ResourceLocation.class, Gender.class, Age.class, CalendarDate.class, int.class, int.class)
                                .newInstance(new ResourceLocation("harvestfestival", name), gender, age, new CalendarDate(dayOfBirth, seasonOfBirth, 1), insideColor, outsideColor);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) { /**/}

        if (npc == null) npc = new NPC(new ResourceLocation(MODID, name), gender, age, new CalendarDate(dayOfBirth, seasonOfBirth, 1), insideColor, outsideColor);
        return (N) npc;
    }

    private static <E extends EntityNPC> void registerNPCRendering(Class<E> entityClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, RenderNPC :: new);
    }

    //Configure
    public static double TOWN_DISTANCE;

    public static void configure() {
        TOWN_DISTANCE = getDouble("Distance between towns", 256D);
    }
}