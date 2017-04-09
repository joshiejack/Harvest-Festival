package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJade extends Gifts {
    public GiftsJade() {
        stackRegistry.register(Ore.of("cropGrape"), Quality.AWESOME);
        stackRegistry.register(new ItemStack(Blocks.RED_FLOWER, 1, 2), Quality.AWESOME);
        stackRegistry.register(HFGathering.NATURE.getStackFromEnum(NaturalBlock.LAVENDER), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.AMETHYST), Quality.AWESOME);
        stackRegistry.register(Items.RABBIT_FOOT, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.GOOD);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(MINERAL, Quality.BAD);
        stackRegistry.register(Ore.of("stone"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("logWood"), Quality.TERRIBLE);
    }
}