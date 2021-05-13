package uk.joshiejack.settlements.npcs;

import uk.joshiejack.settlements.client.gui.NPCDisplayData;
import uk.joshiejack.settlements.npcs.gifts.GiftQuality;
import uk.joshiejack.settlements.world.town.people.Spawner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public interface NPCInfo extends NPCDisplayData, Spawner.Worker {
    ResourceLocation getSkin();
    int getOutsideColor();
    int getInsideColor();
    String getGreeting(Random random);
    GiftQuality getGiftQuality(ItemStack stack);
    String substring(String name);
    int getData(String name);
    void callScript(String function, Object... params);
}
