package joshie.harvest.npcs;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.api.npc.schedule.ScheduleBuilder;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.proxy.HFClientProxy;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.calendar.HFFestivals;
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
import static joshie.harvest.api.calendar.Weekday.*;
import static joshie.harvest.api.npc.INPCHelper.Age.*;
import static joshie.harvest.api.npc.INPCHelper.Gender.FEMALE;
import static joshie.harvest.api.npc.INPCHelper.Gender.MALE;
import static joshie.harvest.calendar.HFFestivals.NEW_YEARS_EVE;
import static joshie.harvest.core.helpers.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.GIFTPATH;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFNPCS;
import static joshie.harvest.calendar.HFFestivals.COOKING_CONTEST;
import static joshie.harvest.calendar.HFFestivals.NEW_YEARS;
import static joshie.harvest.town.BuildingLocations.*;

@HFLoader(priority = HFNPCS)
@SuppressWarnings("unchecked")
public class HFNPCs {
    public static final NPC GODDESS = register("goddess", FEMALE, ADULT, 8, SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.6F); //The Goddess                        (SPAWN)
    public static final NPC CARPENTER = register("yulif", MALE, ADULT, 19, SUMMER, 0x313857, 0x121421); //Builds stuff            (SPAWN)
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
        GODDESS.setHasInfo(new GreetingWeather())
                .addGreeting(new GreetingBeforeAshlee("tutorial.chicken.reminder.poultry"))
                .addGreeting(new GreetingBeforeDanieru(GODDESS));
        ScheduleBuilder.create(GODDESS, null).build();

        //Carpenter
        CARPENTER.addGreeting(new GreetingCarpenter())
                .addGreeting(new GreetingBeforeJim("tutorial.cow.reminder.barn"))
                .addGreeting(new GreetingBeforeDanieru(CARPENTER));
        ScheduleBuilder.create(CARPENTER, CARPENTER_DOWNSTAIRS)
                        .add(SPRING, SUNDAY, 0L, CARPENTER_DOWNSTAIRS)
                        .add(SPRING, SUNDAY, 8000L, CARPENTER_FRONT)
                        .add(SPRING, SUNDAY, 17000L, CARPENTER_DOWNSTAIRS)
                        .add(SPRING, SUNDAY, 21000L, CARPENTER_FRONT)
                        .add(COOKING_CONTEST, 0L, CARPENTER_UPSTAIRS)
                        .add(COOKING_CONTEST, 9000L, PARK_BENCH)
                        .add(COOKING_CONTEST, 20000L, CARPENTER_UPSTAIRS)
                        .add(NEW_YEARS, 0L, CARPENTER_DOWNSTAIRS)
                        .add(NEW_YEARS, 10000L, CARPENTER_FRONT)
                        .add(NEW_YEARS, 11500L, PARK_TRADER)
                        .add(NEW_YEARS, 22500L, CARPENTER_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 0L, CARPENTER_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 13000L, CARPENTER_FRONT)
                        .add(NEW_YEARS_EVE, 17000L, PARK_NOODLES_STAND)
                        .build();

        //Flower Girl, Add the flower buying greeting, and before complettion of meeting jenni
        FLOWER_GIRL.setHasInfo(new GreetingFlowerBuyer())
                .addGreeting(new GreetingBeforeJenni("trade.seeds.reminder"))
                .addGreeting(new GreetingBeforeJenni("trade.tools.reminder"))
                .addGreeting(new GreetingBeforeJenni("tutorial.supermarket.reminder.supermarket"))
                .addGreeting(new GreetingBeforeDanieru(FLOWER_GIRL));
        ScheduleBuilder.create(FLOWER_GIRL, CARPENTER_UPSTAIRS)
                        .add(SPRING, SUNDAY, 0L, CARPENTER_UPSTAIRS)
                        .add(SPRING, SUNDAY, 5000L, TOWNHALL_TEEN)
                        .add(SPRING, SUNDAY, 8000L, CARPENTER_UPSTAIRS)
                        .add(SPRING, SUNDAY, 17000L, PARK_BACK_LEFT)
                        .add(SPRING, SUNDAY, 19000L, CARPENTER_FRONT)
                        .add(SPRING, SUNDAY, 21000L, CARPENTER_UPSTAIRS)
                        .add(COOKING_CONTEST, 0L, CARPENTER_UPSTAIRS)
                        .add(COOKING_CONTEST, 6000L, PARK_CENTRE)
                        .add(COOKING_CONTEST, 17000L, CARPENTER_UPSTAIRS)
                        .add(NEW_YEARS, 0L, CARPENTER_UPSTAIRS)
                        .add(NEW_YEARS, 10000L, CARPENTER_DOWNSTAIRS)
                        .add(NEW_YEARS, 11500L, PARK_TRADER_LEFT)
                        .add(NEW_YEARS, 22500L, CARPENTER_DOWNSTAIRS)
                        .add(NEW_YEARS, 23500L, CARPENTER_UPSTAIRS)
                        .add(NEW_YEARS_EVE, 0L, CARPENTER_UPSTAIRS)
                        .add(NEW_YEARS_EVE, 13000L, CARPENTER_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 17000L, PARK_STAGE_LEFT)
                        .build();

