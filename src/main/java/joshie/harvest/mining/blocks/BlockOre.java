package joshie.harvest.mining.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.BlockHFEnum;
import joshie.harvest.core.util.generic.Text;
import joshie.harvest.mining.blocks.BlockOre.Ore;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.mining.blocks.BlockOre.Ore.MYTHIC;
import static net.minecraft.util.text.TextFormatting.GREEN;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class BlockOre extends BlockHFEnum<BlockOre, Ore> {
    private static final AxisAlignedBB COPPER_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);

    public enum Ore implements IStringSerializable {
        JUNK, COPPER, SILVER, GOLD, MYSTRIL, MYTHIC;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public BlockOre() {
        super(Material.ROCK, Ore.class, HFTab.MINING);
        setHardness(1.5F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return COPPER_AABB;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case MYTHIC:
                return GREEN + super.getItemStackDisplayName(stack);
            default:
                return WHITE + super.getItemStackDisplayName(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (getEnumFromStack(stack) == MYTHIC) {
            list.add(Text.translate("tooltip.mythic_stone"));
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 10 + stack.getItemDamage();
    }
}