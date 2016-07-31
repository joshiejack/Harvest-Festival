package joshie.harvest.core.handlers;

import joshie.harvest.blocks.BlockFlower.FlowerType;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@HFEvents
public class GoddessHandler {
    private static Item goddess;

    public static boolean isGoddessFlower(ItemStack stack) {
        if (goddess == null) goddess = Item.getItemFromBlock(HFBlocks.FLOWERS);
        return stack.getItem() == goddess && stack.getItemDamage() == FlowerType.GODDESS.ordinal();
    }

    public static void spawnGoddess(World world, Entity entity) {
        spawnGoddess(world, entity.posX, entity.posY, entity.posZ);
    }

    public static void spawnGoddess(World world, double x, double y, double z) {
        List<AbstractEntityNPC> npcs = world.getEntitiesWithinAABB(AbstractEntityNPC.class, new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F).expand(32D, 32D, 32D));
        AbstractEntityNPC goddess = null;
        for (AbstractEntityNPC npc: npcs) {
            if (npc.getNPC() == HFNPCs.GODDESS) {
                goddess = npc;
                break;
            }
        }

        goddess = goddess != null ? goddess : NPCHelper.getEntityForNPC(world, (NPC) HFNPCs.GODDESS);
        goddess.setPosition((int) x, (int) y + 1, (int) z);
        goddess.resetSpawnHome();
        world.spawnEntityInWorld(goddess);
    }

    //Goddess flower spawns goddess
    @SubscribeEvent
    public void onItemExpire(ItemExpireEvent event) {
        World world = event.getEntityItem().worldObj;
        if (!world.isRemote) {
            ItemStack stack = event.getEntityItem().getEntityItem();
            if (stack != null) {
                if (isGoddessFlower(stack)) {
                    if (event.getEntityItem().isInsideOfMaterial(Material.WATER)) {
                        spawnGoddess(world, event.getEntityItem());
                    } else {
                        event.setExtraLife(5900);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public static long lastGoddess;

    //Right clicking flower pot with stick creates goddess
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (System.currentTimeMillis() - lastGoddess >= 1000L) {
            ItemStack held = event.getItemStack();
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
                                event.getWorld().playEvent(2005, event.getPos(), 0);
                                if (event.getWorld().rand.nextInt(5) == 0) {
                                    spawnGoddess(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
                                    lastGoddess = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}