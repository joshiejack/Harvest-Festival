package joshie.harvest.core.handlers;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.crops.block.BlockHFCrops;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import java.util.List;

@HFEvents
public class BasketHandler {
    public static boolean forbidsDrop(Block block) {
        return block instanceof BlockDoor || block instanceof BlockFenceGate || block instanceof BlockTrapDoor
                || block instanceof BlockLever || block instanceof BlockButton || block instanceof BlockHFCrops
                || block instanceof joshie.harvest.mining.block.BlockDirt;
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getEntityItem();
        if (HFApi.shipping.getSellValue(stack) > 0) {
            List<ItemStack> list = Lists.newArrayList(stack);
            if (EntityBasket.findBasketAndShip(event.getEntityPlayer(), list)) {
                event.getItem().setDead();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
        for (Entity entity : event.player.getPassengers()) {
            if (entity instanceof EntityBasket) {
                EntityPlayer player = event.player;
                BlockPos pos = new BlockPos(player);
                if (player.worldObj.isAirBlock(pos)) {
                    player.worldObj.setBlockState(pos, HFCore.STORAGE.getStateFromEnum(Storage.BASKET));
                } else {
                    boolean placed = false;
                    int attempts = 0;
                    while (!placed && attempts < 512) {
                        BlockPos placing = pos.add(player.worldObj.rand.nextInt(10) - 5, player.worldObj.rand.nextInt(3), player.worldObj.rand.nextInt(10) - 5);
                        if (player.worldObj.isAirBlock(placing)) {
                            player.worldObj.setBlockState(placing, HFCore.STORAGE.getStateFromEnum(Storage.BASKET));
                            placed = true;
                        }

                        attempts++;
                    }
                }

                entity.setDead();
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!forbidsDrop(event.getWorld().getBlockState(event.getPos()).getBlock())) {
            for (Entity entity : player.getPassengers()) {
                if (entity instanceof EntityBasket) {
                    ItemStack basket = HFCore.STORAGE.getStackFromEnum(Storage.BASKET);
                    if (basket.onItemUse(player, player.worldObj, event.getPos(), EnumHand.MAIN_HAND, event.getFace(), 0F, 0F, 0F) == EnumActionResult.SUCCESS) {
                        entity.setDead();
                    }
                }
            }
        }
    }
}
