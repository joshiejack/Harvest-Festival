package joshie.harvest.mining.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnumCube;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.Text;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockStone.Type;
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
import java.util.Locale;

import static joshie.harvest.mining.block.BlockStone.Type.REAL;

public class BlockStone extends BlockHFEnumCube<BlockStone, Type> {
    public enum Type implements IStringSerializable {
        REAL, DECORATIVE_BLANK, DECORATIVE_PURPLE, DECORATIVE_SILVER, DECORATIVE_GREEN, DECORATIVE_BLUE, DECORATIVE_RED;

        public boolean isFake() {
            return this != REAL;
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
        ArrayList<ItemStack> ret = new ArrayList<>();
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
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = getUnlocalizedName();
        String name = stack.getItemDamage() != 0 ? "decorative" : stack.getItem().getUnlocalizedName(stack);
        return Text.localizeFully(unlocalized + "." + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        int adjusted = Math.max(0, Math.min(Type.values().length, stack.getItemDamage()));
        Type type = Type.values()[adjusted];
        if (type.isFake()) {
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