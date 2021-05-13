package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class ItemBaseSickle extends ItemSingularTool {
    protected static final Int2IntMap areaByHarvestLevel = new Int2IntOpenHashMap();
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.SAPLING, Blocks.LEAVES, Blocks.LEAVES2, Blocks.TALLGRASS, Blocks.DEADBUSH, Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.WHEAT, Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.VINE, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.HAY_BLOCK, Blocks.DOUBLE_PLANT);

    public ItemBaseSickle(ResourceLocation registry) {
        super(registry, ToolMaterial.STONE, EFFECTIVE_ON);
        areaByHarvestLevel.put(0, 0);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == stack.getMaxDamage() ?
                StringHelper.format("penguinlib.item.tools.broken", super.getItemStackDisplayName(stack)) : super.getItemStackDisplayName(stack);
    }

    @Nonnull
    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("sickle");
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
        if (stack.getItemDamage() < stack.getMaxDamage()) {
            for (String type : getToolClasses(stack)) {
                if (state.getBlock().isToolEffective(type, state))
                    return efficiency;
            }

            return EFFECTIVE_ON.contains(state.getBlock()) || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS ? this.efficiency : 1.0F;
        } else return 1F;
    }
}