        //General Store
        GS_OWNER.setHasInfo(new GreetingSupermarket(GS_OWNER.getRegistryName()))
                .addGreeting(new GreetingBeforeDanieru(GS_OWNER));
        ScheduleBuilder.create(GS_OWNER, GENERAL_BEDROOM)
                        .add(SPRING, SUNDAY, 0L, GENERAL_BEDROOM)
                        .add(SPRING, SUNDAY, 8000L, GENERAL_BEDROOM)
                        .add(SPRING, SUNDAY, 10000L, PARK_CENTRE)
                        .add(SPRING, SUNDAY, 15000L, CAFE_FRONT)
                        .add(SPRING, SUNDAY, 17000L, GENERAL_STORE_FRONT)
                        .add(SPRING, SUNDAY, 19000L, CAFE_KITCHEN)
                        .add(SPRING, SUNDAY, 22000L, GENERAL_BED)
                        .add(SPRING, MONDAY, 0L, GENERAL_BEDROOM)
                        .add(SPRING, MONDAY, 7500L, GENERAL_TILL)
                        .add(SPRING, MONDAY, 17000L, GENERAL_BEDROOM)
                        .add(SPRING, MONDAY, 19000L, CAFE_KITCHEN)
                        .add(SPRING, MONDAY, 22000L, FISHING_POND_BACK)
                        .add(SPRING, SATURDAY, 0L, GENERAL_BEDROOM)
                        .add(SPRING, SATURDAY, 10000L, GENERAL_TILL)
                        .add(SPRING, SATURDAY, 16000L, CAFE_CUSTOMER)
                        .add(SPRING, SATURDAY, 19000L, CAFE_KITCHEN)
                        .add(SPRING, SATURDAY, 22000L, FISHING_POND_BACK)
                        .add(COOKING_CONTEST, 0L, GENERAL_BEDROOM)
                        .add(COOKING_CONTEST, 4500L, PARK_PODIUM)
                        .add(COOKING_CONTEST, 18000L, GENERAL_BEDROOM)
                        .add(COOKING_CONTEST, 20000L, FISHING_POND_BACK)
                        .add(NEW_YEARS, 0L, GENERAL_BEDROOM)
                        .add(NEW_YEARS, 7000L, GENERAL_GARDEN)
                        .add(NEW_YEARS, 12000L, PARK_OAK)
                        .add(NEW_YEARS, 20000L, GENERAL_GARDEN)
                        .add(NEW_YEARS, 22000L, GENERAL_BEDROOM)
                        .add(NEW_YEARS_EVE, 0L, GENERAL_BEDROOM)
                        .add(NEW_YEARS_EVE, 13000L, GENERAL_GARDEN)
                        .add(NEW_YEARS_EVE, 17000L, PARK_OAK)
                        .build();

        //Milkmaid
        MILKMAID.addGreeting(new GreetingBeforeDanieru(MILKMAID));
        ScheduleBuilder.create(MILKMAID, GENERAL_BED)
                        .add(SPRING, SUNDAY, 0L, GENERAL_BED)
                        .add(SPRING, SUNDAY, 6000L, CHURCH_LEFT)
                        .add(SPRING, SUNDAY, 16000L, PARK_CENTRE)
                        .add(SPRING, SUNDAY, 20000L, FISHING_POND_BACK)
                        .add(SPRING, SUNDAY, 24000L, GENERAL_BED)
                        .add(SPRING, MONDAY, 0L, GENERAL_BED)
                        .add(SPRING, MONDAY, 8000L, BARN_LEFT_PEN)
                        .add(SPRING, MONDAY, 10000L, BARN_RIGHT_PEN)
                        .add(SPRING, MONDAY, 15000L, BARN_DOOR)
                        .add(SPRING, MONDAY, 17000L, FISHER_LEFT)
                        .add(SPRING, MONDAY, 20000L, FISHING_POND_BACK)
                        .add(SPRING, MONDAY, 24000L, GENERAL_BED)
                        .add(COOKING_CONTEST, 0L, GENERAL_BED)
                        .add(COOKING_CONTEST, 6000L, PARK_TABLE)
                        .add(COOKING_CONTEST, 16000L, PARK_CUSTOMER)
                        .add(COOKING_CONTEST, 18000L, GENERAL_BED)
                        .add(COOKING_CONTEST, 20000L, FISHING_POND_BACK)
                        .add(COOKING_CONTEST, 24000L, GENERAL_BED)
                        .add(NEW_YEARS, 0L, GENERAL_BED)
                        .add(NEW_YEARS, 7000L, GENERAL_GARDEN)
                        .add(NEW_YEARS, 12000L, PARK_OAK)
                        .add(NEW_YEARS, 20000L, GENERAL_GARDEN)
                        .add(NEW_YEARS, 22000L, GENERAL_BED)
                        .add(NEW_YEARS_EVE, 0L, GENERAL_BED)
                        .add(NEW_YEARS_EVE, 13000L, GENERAL_GARDEN)
                        .add(NEW_YEARS_EVE, 17000L, PARK_OAK)
                        .build();

