package uk.joshiejack.penguinlib.tile.machine;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetActiveState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.tile.inventory.TileInventoryRotatable;

import javax.annotation.Nonnull;

public abstract class TileMachine extends TileInventoryRotatable implements ITickable {
    private boolean active;
    private long started;
    private long passed;

    public TileMachine(int size) {
        super(size);
    }

    //1000 = 60 minutes
    //500 = 30 minutes
    //250 = 15 minutes
    //50 = 3 minutes

    protected void startMachine(EntityPlayer player) {
        active = true;
        started = world.getTotalWorldTime();
        markDirty();
        if (!world.isRemote) {
            PenguinNetwork.sendToNearby(this, new PacketSetActiveState(pos, true));
        }
    }

    public abstract void finishMachine();

    public abstract long getOperationalTime();

    @SideOnly(Side.CLIENT)
    public void setActive(boolean active) {
        this.active = active;
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    public boolean isActive() {
        return active;
    }

    protected void checkCanStart() {}

    @Override
    public void update() {
        if (!world.isRemote && world.getTotalWorldTime() % 50 == 1) {
            if (!isActive()) checkCanStart();
            if (active && started != 0L) {
                passed += (world.getTotalWorldTime() - started);
                started = world.getTotalWorldTime(); //Reset the time
                if (passed >= getOperationalTime()) {
                    active = false;
                    passed = 0L;
                    started = 0L;
                    finishMachine();
                    markDirty();
                    PenguinNetwork.sendToNearby(this, new PacketSetActiveState(pos, false));
                }

                markDirty();
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        active = nbt.getBoolean("Active");
        started = nbt.getLong("Started");
        passed = nbt.getLong("Passed");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Active", active);
        nbt.setLong("Started", started);
        nbt.setLong("Passed", passed);
        return super.writeToNBT(nbt);
    }
}
