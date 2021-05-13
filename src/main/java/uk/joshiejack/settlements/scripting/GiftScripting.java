package uk.joshiejack.settlements.scripting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.npcs.gifts.GiftCategory;
import uk.joshiejack.settlements.npcs.gifts.GiftQuality;
import uk.joshiejack.settlements.npcs.gifts.GiftRegistry;
import uk.joshiejack.settlements.scripting.wrappers.EntityNPCJS;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class GiftScripting {
    private static final Random rand = new Random(System.currentTimeMillis());

    @SubscribeEvent
    public static void onFunctionLoading(CollectScriptingFunctions event) {
        event.registerVar("gifts", GiftScripting.class);
    }

    public static ItemStackJS random(String category) {
        NonNullList<ItemStack> stacks = GiftRegistry.CATEGORY_REGISTRY.random(GiftCategory.get(category)).getStacks();
        return WrapperRegistry.wrap(stacks.get(rand.nextInt(stacks.size())));
    }

    public static GiftQuality quality(EntityNPCJS npcWrapper, ItemStackJS wrapper) {
        return npcWrapper.penguinScriptingObject.getInfo().getGiftQuality(wrapper.penguinScriptingObject);
    }

    public static GiftCategory category(ItemStackJS wrapper) {
        return GiftRegistry.CATEGORY_REGISTRY.getValue(wrapper.penguinScriptingObject);
    }
}
