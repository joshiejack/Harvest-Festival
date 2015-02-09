package joshie.harvestmoon.init;

import joshie.harvestmoon.network.PacketCropRequest;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetCalendar;
import joshie.harvestmoon.network.PacketSyncCanProduce;
import joshie.harvestmoon.network.PacketSyncCooking;
import joshie.harvestmoon.network.PacketSyncCrop;
import joshie.harvestmoon.network.PacketSyncGold;
import joshie.harvestmoon.network.PacketSyncOrientation;
import joshie.harvestmoon.network.PacketSyncRelations;
import joshie.harvestmoon.network.PacketSyncStats;
import joshie.harvestmoon.network.quests.PacketQuestCompleted;
import joshie.harvestmoon.network.quests.PacketQuestDecreaseHeld;
import joshie.harvestmoon.network.quests.PacketQuestSetAvailable;
import joshie.harvestmoon.network.quests.PacketQuestSetCurrent;
import joshie.harvestmoon.network.quests.PacketQuestSetStage;
import joshie.harvestmoon.network.quests.PacketQuestStart;
import cpw.mods.fml.relauncher.Side;

public class HMPackets {
    public static void init () {
        //General Packets
        PacketHandler.registerPacket(PacketCropRequest.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncCrop.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncRelations.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncRelations.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncStats.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCooking.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncOrientation.class, Side.CLIENT);

        //Quest Packets
        PacketHandler.registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestStart.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.SERVER);
    }
}
