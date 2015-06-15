package joshie.harvest.init;

import joshie.harvest.core.network.PacketCropRequest;
import joshie.harvest.core.network.PacketDismount;
import joshie.harvest.core.network.PacketFreeze;
import joshie.harvest.core.network.PacketGoldCommand;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketNewDay;
import joshie.harvest.core.network.PacketPurchaseItem;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.core.network.PacketSyncBirthday;
import joshie.harvest.core.network.PacketSyncCanProduce;
import joshie.harvest.core.network.PacketSyncCooking;
import joshie.harvest.core.network.PacketSyncCrop;
import joshie.harvest.core.network.PacketSyncFridge;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.core.network.PacketSyncMarker;
import joshie.harvest.core.network.PacketSyncMarriage;
import joshie.harvest.core.network.PacketSyncOrientation;
import joshie.harvest.core.network.PacketSyncRelationship;
import joshie.harvest.core.network.PacketSyncStats;
import joshie.harvest.core.network.PacketWateringCan;
import joshie.harvest.core.network.quests.PacketQuestCompleted;
import joshie.harvest.core.network.quests.PacketQuestDecreaseHeld;
import joshie.harvest.core.network.quests.PacketQuestSetAvailable;
import joshie.harvest.core.network.quests.PacketQuestSetCurrent;
import joshie.harvest.core.network.quests.PacketQuestSetStage;
import joshie.harvest.core.network.quests.PacketQuestStart;
import cpw.mods.fml.relauncher.Side;

public class HFPackets {
    public static void init() {
        //General Packets
        PacketHandler.registerPacket(PacketCropRequest.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncCrop.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncRelationship.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncMarriage.class, Side.CLIENT);
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
        PacketHandler.registerPacket(PacketDismount.class, Side.SERVER);
        
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
