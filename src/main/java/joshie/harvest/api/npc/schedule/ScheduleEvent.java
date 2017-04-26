package joshie.harvest.api.npc.schedule;

import joshie.harvest.api.npc.NPC;
import net.minecraftforge.fml.common.eventhandler.Event;

/** Called whenever the schedule builder is used
 *  So that you can modify the schedule of existing npcs **/
public class ScheduleEvent extends Event {
    private final NPC npc;
    private final ScheduleBuilder builder;

    private ScheduleEvent(NPC npc, ScheduleBuilder builder) {
        this.npc = npc;
        this.builder = builder;
    }

    public NPC getNPC() {
        return this.npc;
    }

    public ScheduleBuilder getBuilder() {
        return this.builder;
    }

    public static class Pre extends ScheduleEvent {
        public Pre(NPC npc, ScheduleBuilder builder) {
            super(npc, builder);
        }
    }

    public static class Post extends ScheduleEvent {
        public Post(NPC npc, ScheduleBuilder builder) {
            super(npc, builder);
        }
    }
}