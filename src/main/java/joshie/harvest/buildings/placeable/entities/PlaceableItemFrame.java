package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.buildings.placeable.PlaceableHelper;
import joshie.harvest.core.helpers.LootHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static net.minecraft.util.EnumFacing.NORTH;

public class PlaceableItemFrame extends PlaceableHanging {
    private ResourceLocation chestType;
    private ItemStack stack;
    private int rotation;

    public PlaceableItemFrame() {
        super(NORTH, BlockPos.ORIGIN);
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, BlockPos offsetPos) {
        super(facing, offsetPos);
        this.rotation = rotation;
        this.stack = stack;
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, int offsetX, int offsetY, int offsetZ) {
        this(stack, rotation, facing, new BlockPos(offsetX, offsetY, offsetZ));
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, BlockPos offsetPos, ResourceLocation chestType) {
        this(stack, rotation, facing, offsetPos);
        this.chestType = chestType;
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, int offsetX, int offsetY, int offsetZ, ResourceLocation chestType) {
        this(stack, rotation, facing, new BlockPos(offsetX, offsetY, offsetZ), chestType);
    }

    @Override
    public EntityHanging getEntityHanging(UUID owner, World world, BlockPos pos, EnumFacing facing) {
        EntityItemFrame frame = new EntityItemFrame(world, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), facing);
        ItemStack loot = null;

        if (stack != null) loot = stack.copy();
        if (chestType != null) {
            EntityPlayer player = world.getPlayerEntityByUUID(owner);
            loot = LootHelper.getStack(world, player, chestType);
        }

        frame.setDisplayedItem(loot);
        frame.setItemRotation(this.rotation);
        return frame;
    }

    public String getItemStack(ItemStack stack) {
        if (stack == null) {
            return "null";
        }

        String name = PlaceableHelper.getBestGuessName(stack);
        return "new ItemStack(" + name + ", 1, " + stack.getItemDamage() + ")";
    }

    @Override
    public String getStringFor(Entity e, BlockPos pos) {
        EntityItemFrame frame = (EntityItemFrame) e;
        return "list.add(new PlaceableItemFrame(" + getItemStack(frame.getDisplayedItem()) + ", " + frame.getRotation() + ", EnumFacing." + frame.facingDirection.name().toUpperCase() +
                ", new BlockPos(" + pos.getX() + ", " + pos.getY() + "," + pos.getZ() + ")));";
    }
}