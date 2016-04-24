package joshie.harvest.core.handlers;

import joshie.harvest.blocks.BlockFlower;
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
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GoddessHandler {   
    //Goddess flower spawns goddess
    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        World world = event.entityItem.worldObj;
        if (!world.isRemote) {
            if (event.entityItem.isInsideOfMaterial(Material.water)) {
                ItemStack stack = event.entityItem.getEntityItem();
                if (stack != null) {
                    if (stack.getItem() instanceof ItemBlockFlower) {
                        if (stack.getItemDamage() == BlockFlower.GODDESS) {
                            EntityNPC goddess = NPCHelper.getEntityForNPC(null, world, HFNPCs.goddess);
                            goddess.setPosition((int) event.entityItem.posX, (int) event.entityItem.posY + 1, (int) event.entityItem.posZ);
                            world.spawnEntityInWorld(goddess);
                        }
                    }
                }
            }
        }
    }

    //Right clicking flower pot with stick creates goddess
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack held = event.entityPlayer.getCurrentEquippedItem();
            if (held != null && held.getItem() == Items.STICK) {
                if (event.world.getBlockState(event.pos).getBlock() == Blocks.FLOWER_POT) {
                    TileEntityFlowerPot tile = (TileEntityFlowerPot) event.world.getTileEntity(event.pos);
                    if (tile.getFlowerPotItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)) {
                        Block xMinus = event.world.getBlockState(event.pos.add(-1, -1, 0)).getBlock();
                        Block xPlus = event.world.getBlockState(event.pos.add(+1, -1, 0)).getBlock();
                        Block zMinus = event.world.getBlockState(event.pos.add(0, -1, -1)).getBlock();
                        Block zPlus = event.world.getBlockState(event.pos.add(0, -1, +1)).getBlock();
                        int water = 0;
                        int flower = 0;

                        if (xMinus == Blocks.WATER) water++;
                        if (xPlus == Blocks.WATER) water++;
                        if (zMinus == Blocks.WATER) water++;
                        if (zPlus == Blocks.WATER) water++;
                        xMinus = event.world.getBlockState(event.pos.add(-1, 0, 0)).getBlock();
                        xPlus = event.world.getBlockState(event.pos.add(+1, 0, 0)).getBlock();
                        zMinus = event.world.getBlockState(event.pos.add(0, 0, -1)).getBlock();
                        zPlus = event.world.getBlockState(event.pos.add(0, 0, +1)).getBlock();
                        if (xMinus == Blocks.RED_FLOWER || xMinus == Blocks.DOUBLE_PLANT || xMinus == Blocks.TALLGRASS) flower++;
                        if (xPlus == Blocks.RED_FLOWER || xPlus == Blocks.DOUBLE_PLANT || xPlus == Blocks.TALLGRASS) flower++;
                        if (zMinus == Blocks.RED_FLOWER || zMinus == Blocks.DOUBLE_PLANT || zMinus == Blocks.TALLGRASS) flower++;
                        if (zPlus == Blocks.RED_FLOWER || zPlus == Blocks.DOUBLE_PLANT || zPlus == Blocks.TALLGRASS) flower++;
                        
                        if (water == 2 && flower == 2) {
                            if (!event.world.isRemote) {
                                event.world.playAuxSFX(2005, event.pos, 0);
                                if (event.world.rand.nextInt(5) == 0) {
                                    EntityNPC goddess = NPCHelper.getEntityForNPC(null, event.world, HFNPCs.goddess);
                                    goddess.setPosition((int) event.pos.getX(), (int) event.pos.getY() + 1, (int) event.pos.getZ());
                                    event.world.spawnEntityInWorld(goddess);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
