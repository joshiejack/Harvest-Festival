package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.quests.packets.*;
import joshie.harvest.quests.trade.QuestBless;
import joshie.harvest.quests.tutorial.QuestGoddess;
import net.minecraftforge.fml.relauncher.Side;

import static joshie.harvest.core.network.PacketHandler.registerPacket;

public class HFQuests {
    public static void preInit() {
        HFApi.quests.register("tutorial.farming", new QuestGoddess());
        HFApi.quests.register("trade.cursed", new QuestBless());
        //HFApi.quests.register("tutorial.cow", new QuestCowCare());
        //HFApi.quests.register("tutorial.chicken", new QuestChickenCare());

        registerPacket(PacketQuestCompleted.class);
        registerPacket(PacketQuestSetStage.class);
        registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        registerPacket(PacketQuestStart.class, Side.SERVER);
        registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
    }
}
