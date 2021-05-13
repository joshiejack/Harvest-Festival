package uk.joshiejack.penguinlib.template.entities;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.LootHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

@PenguinLoader("item_frame")
public class PlaceableItemFrame extends PlaceableHanging {
    private ResourceLocation chestType;
    private ItemStack stack = ItemStack.EMPTY;
    private int rotation;

    public PlaceableItemFrame() {}
    public PlaceableItemFrame(ResourceLocation chestType, ItemStack stack, int rotation, EnumFacing facing, BlockPos position) {
        super(facing, position);
        this.chestType = chestType;
        this.stack = stack;
        this.rotation = rotation;
    }

    @Override
    public void remove(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, IBlockState replacement) {
        if (canPlace(stage)) {
            BlockPos transformed = BlockPosHelper.getTransformedPosition(this.pos, pos, rotation);
            EntityHelper.getEntities(EntityItemFrame.class, world, transformed, 0.5D, 0.5D).forEach(Entity::setDead);
        }
    }

    @Override
    public EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing) {
        EntityItemFrame frame = new EntityItemFrame(world, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), facing);
        ItemStack loot = ItemStack.EMPTY;

        if (!stack.isEmpty()) loot = stack.copy();
        if (chestType != null && LootTableList.getAll().contains(chestType)) {
            loot = LootHelper.getStack(world, null, chestType, world.rand);
        }

        frame.setDisplayedItem(loot);
        frame.setItemRotation(this.rotation);
        return frame;
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return EntityItemFrame.class;
    }

    @Override
    public PlaceableItemFrame getCopyFromEntity(Entity e, BlockPos position) {
        EntityItemFrame frame = (EntityItemFrame) e;

        ResourceLocation chestType = null;
        ItemStack stack = frame.getDisplayedItem();
        if (!stack.isEmpty() && stack.hasDisplayName()) {
            chestType = new ResourceLocation("harvestfestival", "frames/" + stack.getDisplayName());
            stack = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
        }

        return new PlaceableItemFrame(chestType, stack, frame.getRotation(), frame.facingDirection, position);
    }
}