package uk.joshiejack.harvestcore.world.storage;

import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class WildernessPos extends BlockPos implements INBTSerializable<NBTTagString> {
    private IBlockState state;

    public WildernessPos(IBlockState state, BlockPos pos) {
        super(pos);
        this.state = state;
    }

    public IBlockState getState() {
        return state;
    }

    @Override
    public NBTTagString serializeNBT() {
        String s = state.toString().replace("[", ",").replace("]", "");
        if (s.startsWith("minecraft:")) s = s.replace("minecraft:", "");
        return new NBTTagString(s);
    }

    @Override
    public void deserializeNBT(NBTTagString nbt) {
        state = StateAdapter.fromString(nbt.getString());
    }
}
