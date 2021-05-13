package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.HorticultureConfig;
import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.item.ItemCrop;
import uk.joshiejack.penguinlib.block.base.BlockBaseCrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Locale;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockCrop extends BlockBaseCrop {
    public static final EnumMap<ItemCrop.Crops, Block> BLOCKS = new EnumMap<>(ItemCrop.Crops.class);
    private final ItemCrop.Crops crop;
    private final int regrow;

    public BlockCrop(ItemCrop.Crops crop, int num) {
        this(crop, num, -1);
    }

    public BlockCrop(ItemCrop.Crops crop, int num, int regrow) {
        super(new ResourceLocation(MODID, crop.getName().toLowerCase(Locale.ENGLISH)), num);
        this.crop = crop;
        this.regrow = regrow;
        BlockCrop.BLOCKS.put(crop, this);
    }

    public BlockCrop(ResourceLocation registry, ItemCrop.Crops crop, int num, int regrow) {
        super(registry, num);
        this.crop = crop;
        this.regrow = regrow;
        BlockCrop.BLOCKS.put(crop, this);
    }

    @Override
    protected ItemStack getCropStack() {
        return HorticultureItems.CROP.getStackFromEnum(crop, 1);
    }

    @Override
    protected ItemStack getSeedStack() {
        return HorticultureConfig.enableSeedDrops ? HorticultureItems.SEEDS.getStackFromEnum(crop, 1) : ItemStack.EMPTY;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player,
                                    @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!HorticultureConfig.enableRightClickHarvest) return false;
        int age = getAge(state);
        if (age >= getMaxAge())  {
            boolean toAir = regrow == -1 || !HorticultureConfig.enableCropRegrowth;
            dropBlockAsItem(world, pos, state, (toAir ? 0 : LESS_SEEDS));
            return toAir ? world.setBlockToAir(pos) : world.setBlockState(pos, withAge(regrow));
        } else return false;
    }
}
