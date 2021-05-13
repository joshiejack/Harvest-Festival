package uk.joshiejack.harvestcore.ticker.tree;

import uk.joshiejack.penguinlib.ticker.DailyTicker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractTreeTicker extends DailyTicker implements INBTSerializable<NBTTagCompound> {
    protected int age;

    public AbstractTreeTicker(String type) {
        super(type);
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        age++;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Age", age);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        age = tag.getInteger("Age");
    }
}
