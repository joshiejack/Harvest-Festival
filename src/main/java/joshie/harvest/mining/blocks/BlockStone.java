package joshie.harvest.mining.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.BlockHFEnumCube;
import joshie.harvest.core.helpers.WorldHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.Text;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.blocks.BlockStone.Type;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.mining.blocks.BlockStone.Type.*;

public class BlockStone extends BlockHFEnumCube<BlockStone, Type> {
    public enum Type implements IStringSerializable {
        REAL(true), DECORATIVE_BLANK, DECORATIVE_PURPLE, DECORATIVE_SILVER, DECORATIVE_GREEN, DECORATIVE_BLUE, DECORATIVE_RED,
        REAL_WINTER(true), DECORATIVE_BLANK_WINTER, DECORATIVE_PURPLE_WINTER, DECORATIVE_SILVER_WINTER, DECORATIVE_GREEN_WINTER, DECORATIVE_BLUE_WINTER, DECORATIVE_RED_WINTER,
        HOLE;

        private final boolean isReal;

        Type() {
            this.isReal = false;
        }

        Type(boolean isReal) {
            this.isReal = isReal;
        }

        public boolean isReal() {
            return isReal;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public BlockStone() {
        super(Material.ROCK, Type.class, HFTab.MINING);
        setSoundType(SoundType.STONE);
    }

    //TECHNICAL
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getEnumFromState(state) == REAL ? -1F: 4F;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return getEnumFromState(state) != REAL;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return getEnumFromState(world.getBlockState(pos)) == REAL ? 60000000F : 14F;
    }

    @Override
    public int getToolLevel(Type type) {
        return 2;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (getEnumFromState(world.getBlockState(pos)) != REAL) {
            ret.add(new ItemStack(HFMining.STONE, 1, 1));
        }

        return ret;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if(getEnumFromState(state).isReal) {
            Season season = HFApi.calendar.getSeasonAtCoordinates(WorldHelper.getWorld(world), pos);
            return season == Season.WINTER ? state.withProperty(property, REAL_WINTER) : state.withProperty(property, REAL);
        } else return state;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (getEnumFromStack(stack) == HOLE) return Text.translate("hole");
        String unlocalized = getUnlocalizedName();
        String name = stack.getItemDamage() != 0 ? "decorative" : stack.getItem().getUnlocalizedName(stack);
        return Text.localizeFully(unlocalized + "." + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        int adjusted = Math.max(0, Math.min(Type.values().length, stack.getItemDamage()));
        Type type = Type.values()[adjusted];
        if (!type.isReal && type != HOLE) {
            list.add(TextFormatting.YELLOW + Text.translate("tooltip.cosmetic"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, "mine/mine_wall_" + getEnumFromMeta(i).getName()), "inventory"));
        }
    }
}