        //Barn Owner
        BARN_OWNER.addGreeting(new GreetingBeforeDanieru(BARN_OWNER));
        ScheduleBuilder.create(BARN_OWNER, BARN_INSIDE)
                        .add(SPRING, SUNDAY, 0L, BARN_INSIDE)
                        .add(SPRING, SUNDAY, 10000L, GENERAL_BEDROOM)
                        .add(SPRING, SUNDAY, 13000L, CAFE_FRONT)
                        .add(SPRING, SUNDAY, 16000L, PARK_CENTRE)
                        .add(SPRING, SUNDAY, 19000L, GENERAL_BEDROOM)
                        .add(SPRING, SUNDAY, 21000L, BARN_INSIDE)
                        .add(SPRING, MONDAY, 0L, BARN_INSIDE)
                        .add(SPRING, MONDAY, 15000L, GENERAL_CUSTOMER)
                        .add(SPRING, MONDAY, 17000L, PARK_CENTRE)
                        .add(SPRING, MONDAY, 19000L, GENERAL_BEDROOM)
                        .add(SPRING, MONDAY, 22000L, BARN_INSIDE)
                        .add(COOKING_CONTEST, 0L, BARN_INSIDE)
                        .add(COOKING_CONTEST, 10000L, BARN_INSIDE)
                        .add(COOKING_CONTEST, 17000L, BARN_DOOR)
                        .add(COOKING_CONTEST, 19000L, GENERAL_BEDROOM)
                        .add(COOKING_CONTEST, 23500L, BARN_INSIDE)
                        .add(NEW_YEARS_EVE, 0L, BARN_INSIDE)
                        .add(NEW_YEARS_EVE, 13000L, BARN_DOOR)
                        .add(NEW_YEARS_EVE, 17000L, PARK_LEFT)
                        .build();

        //Miner
        ScheduleBuilder.create(MINER, null).build();

        //Poultry Farm Owner
        ScheduleBuilder.create(POULTRY, POULTRY_CENTRE)
                        .add(SPRING, SUNDAY, 0L, POULTRY_CENTRE)
                        .add(SPRING, SATURDAY, 4500L, CARPENTER_FRONT)
                        .add(SPRING, SUNDAY, 6000L, CARPENTER_FRONT)
                        .add(SPRING, SUNDAY, 7000L, CHURCH_RIGHT)
                        .add(SPRING, SUNDAY, 12000L, POULTRY_CENTRE)
                        .add(SPRING, SUNDAY, 15000L, PARK_BENCH)
                        .add(SPRING, SUNDAY, 18000L, POULTRY_CENTRE)
                        .add(SPRING, MONDAY, 0L, POULTRY_CENTRE)
                        .add(SPRING, MONDAY, 6000L, POULTRY_CENTRE)
                        .add(SPRING, MONDAY, 12000L, GENERAL_STORE_FRONT)
                        .add(SPRING, MONDAY, 15000L, PARK_BENCH)
                        .add(SPRING, MONDAY, 18000L, POULTRY_CENTRE)
                        .add(COOKING_CONTEST, 0L, POULTRY_CENTRE)
                        .add(COOKING_CONTEST, 6000L, PARK_BENCH)
                        .add(COOKING_CONTEST, 11000L, POULTRY_DOOR)
                        .add(COOKING_CONTEST, 13000L, POULTRY_CENTRE)
                        .add(NEW_YEARS_EVE, 0L, POULTRY_CENTRE)
                        .add(NEW_YEARS_EVE, 13000L, POULTRY_DOOR)
                        .add(NEW_YEARS_EVE, 17000L, PARK_LEFT)
                        .build();

