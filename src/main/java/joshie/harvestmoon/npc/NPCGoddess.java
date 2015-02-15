package joshie.harvestmoon.npc;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import net.minecraft.entity.player.EntityPlayer;

public class NPCGoddess extends NPC {
    public NPCGoddess(String name, Gender gender, Age age) {
        super(name, gender, age, new CalendarDate(8, Season.SPRING, 1));
    }

    @Override
    public boolean respawns() {
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, EntityNPC npc) {
        npc.setDead();
    }
}
