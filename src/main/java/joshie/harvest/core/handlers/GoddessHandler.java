package joshie.harvest.core.handlers;

import joshie.harvest.blocks.items.ItemBlockFlower;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GoddessHandler {
    //Goddess flower spawns goddess
    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        World world = event.getEntityItem().worldObj;
        if (!world.isRemote) {
            if (event.getEntityItem().isInsideOfMaterial(Material.WATER)) {
                ItemStack stack = event.getEntityItem().getEntityItem();
                if (stack != null) {
                    if (stack.getItem() instanceof ItemBlockFlower) {
                        EntityNPC goddess = NPCHelper.getEntityForNPC(null, world, HFNPCs.GODDESS);
                        goddess.setPosition((int) event.getEntityItem().posX, (int) event.getEntityItem().posY + 1, (int) event.getEntityItem().posZ);
                        world.spawnEntityInWorld(goddess);
                    }
                }
            }
        }
    }

    //Right clicking flower pot with stick creates goddess
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        ItemStack held = event.getEntityPlayer().getActiveItemStack();
        if (held != null && held.getItem() == Items.STICK) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.FLOWER_POT) {
                TileEntityFlowerPot tile = (TileEntityFlowerPot) event.getWorld().getTileEntity(event.getPos());
                if (tile.getFlowerPotItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)) {
                    Block xMinus = event.getWorld().getBlockState(event.getPos().add(-1, -1, 0)).getBlock();
                    Block xPlus = event.getWorld().getBlockState(event.getPos().add(1, -1, 0)).getBlock();
                    Block zMinus = event.getWorld().getBlockState(event.getPos().add(0, -1, -1)).getBlock();
                    Block zPlus = event.getWorld().getBlockState(event.getPos().add(0, -1, 1)).getBlock();
                    int water = 0;
                    int flower = 0;

                    if (xMinus == Blocks.WATER) water++;
                    if (xPlus == Blocks.WATER) water++;
                    if (zMinus == Blocks.WATER) water++;
                    if (zPlus == Blocks.WATER) water++;
                    xMinus = event.getWorld().getBlockState(event.getPos().add(-1, 0, 0)).getBlock();
                    xPlus = event.getWorld().getBlockState(event.getPos().add(1, 0, 0)).getBlock();
                    zMinus = event.getWorld().getBlockState(event.getPos().add(0, 0, -1)).getBlock();
                    zPlus = event.getWorld().getBlockState(event.getPos().add(0, 0, 1)).getBlock();
                    if (xMinus == Blocks.RED_FLOWER || xMinus == Blocks.DOUBLE_PLANT || xMinus == Blocks.TALLGRASS)
                        flower++;
                    if (xPlus == Blocks.RED_FLOWER || xPlus == Blocks.DOUBLE_PLANT || xPlus == Blocks.TALLGRASS)
                        flower++;
                    if (zMinus == Blocks.RED_FLOWER || zMinus == Blocks.DOUBLE_PLANT || zMinus == Blocks.TALLGRASS)
                        flower++;
                    if (zPlus == Blocks.RED_FLOWER || zPlus == Blocks.DOUBLE_PLANT || zPlus == Blocks.TALLGRASS)
                        flower++;

                    if (water == 2 && flower == 2) {
                        if (!event.getWorld().isRemote) {
                            event.getWorld().playAuxSFX(2005, event.getPos(), 0);
                            if (event.getWorld().rand.nextInt(5) == 0) {
                                EntityNPC goddess = NPCHelper.getEntityForNPC(null, event.getWorld(), HFNPCs.GODDESS);
                                goddess.setPosition(event.getPos().getX(), event.getPos().getY() + 1, event.getPos().getZ());
                                event.getWorld().spawnEntityInWorld(goddess);
                            }
                        }
                    }
                }
            }
        }
    }
}