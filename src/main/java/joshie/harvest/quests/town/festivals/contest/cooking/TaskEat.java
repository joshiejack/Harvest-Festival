package joshie.harvest.quests.town.festivals.contest.cooking;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.task.HFTask;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.town.Town;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.tile.TilePlate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

@HFTask("eat")
public class TaskEat extends TaskElement {
    private BlockPos festivalPos;

    public TaskEat(Town town, BlockPos pos) {
        festivalPos = town.getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, pos);
    }

    @Override
    public void execute(NPCEntity npc) {
        TileEntity tile = npc.getAsEntity().world.getTileEntity(festivalPos);
        if (tile instanceof TilePlate) {
            ((TilePlate)tile).setContents(ItemStack.EMPTY);
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