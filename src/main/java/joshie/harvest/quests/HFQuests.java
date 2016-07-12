package joshie.harvest.quests;

import joshie.harvest.quests.packets.*;
import joshie.harvest.quests.trade.QuestBless;
import joshie.harvest.quests.tutorial.QuestChickenCare;
import joshie.harvest.quests.tutorial.QuestCowCare;
import joshie.harvest.quests.tutorial.QuestGoddess;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.network.PacketHandler.registerPacket;

public class HFQuests {
    public static final Quest TUTORIAL_FARMING = register("tutorial.farming", QuestGoddess.class);
    public static final Quest TUTORIAL_COW = register("tutorial.cow", QuestCowCare.class);
    public static final Quest TUTORIAL_CHICKEN = register("tutorial.chicken", QuestChickenCare.class);
    public static final Quest TRADE_CURSED = register("trade.cursed", QuestBless.class);

    public static void preInit() {
        registerPacket(PacketQuestCompleted.class);
        registerPacket(PacketQuestSetStage.class);
        registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        registerPacket(PacketQuestStart.class, Side.SERVER);
        registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
    }

    private static Quest register(String name, Class<? extends Quest> quest) {
        try {
            Quest q = quest.newInstance().setRegistryName(new ResourceLocation(MODID, name));
            Quest.REGISTRY.register(q);
            return q;
        } catch (Exception e) { return null; }
    }
}
