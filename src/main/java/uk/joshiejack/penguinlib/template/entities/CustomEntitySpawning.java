package uk.joshiejack.penguinlib.template.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CustomEntitySpawning {
    Entity getEntity(World world, BlockPos pos, Rotation rotation, NBTTagCompound data);
}
