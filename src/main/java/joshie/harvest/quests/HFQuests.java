package joshie.harvest.quests;

import joshie.harvest.quests.packets.*;
import net.minecraftforge.fml.relauncher.Side;

import static joshie.harvest.core.network.PacketHandler.registerPacket;

public class HFQuests {
    public static void preInit() {
        registerPacket(PacketQuestCompleted.class);
        registerPacket(PacketQuestIncrease.class);
        registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        registerPacket(PacketQuestStart.class, Side.SERVER);
        registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
    }

}