        //Fisherman
        FISHERMAN.addGreeting(new GreetingLocation(FISHING_POND_PIER));
        ScheduleBuilder.create(FISHERMAN, FISHING_HUT_UPSTAIRS)
                        .add(SPRING, SUNDAY, 0L, FISHING_HUT_UPSTAIRS)
                        .add(SPRING, SATURDAY, 5000L, FISHING_POND_PIER)
                        .add(SPRING, SUNDAY, 6000L, FISHING_POND_PIER)
                        .add(SPRING, SUNDAY, 9000L, GODDESS_POND_FRONT)
                        .add(SPRING, SUNDAY, 12000L, FISHING_HUT_DOWNSTAIRS)
                        .add(SPRING, SUNDAY, 15000L, FISHING_POND_PIER)
                        .add(SPRING, SUNDAY, 18000L, FISHING_HUT_UPSTAIRS)
                        .add(SPRING, TUESDAY, 0L, FISHING_HUT_UPSTAIRS)
                        .add(SPRING, TUESDAY, 6000L, FISHING_POND_PIER)
                        .add(SPRING, TUESDAY, 10000L, GODDESS_POND_FRONT)
                        .add(SPRING, TUESDAY, 12000L, FISHING_HUT_DOWNSTAIRS)
                        .add(SPRING, TUESDAY, 19000L, FISHING_POND_PIER)
                        .add(SPRING, TUESDAY, 22000L, FISHING_HUT_UPSTAIRS)
                        .add(SPRING, SATURDAY, 0L, FISHING_HUT_UPSTAIRS)
                        .add(SPRING, SATURDAY, 6000L, GODDESS_POND_FRONT)
                        .add(SPRING, SATURDAY, 8000L, FISHING_POND_PIER)
                        .add(SPRING, SATURDAY, 13000L, FISHING_HUT_DOWNSTAIRS)
                        .add(SPRING, SATURDAY, 15000L, FISHING_POND_PIER)
                        .add(SPRING, SATURDAY, 18000L, FISHING_HUT_DOWNSTAIRS)
                        .add(SPRING, SATURDAY, 19500L, FISHING_POND_PIER)
                        .add(SPRING, SATURDAY, 22000L, FISHING_HUT_UPSTAIRS)
                        .add(COOKING_CONTEST, 0L, FISHING_HUT_UPSTAIRS)
                        .add(COOKING_CONTEST, 6000L, GODDESS_POND_FRONT)
                        .add(COOKING_CONTEST, 7000L, FISHING_POND_PIER)
                        .add(COOKING_CONTEST, 13000L, PARK_LEFT)
                        .add(COOKING_CONTEST, 16000L, PARK_CUSTOMER)
                        .add(COOKING_CONTEST, 17500L, FISHING_POND_PIER)
                        .add(COOKING_CONTEST, 20000L, FISHING_HUT_DOWNSTAIRS)
                        .add(COOKING_CONTEST, 22000L, FISHING_HUT_UPSTAIRS)
                        .add(NEW_YEARS_EVE, 0L, FISHING_HUT_UPSTAIRS)
                        .add(NEW_YEARS_EVE, 13000L, FISHING_HUT_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 17000L, PARK_BOTTOM)
                        .build();

        //Cafe Owner
        ScheduleBuilder.create(CAFE_OWNER, CAFE_BALCONY)
                        .add(SPRING, SUNDAY, 0L, CAFE_BALCONY)
                        .add(SPRING, SATURDAY, 5000L, GODDESS_POND_FRONT_LEFT)
                        .add(SPRING, SUNDAY, 6000L, GODDESS_POND_FRONT_LEFT)
                        .add(SPRING, SUNDAY, 7000L, CHURCH_INSIDE)
                        .add(SPRING, SUNDAY, 8500L, CAFE_TILL)
                        .add(SPRING, SUNDAY, 17000L, PARK_TABLE)
                        .add(SPRING, SUNDAY, 20000L, CAFE_BALCONY)
                        .add(SPRING, MONDAY, 0L, CAFE_BALCONY)
                        .add(SPRING, MONDAY, 8000L, CAFE_TILL)
                        .add(SPRING, MONDAY, 17000L, CAFE_CUSTOMER)
                        .add(SPRING, MONDAY, 18000L, CAFE_FRONT)
                        .add(SPRING, MONDAY, 19000L, PARK_TABLE)
                        .add(SPRING, MONDAY, 22000L, CAFE_BALCONY)
                        .add(SPRING, SATURDAY, 0L, CAFE_BALCONY)
                        .add(SPRING, SATURDAY, 6000L, GODDESS_POND_FRONT_LEFT)
                        .add(SPRING, SATURDAY, 8000L, CAFE_TILL)
                        .add(SPRING, SATURDAY, 17000L, CAFE_CUSTOMER)
                        .add(SPRING, SATURDAY, 18000L, CAFE_FRONT)
                        .add(SPRING, SATURDAY, 19000L, PARK_TABLE)
                        .add(SPRING, SATURDAY, 22000L, CAFE_BALCONY)
                        .add(COOKING_CONTEST, 0L, CAFE_BALCONY)
                        .add(COOKING_CONTEST, 5000L, PARK_STALL)
                        .add(COOKING_CONTEST, 18000L, CAFE_BALCONY)
                        .add(NEW_YEARS_EVE, 0L, CAFE_BALCONY)
                        .add(NEW_YEARS_EVE, 13000L, CAFE_FRONT)
                        .add(NEW_YEARS_EVE, 17000L, PARK_SPRUCE)
                        .build();

