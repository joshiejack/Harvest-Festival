package joshie.harvest.core.block;

import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GoddessHandler;
import joshie.harvest.core.helpers.FakePlayerHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPCGoddess;
import joshie.harvest.npcs.packet.PacketGoddessGift;
import joshie.harvest.player.relationships.RelationshipData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;
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
            ItemStack stack = item.getItem();
            if (!NPCHelper.INSTANCE.getGifts().isBlacklisted(world, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos), stack)) {
                if (!GoddessHandler.spawnGoddess(world, entity, false, false)) {
                    if (item.getThrower() != null) {
                        EntityPlayer player = world.getPlayerEntityByName(item.getThrower());
                        RelationshipData data = HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships();
                        if (!data.isStatusMet(HFNPCs.GODDESS, RelationStatus.GIFTED)) {
                            HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().gift(player, HFNPCs.GODDESS, NPCHelper.getGiftValue(HFNPCs.GODDESS, stack).getRelationPoints());
                            double x = item.posX;
                            double y = item.posY;
                            double z = item.posZ;
                            List<EntityNPCGoddess> npcs = world.getEntitiesWithinAABB(EntityNPCGoddess.class,
                                    new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F).expand(32D, 32D, 32D));
                            if (npcs.size() >= 1) {
                                PacketHandler.sendToClient(new PacketGoddessGift(npcs.get(0), stack), player);
                            }
                        }
                    }

                    entity.setDead();
                }
            }
        }
    }

    public BlockGoddessWater register(String name) {
        setTranslationKey(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameData.register_impl(this);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(item));
    }
}