package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.buildings.placeable.PlaceableHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableItemFrame extends PlaceableHanging {
    private ItemStack stack;
    private int rotation;
    private String chestType;

    public PlaceableItemFrame() {
        super(EnumFacing.DOWN, BlockPos.ORIGIN);
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, BlockPos offsetPos) {
        super(facing, offsetPos);
        this.stack = stack;
        this.rotation = rotation;
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, int offsetX, int offsetY, int offsetZ) {
        this(stack, rotation, facing, new BlockPos(offsetX, offsetY, offsetZ));
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, BlockPos offsetPos, String chestType) {
        this(stack, rotation, facing, offsetPos);
        this.chestType = chestType;
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, EnumFacing facing, int offsetX, int offsetY, int offsetZ, String chestType) {
        this(stack, rotation, facing, new BlockPos(offsetX, offsetY, offsetZ), chestType);
    }

    @Override
    public Entity getEntity(UUID uuid, World world, BlockPos pos, boolean n1, boolean n2, boolean swap) {
        EnumFacing facing = getFacing(n1, n2, swap);
        EntityItemFrame frame = new EntityItemFrame(world, new BlockPos(getX(pos.getX(), facing), pos.getY(), getZ(pos.getZ(), facing)), EnumFacing.values()[facing.ordinal()]);
        ItemStack loot = null;

        if (stack != null) loot = stack.copy();
        if (chestType != null) {
            /*try {
                WeightedRandomChestContent chest = (WeightedRandomChestContent) WeightedRandom.getRandomItem(world.rand, ChestGenHooks.getItems(chestType, world.rand)); //TODO
                while (loot == null) {
                    ItemStack[] stacks = ChestGenHooks.generateStacks(world.rand, chest.theItemId, chest.minStackSize, chest.maxStackSize);
                    if (stacks != null && stacks.length >= 1) {
                        loot = stacks[0].copy();
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }*/
        }

        frame.setDisplayedItem(loot);
        frame.setItemRotation(rotation);
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
        return "list.add(new PlaceableItemFrame(" + getItemStack(frame.getDisplayedItem()) + ", " + frame.getRotation() + ", " + frame.facingDirection + ", " + pos + "));";
    }
}