        //Cafe Granny
        ScheduleBuilder.create(CAFE_GRANNY, CAFE_KITCHEN)
                        .add(SPRING, SUNDAY, 0L, CAFE_KITCHEN)
                        .add(SPRING, SATURDAY, 5000L, CHURCH_PEW_CENTRE)
                        .add(SPRING, SUNDAY, 17000L, FISHING_POND_RIGHT)
                        .add(SPRING, SUNDAY, 20000L, CAFE_KITCHEN)
                        .add(SPRING, MONDAY, 0L, CAFE_KITCHEN)
                        .add(SPRING, MONDAY, 5000L, GODDESS_POND_FRONT_LEFT)
                        .add(SPRING, MONDAY, 6500L, CAFE_FRONT)
                        .add(SPRING, MONDAY, 17000L, FISHING_POND_RIGHT)
                        .add(SPRING, MONDAY, 19000L, CAFE_KITCHEN)
                        .add(SPRING, FRIDAY, 0L, CAFE_KITCHEN)
                        .add(SPRING, FRIDAY, 9500L, GODDESS_POND_FRONT_LEFT)
                        .add(SPRING, FRIDAY, 14000L, CAFE_FRONT)
                        .add(SPRING, FRIDAY, 16000L, CAFE_KITCHEN)
                        .add(SPRING, FRIDAY, 20000L, FISHING_POND_RIGHT)
                        .add(SPRING, FRIDAY, 22000L, CAFE_KITCHEN)
                        .add(SPRING, SATURDAY, 0L, CAFE_KITCHEN)
                        .add(SPRING, SATURDAY, 6000L, GODDESS_POND_FRONT_LEFT)
                        .add(SPRING, SATURDAY, 10000L, CAFE_FRONT)
                        .add(SPRING, SATURDAY, 15000L, CAFE_KITCHEN)
                        .add(SPRING, SATURDAY, 17000L, FISHING_POND_RIGHT)
                        .add(SPRING, SATURDAY, 19000L, CAFE_KITCHEN)
                        .add(COOKING_CONTEST, 0L, CAFE_KITCHEN)
                        .add(COOKING_CONTEST, 5000L, PARK_CAFE)
                        .add(COOKING_CONTEST, 18000L, CAFE_KITCHEN)
                        .add(NEW_YEARS_EVE, 0L, CAFE_KITCHEN)
                        .add(NEW_YEARS_EVE, 13000L, GODDESS_POND_FRONT_RIGHT)
                        .add(NEW_YEARS_EVE, 17000L, PARK_SPRUCE)
                        .build();

        //Blacksmith
        ScheduleBuilder.create(BLACKSMITH, BLACKSMITH_FURNACE)
                        .add(SPRING, SUNDAY, 0L, BLACKSMITH_FURNACE)
                        .add(SPRING, SUNDAY, 6000L, CAFE_DOOR)
                        .add(SPRING, SUNDAY, 8000L, BLACKSMITH_FRONT)
                        .add(SPRING, SUNDAY, 9500L, BLACKSMITH_FURNACE)
                        .add(SPRING, SUNDAY, 17000L, TOWNHALL_ENTRANCE)
                        .add(SPRING, SUNDAY, 20000L, PARK_OAK)
                        .add(SPRING, SUNDAY, 23000L, BLACKSMITH_FURNACE)
                        .add(SPRING, THURSDAY, 0L, BLACKSMITH_FURNACE)
                        .add(SPRING, THURSDAY, 6000L, CAFE_DOOR)
                        .add(SPRING, THURSDAY, 10000L, CAFE_STAIRS)
                        .add(SPRING, THURSDAY, 13000L, MINE_RIGHT)
                        .add(SPRING, THURSDAY, 16000L, CAFE_DOOR)
                        .add(SPRING, THURSDAY, 18000L, BLACKMSITH_DOOR)
                        .add(SPRING, THURSDAY, 20000L, PARK_OAK)
                        .add(SPRING, THURSDAY, 23000L, BLACKSMITH_FURNACE)
                        .add(SPRING, FRIDAY, 0L, BLACKSMITH_FURNACE)
                        .add(SPRING, FRIDAY, 6000L, CAFE_DOOR)
                        .add(SPRING, FRIDAY, 8000L, BLACKSMITH_FRONT)
                        .add(SPRING, FRIDAY, 9500L, BLACKSMITH_FURNACE)
                        .add(SPRING, FRIDAY, 17000L, TOWNHALL_ENTRANCE)
                        .add(SPRING, FRIDAY, 20000L, PARK_OAK)
                        .add(SPRING, FRIDAY, 23000L, BLACKSMITH_FURNACE)
                        .add(NEW_YEARS_EVE, 0L, BLACKSMITH_FURNACE)
                        .add(NEW_YEARS_EVE, 13000L, BLACKSMITH_FRONT)
                        .add(NEW_YEARS_EVE, 17000L, PARK_BUSH)
                        .build();

