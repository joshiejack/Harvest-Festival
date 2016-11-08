package joshie.harvest.core.block;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GoddessHandler;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BlockGoddessWater extends BlockFluidClassic {
    public BlockGoddessWater(Fluid fluid) {
        super(fluid, Material.WATER);
    }

    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity instanceof EntityItem) {
            EntityItem item = ((EntityItem)entity);
            ItemStack stack = item.getEntityItem();
            if (!NPCRegistry.INSTANCE.getGifts().isBlacklisted(stack)) {
                if (!GoddessHandler.spawnGoddess(world, entity, false, false)) {
                    if (item.getThrower() != null) {
                        EntityPlayer player = world.getPlayerEntityByName(item.getThrower());
                        HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().gift(player, HFNPCs.GODDESS.getUUID(), HFNPCs.GODDESS.getGiftValue(stack).getRelationPoints());
                    }

                    entity.setDead();
                }
            }
        }
    }

    public BlockGoddessWater register(String name) {
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item));
    }
}