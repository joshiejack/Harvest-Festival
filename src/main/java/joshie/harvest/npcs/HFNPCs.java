package joshie.harvest.npcs;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.npcs.entity.*;
import joshie.harvest.npcs.greeting.*;
import joshie.harvest.npcs.item.ItemNPCSpawner;
import joshie.harvest.npcs.item.ItemNPCTool;
import joshie.harvest.npcs.npc.*;
import joshie.harvest.npcs.render.NPCItemRenderer;
import joshie.harvest.npcs.render.NPCItemRenderer.NPCTile;
import joshie.harvest.npcs.render.RenderNPC;
import joshie.harvest.quests.Quests;
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
import static joshie.harvest.api.npc.INPCHelper.Age.*;
import static joshie.harvest.api.npc.INPCHelper.Gender.FEMALE;
import static joshie.harvest.api.npc.INPCHelper.Gender.MALE;
import static joshie.harvest.core.helpers.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.GIFTPATH;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFNPCS;
import static joshie.harvest.town.BuildingLocations.FISHING_POND_PIER;

@HFLoader(priority = HFNPCS)
@SuppressWarnings("unchecked")
public class HFNPCs {
    public static final NPC GODDESS = register("goddess", FEMALE, ADULT, 8, SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.6F).setUninvitable();
    public static final NPCSpecialSeller CARPENTER = register("yulif", MALE, ADULT, 19, SUMMER, 0x313857, 0x121421, NPCSpecialSeller.class);
    public static final NPCSpecialSeller FLOWER_GIRL = register("jade", FEMALE, ADULT, 14, SPRING, 0x653081, 0x361840, NPCSpecialSeller.class);
    public static final NPCSpecialSeller GS_OWNER = register("jenni", FEMALE, ADULT, 7, WINTER, 0xDDD0AD, 0xE79043, NPCSpecialOpener.class).setNPC(GODDESS);
    public static final NPC MILKMAID = register("candice", FEMALE, ADULT, 5, AUTUMN, 0xF65FAB, 0xF21985, NPCHolidayStore.class);
    public static final NPC BARN_OWNER = register("jim", MALE, ADULT, 26, SPRING, 0xDE7245, 0x722B19, NPCHolidayStore.class);
    public static final NPC POULTRY = register("ashlee", FEMALE, ADULT, 16, AUTUMN, 0xC62D2D, 0x571111);
    public static final NPC TRADER = register("girafi", MALE, ADULT, 2, AUTUMN,  0xFFFFFF, 0xC60C30).setUninvitable();
    public static final NPCSpecialSeller FISHERMAN = register("jacob", MALE, ADULT, 28, AUTUMN, 0x7396FF, 0x0036D9, NPCSpecialSeller.class);
    public static final NPCSpecialSeller MINER = register("brandon", MALE, ADULT, 13, AUTUMN, 0xC28D48, 0x5F5247, NPCSpecialSeller.class);
    public static final NPCSpecialSeller CAFE_OWNER = register("liara", FEMALE, ADULT, 17, SPRING, 0xBEC8EE, 0x8091D0, NPCHolidayStoreSpecial.class);
    public static final NPC CAFE_GRANNY = register("katlin", FEMALE, ELDER, 12, SUMMER, 0xDDDDDD, 0x777777, NPCHolidayStore.class);
    public static final NPC BLACKSMITH = register("daniel", MALE, ADULT, 1, WINTER, 0x613827, 0x23150E);
    public static final NPC CLOCKMAKER = register("tiberius", MALE, ADULT, 15, WINTER, 0x305A2E, 0x142419, NPCClockmaker.class);
    public static final NPC CLOCKMAKER_CHILD = register("fenn", MALE, CHILD, 25, SUMMER, 0x228C00, 0x003F00);
    public static final NPC PRIEST = register("thomas", MALE, ELDER, 9, SUMMER, 0x006666, 0x00B2B20);
    public static final NPC MAYOR = register("jamie", FEMALE, ELDER, 8, SUMMER, 0xA8AC9A, 0x3B636D);
    public static final NPC DAUGHTER_ADULT = register("cloe", FEMALE, ADULT, 3, SPRING, 0xFFFF99, 0xB2B200);
    public static final NPC DAUGHTER_CHILD = register("abi", FEMALE, CHILD, 27, WINTER, 0xFF99FF, 0xFF20FF);

    //Item
    public static final ItemNPCSpawner SPAWNER_NPC = new ItemNPCSpawner().register("spawner_npc");
    public static final ItemNPCTool TOOLS = new ItemNPCTool().register("tool_npc");