        //Clockmaker
        CLOCKMAKER.setHasInfo(new GreetingTime());
        ScheduleBuilder.create(CLOCKMAKER, CLOCKMAKER_DOWNSTAIRS)
                        .add(SPRING, SUNDAY, 0L, CLOCKMAKER_DOWNSTAIRS)
                        .add(SPRING, SUNDAY, 8000L, CHURCH_PEW_BACK_RIGHT)
                        .add(SPRING, SUNDAY, 11000L, TOWNHALL_RIGHT)
                        .add(SPRING, SUNDAY, 14000L, GODDESS_POND_BACK)
                        .add(SPRING, SUNDAY, 16000L, PARK_LEFT)
                        .add(SPRING, SUNDAY, 18000L, CLOCKMAKER_DOWNSTAIRS)
                        .add(SPRING, MONDAY, 0L, CLOCKMAKER_DOWNSTAIRS)
                        .add(SPRING, MONDAY, 8000L, CAFE_CUSTOMER)
                        .add(SPRING, MONDAY, 11000L, TOWNHALL_RIGHT_OF_STAGE)
                        .add(SPRING, MONDAY, 14000L, FISHING_POND_LEFT)
                        .add(SPRING, MONDAY, 16000L, PARK_LEFT)
                        .add(SPRING, MONDAY, 18000L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS, 0L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS, 7000L, CLOCKMAKER_DOOR)
                        .add(NEW_YEARS, 12000L, PARK_SPRUCE)
                        .add(NEW_YEARS, 20000L, CLOCKMAKER_DOOR)
                        .add(NEW_YEARS, 22000L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 0L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 13000L, CLOCKMAKER_DOOR)
                        .add(NEW_YEARS_EVE, 17000L, PARK_BACK_LEFT)
                        .build();

        //Clockmaker Child
        ScheduleBuilder.create(CLOCKMAKER_CHILD, CLOCKMAKER_UPSTAIRS)
                        .add(SPRING, SUNDAY, 0L, CLOCKMAKER_UPSTAIRS)
                        .add(SPRING, SUNDAY, 8000L, CHURCH_PEW_BACK_RIGHT)
                        .add(SPRING, SUNDAY, 10000L, POULTRY_DOOR)
                        .add(SPRING, SUNDAY, 12000L, BARN_DOOR)
                        .add(SPRING, SUNDAY, 16000L, TOWNHALL_LEFT)
                        .add(SPRING, SUNDAY, 18000L, CLOCKMAKER_UPSTAIRS)
                        .add(SPRING, MONDAY, 0L, CLOCKMAKER_UPSTAIRS)
                        .add(SPRING, MONDAY, 8000L, BARN_DOOR)
                        .add(SPRING, MONDAY, 10000L, POULTRY_DOOR)
                        .add(SPRING, MONDAY, 14000L, FISHING_POND_LEFT)
                        .add(SPRING, MONDAY, 16000L, PARK_SPRUCE)
                        .add(SPRING, MONDAY, 18000L, CLOCKMAKER_UPSTAIRS)
                        .add(NEW_YEARS, 0L, CLOCKMAKER_UPSTAIRS)
                        .add(NEW_YEARS, 7000L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS, 12000L, PARK_SPRUCE)
                        .add(NEW_YEARS, 19000L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS, 20000L, CLOCKMAKER_UPSTAIRS)
                        .add(NEW_YEARS_EVE, 0L, CLOCKMAKER_UPSTAIRS)
                        .add(NEW_YEARS_EVE, 13000L, CLOCKMAKER_DOWNSTAIRS)
                        .add(NEW_YEARS_EVE, 17000L, PARK_BACK_LEFT)
                        .build();

