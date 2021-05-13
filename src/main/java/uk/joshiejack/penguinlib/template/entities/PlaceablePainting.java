package uk.joshiejack.penguinlib.template.entities;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@PenguinLoader("painting")
public class PlaceablePainting extends PlaceableHanging {
    private String painting;

    public PlaceablePainting() {}
    public PlaceablePainting(String name, EnumFacing facing, BlockPos position) {
        super(facing, position);
        this.painting = name;
    }

    @Override
    public void remove(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, IBlockState replacement) {
        if (canPlace(stage)) {
            BlockPos transformed = BlockPosHelper.getTransformedPosition(this.pos, pos, rotation);
            EntityHelper.getEntities(EntityPainting.class, world, transformed, 0.5D, 0.5D).forEach(Entity::setDead);
        }
    }

    @Override
    public EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing) {
        EntityPainting painting = new EntityPainting(world, pos, facing);
        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values()) {
            if (entitypainting$enumart.title.equals(this.painting)) {
                painting.art = entitypainting$enumart;
                break;
            }
        }

        return painting;
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return EntityPainting.class;
    }

    @Override
    public PlaceablePainting getCopyFromEntity(Entity e, BlockPos position) {
        EntityPainting p = (EntityPainting) e;
        return new PlaceablePainting(p.art.title, p.facingDirection, position);
    }
}