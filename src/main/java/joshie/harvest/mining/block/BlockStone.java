package joshie.harvest.mining.block;

import com.mojang.realmsclient.gui.ChatFormatting;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnumCube;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockStone.Type;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.mining.block.BlockStone.Type.LADDER_HOLE;

public class BlockStone extends BlockHFEnumCube<BlockStone, Type> {
    public enum Type implements IStringSerializable {
        REAL(false), DECORATIVE_BLANK(true), DECORATIVE_PURPLE(true), DECORATIVE_SILVER(true), DECORATIVE_GREEN(true), DECORATIVE_BLUE(true), DECORATIVE_RED(true), LADDER_HOLE(false);

        private final boolean isFake;

        Type(boolean isFake) {
            this.isFake = isFake;
        }

        public boolean isFake() {
            return isFake;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockStone() {
        super(Material.ROCK, Type.class, HFTab.MINING);
        setSoundType(SoundType.STONE);
    }

    //TECHNICAL
    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return !getEnumFromState(state).isFake ? -1F: 4F;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return getEnumFromState(state).isFake;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nonnull Entity exploder, Explosion explosion) {
        return !getEnumFromState(world.getBlockState(pos)).isFake ? 60000000F : 14F;
    }

    @Override
    public int getToolLevel(Type type) {
        return 2;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        NonNullList<ItemStack> ret = NonNullList.create();
        if (getEnumFromState(world.getBlockState(pos)).isFake()) {
            ret.add(new ItemStack(HFMining.STONE, 1, getEnumFromState(world.getBlockState(pos)).ordinal()));
        }

        return ret;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (getEnumFromState(state) == LADDER_HOLE) {
            ItemStack heldItem = player.getHeldItem(hand);
            if (heldItem.getItem() == Item.getItemFromBlock(HFMining.LADDER)) {
                world.setBlockState(pos, world.getBlockState(pos.down()));
                heldItem.shrink(1);
                return true;
            } else if (world.isRemote) ChatHelper.displayChat(ChatFormatting.ITALIC + TextHelper.translate("stone.ladder.hole.whisper"));
        }

        return false;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, SpawnPlacementType type) {
        return false;
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        String unlocalized = getTranslationKey();
        String name = getEnumFromStack(stack).isFake() ? "decorative" : stack.getItem().getTranslationKey(stack);
        return TextHelper.localizeFully(unlocalized + "." + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        int adjusted = Math.max(0, Math.min(Type.values().length, stack.getItemDamage()));
        Type type = Type.values()[adjusted];
        if (type.isFake()) {
            list.add(TextFormatting.YELLOW + TextHelper.translate("tooltip.cosmetic"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, "mine/mine_wall_" + getEnumFromMeta(i).getName()), "inventory"));
        }
    }
}