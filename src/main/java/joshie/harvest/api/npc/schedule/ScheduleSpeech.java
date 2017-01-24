package joshie.harvest.api.npc.schedule;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.greeting.Script;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class ScheduleSpeech extends ScheduleElement<Script> {
    private final Script script;
    private boolean satisfied = false;

    private ScheduleSpeech(Script script) {
        this.script = script;
    }

    public static ScheduleSpeech of(Script script) {
        return new ScheduleSpeech(script);
    }

    public boolean isSatisfied(EntityAgeable npc) {
        return satisfied;
    }

    @Override
    public void execute(EntityAgeable npc) {
        if (npc.worldObj instanceof WorldServer) {
            BlockPos pos = new BlockPos(npc);
            WorldServer server = (WorldServer) npc.worldObj;
            for (EntityPlayer player : server.playerEntities) {
                EntityPlayerMP mp = ((EntityPlayerMP) player);
                if (mp.getDistanceSq(pos) < 64 * 64 && server.getPlayerChunkMap().isPlayerWatchingChunk(mp, pos.getX() >> 4, pos.getZ() >> 4)) {
                    HFApi.npc.forceScriptOpen(player, npc, script);
                }
            }

            satisfied = true;
        }
    }
}
