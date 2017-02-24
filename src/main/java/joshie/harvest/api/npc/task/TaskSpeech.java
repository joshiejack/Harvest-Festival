package joshie.harvest.api.npc.task;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.greeting.Script;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class TaskSpeech extends TaskElement {
    private Script script;

    private TaskSpeech(Script script) {
        this.script = script;
    }

    public static TaskSpeech of(Script script) {
        return new TaskSpeech(script);
    }

    @Override
    public void execute(EntityAgeable npc) {
        BlockPos pos = new BlockPos(npc);
        WorldServer server = (WorldServer) npc.worldObj;
        for (EntityPlayer player : server.playerEntities) {
            EntityPlayerMP mp = ((EntityPlayerMP) player);
            if (mp.getDistanceSq(pos) < 64 * 64) {
                HFApi.npc.forceScriptOpen(player, npc, script);
            }
        }

        super.execute(npc);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        script = Script.REGISTRY.getValue(new ResourceLocation(tag.getString("Script")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("Script", script.getRegistryName().toString());
        return tag;
    }
}
