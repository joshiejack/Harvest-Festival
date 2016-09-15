package joshie.harvest.core.handlers;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.entity.EntityNPCGoddess;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@HFEvents
public class GoddessHandler {
    private static Item goddess;

    public static boolean isGoddessFlower(ItemStack stack) {
        if (goddess == null) goddess = Item.getItemFromBlock(HFCore.FLOWERS);
        return stack.getItem() == goddess && stack.getItemDamage() == FlowerType.GODDESS.ordinal();
    }

    public static boolean spawnGoddess(World world, Entity entity, boolean flower, boolean move) {
        return spawnGoddess(world, entity.posX, entity.posY, entity.posZ, flower, move);
    }

    public static boolean spawnGoddess(World world, double x, double y, double z, boolean flower, boolean move) {
        List<EntityNPCGoddess> npcs = world.getEntitiesWithinAABB(EntityNPCGoddess.class, new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F).expand(32D, 32D, 32D));
        EntityNPCGoddess goddess = npcs.size() > 0 ? npcs.get(0) : NPCHelper.getEntityForNPC(world, (NPC) HFNPCs.GODDESS);
        if (flower) goddess.setFlower();
        if (move || (goddess.posX == 0 && goddess.posY == 0 && goddess.posZ == 0)) goddess.setPosition(x, y + 1, z);
        goddess.resetSpawnHome();
        world.spawnEntityInWorld(goddess);
        return npcs.size() > 0 && npcs.get(0) == goddess;
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
                        spawnGoddess(world, event.getEntityItem(), true, true);
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
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            ItemStack held = event.getItemStack();
            if (held != null && held.getItem() == Items.STICK) {
                if (world.getBlockState(pos).getBlock() == Blocks.FLOWER_POT) {
                    TileEntityFlowerPot tile = (TileEntityFlowerPot) world.getTileEntity(pos);
                    if (tile != null && tile.getFlowerPotItem() != null) {
                        this.checkFlower(world, pos, event.getEntityPlayer(), Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER, Blocks.DOUBLE_PLANT, Blocks.TALLGRASS);
                    }
                }
            }
        }
    }

    private void checkFlower(World world, BlockPos pos, EntityPlayer player, Block... blocks) {
        Block xMinus = world.getBlockState(pos.add(-1, -1, 0)).getBlock();
        Block xPlus = world.getBlockState(pos.add(1, -1, 0)).getBlock();
        Block zMinus = world.getBlockState(pos.add(0, -1, -1)).getBlock();
        Block zPlus = world.getBlockState(pos.add(0, -1, 1)).getBlock();
        int water = 0;
        int flower = 0;

        if (xMinus == Blocks.WATER) water++;
        if (xPlus == Blocks.WATER) water++;
        if (zMinus == Blocks.WATER) water++;
        if (zPlus == Blocks.WATER) water++;
        xMinus = world.getBlockState(pos.add(-1, 0, 0)).getBlock();
        xPlus = world.getBlockState(pos.add(1, 0, 0)).getBlock();
        zMinus = world.getBlockState(pos.add(0, 0, -1)).getBlock();
        zPlus = world.getBlockState(pos.add(0, 0, 1)).getBlock();
        for (Block block : blocks) {
            if (xMinus == block)
                flower++;
            if (xPlus == block)
                flower++;
            if (zMinus == block)
                flower++;
            if (zPlus == block)
                flower++;
        }

        if (water == 2 && flower == 2) {
            if (!world.isRemote) {
                world.playEvent(2005, pos, 0);
                if (world.rand.nextInt(9) == 0) {
                    world.setBlockState(pos, HFCore.FLOWERS.getStateFromEnum(FlowerType.GODDESS));
                    world.playSound(null, player.posX, player.posY, player.posZ, HFSounds.GODDESS_SPAWN, SoundCategory.NEUTRAL, 0.5F, 1.1F);
                    player.addStat(HFAchievements.summon);
                    lastGoddess = System.currentTimeMillis();
                }
            }
        }
    }
}