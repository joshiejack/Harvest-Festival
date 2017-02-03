package joshie.harvest.festivals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.special.SpecialRuleFestivals;
import joshie.harvest.core.helpers.RegistryHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.festivals.block.BlockStand;
import joshie.harvest.festivals.render.SpecialRendererStand;
import joshie.harvest.festivals.tile.TileStand;
import joshie.harvest.npcs.HFNPCs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.api.calendar.Season.WINTER;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFBUILDING;

@HFLoader(priority = HFBUILDING)
@SuppressWarnings("unchecked")
public class HFFestivals {
    public static final Festival STARRY_NIGHT_PRE = registerFestival("starry_pre", 23, WINTER);
    public static final Building FESTIVAL_GROUNDS = HFBuildings.registerBuilding("festivals", BuildingFestival.class).setSpecialRules(new SpecialRuleFestivals()).setInhabitants(HFNPCs.TRADER).setOffset(14, -1, 32);
    public static final BlockStand STAND = new BlockStand().register("stand");

    public static void preInit() {
        RegistryHelper.registerTiles(TileStand.class);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileStand.class, new SpecialRendererStand());
    }

    public static Festival registerFestival(String name, int day, Season season) {
        ResourceLocation resource = new ResourceLocation(MODID, name);
        Festival festival = new Festival(resource);
        HFApi.calendar.registerFestival(festival, day, season);
        return festival;
    }
}
