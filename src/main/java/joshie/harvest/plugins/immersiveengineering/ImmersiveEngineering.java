package joshie.harvest.plugins.immersiveengineering;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.handlers.DisableHandler.DisableVanillaSeeds;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFCROPS;


@HFLoader(mods = "immersiveengineering", priority = HFCROPS + 1)
public class ImmersiveEngineering {
    private static final StateHandlerHemp STATE_HANDLER = new StateHandlerHemp();
    public static final Crop HEMP = new Crop(new ResourceLocation(MODID, "hemp"), 1000L, 1L, 15, 0xB57449, SPRING, SUMMER, AUTUMN).setStateHandler(STATE_HANDLER)
                                        .setAnimalFoodType(null).setRequiresSickle(15).setGrowthHandler(new HempGrowthHandler()).setBecomesDouble(15).setRegrowStage(1);

    @ItemStackHolder(value = "immersiveengineering:material", meta = 4)
    public static final ItemStack hemp = null;

    @ItemStackHolder(value = "immersiveengineering:seed")
    public static final ItemStack hemp_seeds = null;

    public static void preInit() {}

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        STATE_HANDLER.setBlock(Block.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "hemp")));
        HEMP.setDropHandler(new DropHandlerHemp(hemp.getItem())).setItem(hemp);
        DisableVanillaSeeds.BLACKLIST.register(hemp_seeds);
    }
}
