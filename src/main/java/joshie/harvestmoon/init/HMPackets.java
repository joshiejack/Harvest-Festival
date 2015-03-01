package joshie.harvestmoon.init;

import joshie.harvestmoon.core.network.PacketCropRequest;
import joshie.harvestmoon.core.network.PacketFreeze;
import joshie.harvestmoon.core.network.PacketGoldCommand;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketNewDay;
import joshie.harvestmoon.core.network.PacketPurchaseItem;
import joshie.harvestmoon.core.network.PacketSetCalendar;
import joshie.harvestmoon.core.network.PacketSyncBirthday;
import joshie.harvestmoon.core.network.PacketSyncCanProduce;
import joshie.harvestmoon.core.network.PacketSyncCooking;
import joshie.harvestmoon.core.network.PacketSyncCrop;
import joshie.harvestmoon.core.network.PacketSyncFridge;
import joshie.harvestmoon.core.network.PacketSyncGold;
import joshie.harvestmoon.core.network.PacketSyncMarker;
import joshie.harvestmoon.core.network.PacketSyncOrientation;
import joshie.harvestmoon.core.network.PacketSyncRelations;
import joshie.harvestmoon.core.network.PacketSyncStats;
import joshie.harvestmoon.core.network.PacketWateringCan;
import joshie.harvestmoon.core.network.quests.PacketQuestCompleted;
import joshie.harvestmoon.core.network.quests.PacketQuestDecreaseHeld;
import joshie.harvestmoon.core.network.quests.PacketQuestSetAvailable;
import joshie.harvestmoon.core.network.quests.PacketQuestSetCurrent;
import joshie.harvestmoon.core.network.quests.PacketQuestSetStage;
import joshie.harvestmoon.core.network.quests.PacketQuestStart;
import cpw.mods.fml.relauncher.Side;

public class HMPackets {
    public static void init() {
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
        PacketHandler.registerPacket(PacketSyncBirthday.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketPurchaseItem.class, Side.SERVER);
        PacketHandler.registerPacket(PacketGoldCommand.class, Side.SERVER);
        PacketHandler.registerPacket(PacketFreeze.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncMarker.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncFridge.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketNewDay.class, Side.SERVER);
        PacketHandler.registerPacket(PacketWateringCan.class, Side.SERVER);
        
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
