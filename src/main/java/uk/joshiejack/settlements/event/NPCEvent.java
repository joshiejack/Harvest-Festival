package uk.joshiejack.settlements.event;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.quest.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class NPCEvent extends PlayerEvent {
    private final EntityNPC npcEntity;

    public NPCEvent(EntityNPC npcEntity, EntityPlayer player) {
        super(player);
        this.npcEntity = npcEntity;
    }

    public EntityNPC getNPCEntity() {
        return npcEntity;
    }

    public static class NPCRightClickedEvent extends NPCEvent {
        private final EnumHand hand;

        public NPCRightClickedEvent(EntityNPC npcEntity, EntityPlayer player, EnumHand hand) {
            super(npcEntity, player);
            this.hand = hand;
        }

        public EnumHand getHand() {
            return hand;
        }
    }

    public static class NPCFinishedSpeakingEvent extends NPCEvent {
        private final Quest script;

        public NPCFinishedSpeakingEvent(EntityNPC npcEntity, EntityPlayer player, Quest script) {
            super(npcEntity, player);
            this.script = script;
        }

        public Quest getScript() {
            return script;
        }
    }
}
