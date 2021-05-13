package uk.joshiejack.penguinlib.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.database.registries.SickleRegistry;
import uk.joshiejack.penguinlib.data.database.registries.SmashRegistry;
import uk.joshiejack.penguinlib.events.BlockSmashedEvent;
import uk.joshiejack.penguinlib.item.interfaces.SmashingTool;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class ToolUsageEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null && !player.world.isRemote && player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains("sickle")) {
            IBlockState state = event.getState();
            ItemStack stack = SickleRegistry.SICKLE.get(state);
            if (stack == null && state.getBlock() == Blocks.DOUBLE_PLANT) stack = SickleRegistry.SICKLE.get(player.world.getBlockState(event.getPos().down()));
            if (stack != null) {
                Block.spawnAsEntity(player.world, event.getPos(), stack.copy());
            }
        }
    }

    @SubscribeEvent
    public static void onCriticalSmash(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getHand() == EnumHand.MAIN_HAND && player.motionY <= -0.1F) {
            Item item = player.getHeldItem(event.getHand()).getItem();
            if (item instanceof SmashingTool) {
                World world = event.getWorld();
                BlockPos pos = event.getPos();
                ItemStack stack = player.getHeldItem(event.getHand());
                int area = ((SmashingTool) item).getArea();
                for (BlockPos target : BlockPos.getAllInBox(pos.add(-area, 0, -area), pos.add(area, 0, area))) {
                    IBlockState state = world.getBlockState(target);
                    if (ForgeHooks.canToolHarvestBlock(world, target, stack) && stack.getItemDamage() < stack.getMaxDamage() &&
                    ForgeHooks.isToolEffective(world, target, stack) && ForgeHooks.blockStrength(state, player, world, target) > 0F
                     && (SmashRegistry.ALLOWED_BLOCKS.contains(state.getBlock()) || SmashRegistry.ALLOWED_STATES.contains(state))) {
                        if (!world.isRemote) {
                            BlockSmashedEvent smashEvent = new BlockSmashedEvent(player, event.getHand(), target, state);
                            MinecraftForge.EVENT_BUS.post(smashEvent); //Post a smash event
                            if (!smashEvent.isCanceled()) {
                                stack.damageItem(1, player);
                                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 0, 0, 0);
                                if (!target.equals(pos)) {
                                    TerrainHelper.destroyBlock(world, target, (EntityPlayerMP) player);
                                    SoundType soundtype = state.getBlock().getSoundType(state, world, target, null);
                                    world.playSound(null, target, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