    @SuppressWarnings("deprecation")
    public static void preInit() {
        CARPENTER.addFamily(FLOWER_GIRL);
        FLOWER_GIRL.addFamily(CARPENTER);
        GS_OWNER.addFamily(MILKMAID);
        MILKMAID.addFamily(GS_OWNER);
        BARN_OWNER.addFamily(FISHERMAN);
        POULTRY.addFamily(BLACKSMITH);
        FISHERMAN.addFamily(BARN_OWNER, POULTRY);
        CAFE_OWNER.addFamily(CAFE_GRANNY);
        CLOCKMAKER.addFamily(CLOCKMAKER_CHILD);
        BLACKSMITH.addFamily(POULTRY);
        DAUGHTER_ADULT.addFamily(PRIEST, MAYOR, DAUGHTER_CHILD);
        EntityRegistry.registerModEntity(EntityNPCVillager.class, "villager", EntityIDs.VILLAGER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "builder", EntityIDs.BUILDER, HarvestFestival.instance, 80, 3, true);
        //TODO: Remove in 0.7+
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "shopkeeper", EntityIDs.SHOPKEEPER, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCGoddess.class, "goddess", EntityIDs.GODDESS, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCMiner.class, "miner", EntityIDs.MINER, HarvestFestival.instance, 80, 3, true);
        registerSounds("goddess", "blessing");
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(SPAWNER_NPC, new MeshIdentical(SPAWNER_NPC));
        registerNPCRendering(EntityNPCVillager.class);
        registerNPCRendering(EntityNPCBuilder.class);
        //TODO: Remove in 0.7+
        registerNPCRendering(EntityNPCShopkeeper.class);
        registerNPCRendering(EntityNPCGoddess.class);
        registerNPCRendering(EntityNPCMiner.class);
    }

    public static void init() {
        GODDESS.setHasInfo(new GreetingWeather())
                .addGreeting(new GreetingBeforeAshlee("tutorial.chicken.reminder.poultry"))
                .addGreeting(new GreetingBeforeDanieru(GODDESS));
        CARPENTER.setQuest(Quests.SELL_SPRINKLER)
                .addGreeting(new GreetingCarpenter())
                .addGreeting(new GreetingBeforeJim("tutorial.cow.reminder.barn"))
                .addGreeting(new GreetingBeforeDanieru(CARPENTER));
        FLOWER_GIRL.setQuest(Quests.FLOWER_BUYER)
                .setHasInfo(new GreetingFlowerBuyer())
                .addGreeting(new GreetingBeforeJenni("trade.seeds.reminder"))
                .addGreeting(new GreetingBeforeJenni("trade.tools.reminder"))
                .addGreeting(new GreetingBeforeJenni("tutorial.supermarket.reminder.supermarket"))
                .addGreeting(new GreetingBeforeDanieru(FLOWER_GIRL));
        GS_OWNER.setHasInfo(new GreetingSupermarket(GS_OWNER))
                .addGreeting(new GreetingBeforeDanieru(GS_OWNER));
        MILKMAID.addGreeting(new GreetingBeforeDanieru(MILKMAID));
        BARN_OWNER.addGreeting(new GreetingBeforeDanieru(BARN_OWNER));
        MINER.setQuest(Quests.SELL_ORES).setUninvitable();
        FISHERMAN.setQuest(Quests.SELL_HATCHERY).addGreeting(new GreetingLocation(FISHING_POND_PIER));
        CAFE_OWNER.setQuest(Quests.SELL_MEALS);
        CLOCKMAKER.setHasInfo(new GreetingTime());
        PRIEST.addGreeting(new GreetingPriestBlessing());

        NPC.REGISTRY.values().stream().forEachOrdered(npc -> {
            addHolidayGreetings(npc, HFFestivals.NEW_YEARS, HFFestivals.COOKING_CONTEST, HFFestivals.CHICKEN_FESTIVAL, HFFestivals.COW_FESTIVAL,
                    HFFestivals.HARVEST_FESTIVAL, HFFestivals.SHEEP_FESTIVAL, HFFestivals.STARRY_NIGHT, HFFestivals.NEW_YEARS_EVE);
            setupGifts(npc);
        });
    }

    private static void addHolidayGreetings(NPC npc, Festival... festivals) {
        for (Festival festival : festivals) npc.addGreeting(new GreetingFestival(festival));
    }

    private static void setupGifts(NPC npc) {
        npc.setGiftHandler(new IGiftHandler() {});
        if (npc.getResource().getResourceDomain().equals(MODID)) {
            try {
                IGiftHandler handler = (IGiftHandler) Class.forName(GIFTPATH + WordUtils.capitalize(npc.getResource().getResourcePath())).newInstance();
                if (handler != null) npc.setGiftHandler(handler);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {/**/}
        }
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        HFClientProxy.RENDER_MAP.put(SPAWNER_NPC, NPCTile.INSTANCE);
        ClientRegistry.bindTileEntitySpecialRenderer(NPCTile.class, new NPCItemRenderer());
        for (NPC npc: NPC.REGISTRY.values()) {
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

        if (npc == null) {
            npc = new NPC(new ResourceLocation(MODID, name), gender, age, new CalendarDate(dayOfBirth, seasonOfBirth, 1), insideColor, outsideColor);
            npc.setRegistryName(new ResourceLocation(MODID, name));
            NPCHelper.OLD_REGISTRY.register(npc);
        }
        return (N) npc;
    }

    private static <E extends EntityNPC> void registerNPCRendering(Class<E> entityClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, RenderNPC :: new);
    }

    //Configure
    public static double TOWN_DISTANCE;
    public static double NPC_AI_DISTANCE;

    public static void configure() {
        TOWN_DISTANCE = getDouble("Distance between towns", 256D);
        NPC_AI_DISTANCE = getDouble("AI Range", 32D);
    }
}