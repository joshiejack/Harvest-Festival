package uk.joshiejack.settlements.entity.ai.action.move;

import com.google.common.collect.Sets;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

@PenguinLoader("sleep")
public class ActionSleep extends ActionPhysical {
    private final Set<BlockPos> searched = Sets.newHashSet();
    private EnumFacing facing;

    @Override
    public ActionSleep withData(Object... params) {
        this.facing = (EnumFacing) params[0];
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        // Find the closest bed for this npc
        // Teleport them there and make them sleep in it


        npc.setAnimation("sleep", facing);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("Facing", (byte) facing.getIndex());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.facing = EnumFacing.byIndex(tag.getByte("Facing"));
    }
}