        //Priest
        PRIEST.addGreeting(new GreetingPriestBlessing());
        ScheduleBuilder.create(PRIEST, TOWNHALL_ADULT_BED)
                        .add(SPRING, SUNDAY, 0L, TOWNHALL_ADULT_BED)
                        .add(SPRING, SUNDAY, 6000L, CHURCH_FRONT)
                        .add(SPRING, SUNDAY, 7000L, CHURCH_INSIDE)
                        .add(SPRING, SUNDAY, 17000L, GODDESS_POND_FRONT_RIGHT)
                        .add(SPRING, SUNDAY, 19000L, GODDESS_POND_FRONT)
                        .add(SPRING, SUNDAY, 22000L, TOWNHALL_ADULT_BED)
                        .add(SPRING, MONDAY, 0L, TOWNHALL_ADULT_BED)
                        .add(SPRING, MONDAY, 6000L, MINE_BACK)
                        .add(SPRING, MONDAY, 9000L, CHURCH_INSIDE)
                        .add(SPRING, MONDAY, 17500L, FISHING_HUT_RIGHT)
                        .add(SPRING, MONDAY, 19000L, TOWNHALL_LEFT_OF_STAGE)
                        .add(SPRING, MONDAY, 22000L, TOWNHALL_ADULT_BED)
                        .add(NEW_YEARS, 0L, TOWNHALL_ADULT_BED)
                        .add(NEW_YEARS, 7000L, TOWNHALL_ENTRANCE)
                        .add(NEW_YEARS, 12000L, PARK_BENCH)
                        .add(NEW_YEARS, 20000L, TOWNHALL_ENTRANCE)
                        .add(NEW_YEARS, 22000L, TOWNHALL_ADULT_BED)
                        .build();
        //Mayor
        ScheduleBuilder.create(MAYOR, TOWNHALL_STAGE)
                        .add(SPRING, SUNDAY, 0L, TOWNHALL_ADULT_BED)
                        .add(SPRING, SUNDAY, 6000L, TOWNHALL_FRONT_OF_STAGE)
                        .add(SPRING, SUNDAY, 9000L, TOWNHALL_STAGE)
                        .add(SPRING, SUNDAY, 15000L, CAFE_BALCONY)
                        .add(SPRING, SUNDAY, 19000L, TOWNHALL_LEFT)
                        .add(SPRING, SUNDAY, 22000L, TOWNHALL_ADULT_BED)
                        .add(COOKING_CONTEST, 0L, TOWNHALL_ADULT_BED)
                        .add(COOKING_CONTEST, 6000L, PARK_BUSH)
                        .add(COOKING_CONTEST, 19000L, TOWNHALL_FRONT_OF_STAGE)
                        .add(COOKING_CONTEST, 22000L, TOWNHALL_ADULT_BED)
                        .add(NEW_YEARS, 0L, TOWNHALL_ADULT_BED)
                        .add(NEW_YEARS, 7000L, TOWNHALL_STAGE)
                        .add(NEW_YEARS, 12000L, PARK_PODIUM)
                        .add(NEW_YEARS, 20000L, TOWNHALL_STAGE)
                        .add(NEW_YEARS, 22000L, TOWNHALL_ADULT_BED)
                        .add(NEW_YEARS_EVE, 0L, TOWNHALL_ADULT_BED)
                        .add(NEW_YEARS_EVE, 13000L, TOWNHALL_STAGE)
                        .add(NEW_YEARS_EVE, 17000L, PARK_CUSTOMER)
                        .build();

        //Eldest Daughter
        ScheduleBuilder.create(DAUGHTER_ADULT, TOWNHALL_TEEN_BED)
                        .add(SPRING, SUNDAY, 0L, TOWNHALL_TEEN_BED)
                        .add(SPRING, SUNDAY, 7000L, CHURCH_PEW_FRONT_RIGHT)
                        .add(SPRING, SUNDAY, 10000L, CAFE_KITCHEN)
                        .add(SPRING, SUNDAY, 13000L, POULTRY_FRONT)
                        .add(SPRING, SUNDAY, 15000L, TOWNHALL_RIGHT)
                        .add(SPRING, SUNDAY, 19000L, TOWNHALL_TEEN_BED)
                        .add(SPRING, MONDAY, 0L, TOWNHALL_TEEN_BED)
                        .add(SPRING, MONDAY, 7000L, CAFE_KITCHEN)
                        .add(SPRING, MONDAY, 10000L, FISHING_HUT_DOOR)
                        .add(SPRING, MONDAY, 15000L, TOWNHALL_RIGHT)
                        .add(SPRING, MONDAY, 19000L, TOWNHALL_TEEN_BED)
                        .add(NEW_YEARS, 0L, TOWNHALL_TEEN_BED)
                        .add(NEW_YEARS, 7000L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS, 12000L, PARK_BENCH)
                        .add(NEW_YEARS, 20000L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS, 22000L, TOWNHALL_TEEN_BED)
                        .add(NEW_YEARS_EVE, 0L, TOWNHALL_TEEN_BED)
                        .add(NEW_YEARS_EVE, 13000L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS_EVE, 17000L, PARK_CUSTOMER)
                        .build();

