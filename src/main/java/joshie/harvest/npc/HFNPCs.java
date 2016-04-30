package joshie.harvest.npc;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import joshie.harvest.npc.render.RenderNPC;
import joshie.harvest.player.town.TownData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.npc.NPC.Age.*;
import static joshie.harvest.npc.NPC.Gender.FEMALE;
import static joshie.harvest.npc.NPC.Gender.MALE;

public class HFNPCs {
    public static INPC goddess; //The Goddess                        (SPAWN)
    public static INPC animal_owner; // Owner of the Animal Barn     (BARN)
    public static INPC cafe_owner; // Owner of the Cafe              (CAFE)
    public static INPC cafe_granny;// Granny of Cafe Owner           (CAFE)
    public static INPC seed_owner; // Sister of Yulif                (CARPENTER)
    public static INPC tool_owner; // Blacksmith                     (BLACKSMITH)
    public static INPC priest; //Married to mayor                    (CHURCH)
    public static INPC mayor; //Married to priest                    (TOWNHALL)
    public static INPC daughter_1; //Daughter of Mayor and Priest    (TOWNHALL)
    public static INPC daughter_2; //Daughter of Mayor and Priest    (TOWNHALL)
    public static INPC clock_worker; //The clock worker              (CLOCKWORKERS)
    public static INPC clockmaker_child; // Clockmakers Child        (CLOCKWORKERS)
    public static INPC gs_owner; //Owner of general store            (GENERAL STORE)
    public static INPC miner; //Works in the mines                   (MINING HUT)
    public static INPC fisherman; //Fisherman                        (FISHING HUT)
    public static INPC milkmaid; //Works in the Barn, Milking Cows   (GENERAL STORE)
    public static INPC builder; //Builds stuff for the players       (SPAWN)
    public static INPC poultry; //Poultry Farm Owner                 (POULTRY FARM)

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "NPCBuilder", 2, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "NPCShopkeeper", 3, HarvestFestival.instance, 80, 3, true);
        //EntityRegistry.registerModEntity(EntityNPCMiner.class, "NPCMiner", 4, HarvestFestival.instance, 80, 3, true);

        goddess = HFApi.NPC.register("goddess", FEMALE, ADULT, 8, Season.SPRING, 0x8CEED3, 0x4EC485).setHeight(1.2F, 0.05F);
        animal_owner = HFApi.NPC.register("jim", MALE, ADULT, 26, Season.SPRING, 0x888888, 0x000000);
        cafe_owner = HFApi.NPC.register("liara", FEMALE, ADULT, 17, Season.SPRING, 0xA64DFF, 0x46008C);
        seed_owner = HFApi.NPC.register("jade", FEMALE, ADULT, 14, Season.SPRING, 0x9326FF, 0x46008C);
        daughter_1 = HFApi.NPC.register("cloe", FEMALE, ADULT, 3, Season.SPRING, 0xFFFF99, 0xB2B200);
        priest = HFApi.NPC.register("thomas", MALE, ELDER, 9, Season.SUMMER, 0x006666, 0x00B2B20);
        clockmaker_child = HFApi.NPC.register("fenn", MALE, CHILD, 25, Season.SUMMER, 0x228C00, 0x003F00);
        cafe_granny = HFApi.NPC.register("katlin", FEMALE, ELDER, 12, Season.SUMMER, 0xDDDDDD, 0x777777);
        mayor = HFApi.NPC.register("jamie", FEMALE, ELDER, 8, Season.SUMMER, 0xFF9326, 0xB25900);
        builder = HFApi.NPC.register("yulif", MALE, ADULT, 19, Season.SUMMER, 0x8C001A, 0x3C0000).setIsBuilder();
        tool_owner = HFApi.NPC.register("daniel", MALE, ADULT, 1, Season.WINTER, 0xD9D916, 0x6D14C7);
        daughter_2 = HFApi.NPC.register("abi", FEMALE, CHILD, 27, Season.WINTER, 0xFF99FF, 0xFF20FF);
        clock_worker = HFApi.NPC.register("tiberius", MALE, ADULT, 15, Season.WINTER, 0xFF4C4C, 0x700606);
        gs_owner = HFApi.NPC.register("jenni", FEMALE, ADULT, 7, Season.WINTER, 0x2858E0, 0x000000);
        miner = HFApi.NPC.register("brandon", MALE, ADULT, 13, Season.AUTUMN, 0xC28D48, 0x5F5247).setIsMiner();
        fisherman = HFApi.NPC.register("jacob", MALE, ADULT, 28, Season.AUTUMN, 0x7396FF, 0x0036D9);
        milkmaid = HFApi.NPC.register("candice", FEMALE, ADULT, 5, Season.AUTUMN, 0xF65FAB, 0xF21985);
        poultry = HFApi.NPC.register("ondra", MALE, ADULT, 16, Season.AUTUMN, 0xFF8000, 0x46008C);
    }

    public static void init() {
        goddess.setHome(HFBuildings.goddessPond, TownData.GODDESS);
        animal_owner.setHome(HFBuildings.barn, TownData.JIM);
        cafe_owner.setHome(HFBuildings.cafe, TownData.LIARA);
        seed_owner.setHome(HFBuildings.carpenter, TownData.JADE);
        daughter_1.setHome(HFBuildings.townhall, TownData.CLOE);
        priest.setHome(HFBuildings.townhall, TownData.TOWNHALL_ADULT_BEDROOM);
        clockmaker_child.setHome(HFBuildings.clockmaker, TownData.FENN);
        cafe_granny.setHome(HFBuildings.cafe, TownData.KATLIN);
        mayor.setHome(HFBuildings.townhall, TownData.JAMIE);
        builder.setHome(HFBuildings.carpenter, TownData.CARPENTER_DOWNSTAIRS);
        tool_owner.setHome(HFBuildings.blacksmith, TownData.DANIEL);
        daughter_2.setHome(HFBuildings.townhall, TownData.ABI);
        clock_worker.setHome(HFBuildings.clockmaker, TownData.TIBERIUS);
        gs_owner.setHome(HFBuildings.supermarket, TownData.JENNI);
        miner.setHome(HFBuildings.miningHut, TownData.BRANDON);
        fisherman.setHome(HFBuildings.fishingHut, TownData.JACOB);
        milkmaid.setHome(HFBuildings.supermarket, TownData.CANDICE);
        poultry.setHome(HFBuildings.poultryFarm, TownData.ONDRA);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        registerNPCRendering(EntityNPC.class);
        registerNPCRendering(EntityNPCBuilder.class);
        registerNPCRendering(EntityNPCShopkeeper.class);
    }

    private static <E extends EntityNPC> void registerNPCRendering(Class<E> entityClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<EntityNPC>() {
            @Override
            public Render<? super EntityNPC> createRenderFor(RenderManager manager) {
                return new RenderNPC(manager);
            }
        });
    }
}