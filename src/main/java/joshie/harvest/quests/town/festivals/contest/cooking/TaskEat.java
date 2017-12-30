package joshie.harvest.quests.town.festivals.contest.cooking;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.task.HFTask;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.core.tile.TilePlate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

@HFTask("eat")
public class TaskEat extends TaskElement {
    private BlockPos worldPos;

    public TaskEat(BlockPos worldPos) {
        this.worldPos = worldPos;
    }

    @Override
    public void execute(NPCEntity npc) {
        WorldServer worldServer = (WorldServer) npc.getAsEntity().world;
        TileEntity tile = worldServer.getTileEntity(worldPos);
        if (tile instanceof TilePlate) {
            ((TilePlate) tile).setContents(null);
        }
        satisfied = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        satisfied = tag.getBoolean("Consumed");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("Consumed", satisfied);
        return tag;
    }
}