        //Youngest Daughter
        ScheduleBuilder.create(DAUGHTER_CHILD, TOWNHALL_CHILD_BED)
                        .add(SPRING, SUNDAY, 0L, TOWNHALL_CHILD_BED)
                        .add(SPRING, SUNDAY, 8000L, CHURCH_PEW_FRONT_LEFT)
                        .add(SPRING, SUNDAY, 10000L, POULTRY_DOOR)
                        .add(SPRING, SUNDAY, 12000L, BARN_DOOR)
                        .add(SPRING, SUNDAY, 14000L, TOWNHALL_LEFT)
                        .add(SPRING, SUNDAY, 15000L, TOWNHALL_CHILD_BED)
                        .add(SPRING, MONDAY, 0L, TOWNHALL_CHILD_BED)
                        .add(SPRING, MONDAY, 8000L, BARN_DOOR)
                        .add(SPRING, MONDAY, 10000L, POULTRY_DOOR)
                        .add(SPRING, MONDAY, 12000L, TOWNHALL_LEFT)
                        .add(SPRING, MONDAY, 15000L, TOWNHALL_CHILD_BED)
                        .add(NEW_YEARS, 0L, TOWNHALL_CHILD_BED)
                        .add(NEW_YEARS, 7000L, TOWNHALL_LEFT)
                        .add(NEW_YEARS, 12000L, PARK_BENCH)
                        .add(NEW_YEARS, 19000L, TOWNHALL_LEFT)
                        .add(NEW_YEARS, 20000L, TOWNHALL_CHILD_BED)
                        .add(NEW_YEARS_EVE, 0L, TOWNHALL_CHILD_BED)
                        .add(NEW_YEARS_EVE, 13000L, TOWNHALL_LEFT)
                        .add(NEW_YEARS_EVE, 17000L, PARK_CUSTOMER)
                        .build();

        //Trader
        ScheduleBuilder.create(TRADER, PARK_TRADER)
                        .add(SPRING, SUNDAY, 0L, TOWNHALL_RIGHT)
                        .add(SPRING, SUNDAY, 6000L, PARK_TRADER)
                        .add(SPRING, SUNDAY, 11000L, GENERAL_CUSTOMER)
                        .add(SPRING, SUNDAY, 13000L, GENERAL_GARDEN)
                        .add(SPRING, SUNDAY, 16000L, GODDESS_POND_BACK_RIGHT)
                        .add(COOKING_CONTEST, 0L, TOWNHALL_RIGHT)
                        .add(COOKING_CONTEST, 6000L, PARK_TRADER)
                        .add(COOKING_CONTEST, 19000L, GENERAL_GARDEN)
                        .add(COOKING_CONTEST, 22000L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS, 0L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS, 10000L, TOWNHALL_ENTRANCE)
                        .add(NEW_YEARS, 11500L, PARK_TRADER_RIGHT)
                        .add(NEW_YEARS, 22500L, TOWNHALL_ENTRANCE)
                        .add(NEW_YEARS, 23500L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS_EVE, 0L, TOWNHALL_RIGHT)
                        .add(NEW_YEARS_EVE, 13000L, TOWNHALL_ENTRANCE)
                        .add(NEW_YEARS_EVE, 17000L, PARK_TABLE)
                        .build();

        for (NPC npc: NPC.REGISTRY) {
            if (npc != NPC.NULL_NPC) {
                //addHolidayGreetings(npc,
                        //HFFestivals.NEW_YEARS, HFFestivals.COOKING_CONTEST, HFFestivals.CHICKEN_FESTIVAL, HFFestivals.COW_FESTIVAL,
                        //HFFestivals.HARVEST_FESTIVAL, HFFestivals.SHEEP_FESTIVAL, HFFestivals.STARRY_NIGHT, HFFestivals.NEW_YEARS_EVE);
                addHolidayGreetings(npc, HFFestivals.NEW_YEARS, HFFestivals.COOKING_CONTEST, HFFestivals.NEW_YEARS_EVE);
                setupGifts(npc);
            }
        }
    }

    private static void addHolidayGreetings(NPC npc, Festival... festivals) {
        for (Festival festival : festivals) npc.addGreeting(new GreetingFestival(festival));
    }

    private static void setupGifts(NPC npc) {
        npc.setGiftHandler(new IGiftHandler() {});
        if (npc.getRegistryName().getResourceDomain().equals(MODID)) {
            try {
                IGiftHandler handler = (IGiftHandler) Class.forName(GIFTPATH + WordUtils.capitalize(npc.getRegistryName().getResourcePath())).newInstance();
                if (handler != null) npc.setGiftHandler(handler);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {/**/}
        }
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        HFClientProxy.RENDER_MAP.put(SPAWNER_NPC, NPCTile.INSTANCE);
        ClientRegistry.bindTileEntitySpecialRenderer(NPCTile.class, new NPCItemRenderer());
        for (NPC npc: NPC.REGISTRY) {
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