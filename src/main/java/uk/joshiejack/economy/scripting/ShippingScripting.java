package uk.joshiejack.economy.scripting;

import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.economy.event.ItemShippedEvent;
import uk.joshiejack.economy.shipping.Market;
import uk.joshiejack.economy.shipping.Shipping;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingMethods;
import uk.joshiejack.penguinlib.scripting.event.ScriptingTriggerFired;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.util.helpers.forge.CapabilityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import static uk.joshiejack.economy.Economy.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ShippingScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("shipping", ShippingScripting.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingMethods(CollectScriptingMethods event) {
        event.add("onItemShipped");
    }

    @SubscribeEvent
    public static void onShipping(ItemShippedEvent event) { //Only ever called server side
        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onItemShipped", event.getWorld(), event.getTeam(), event.getShipped(), event.getValue()));
    }

    @SuppressWarnings("unchecked")
    public static long value(ItemStackJS wrapper) {
        return ShippingRegistry.INSTANCE.getValue(wrapper.penguinScriptingObject);
    }

    public static void ship(PlayerJS playerWrapper, ItemStackJS stackWrapper, int count) {
        ItemStack stack = stackWrapper.penguinScriptingObject;
        Shipping shipping = Market.get(playerWrapper.penguinScriptingObject.world).getShippingForPlayer(playerWrapper.penguinScriptingObject);
        IItemHandler handler = CapabilityHelper.getCapabilityFromStack(stack, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (handler instanceof IItemHandlerModifiable) {
            IItemHandlerModifiable inventory = ((IItemHandlerModifiable) handler);
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack inSlot = inventory.getStackInSlot(i);
                long value = ShippingRegistry.INSTANCE.getValue(inSlot);
                if (value > 0) {
                    shipping.add(inSlot);
                    inventory.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        } else {
            ItemStack shipped = stack.copy();
            shipped.setCount(count);
            shipping.add(shipped);
            stack.shrink(count); //Shrink the actual stack
        }
    }

    public static int getSoldCount(PlayerJS playerW, ItemStackJS wrapper) {
        ItemStack stack = wrapper.penguinScriptingObject;
        EntityPlayer player = playerW.penguinScriptingObject;
        return player.world.isRemote ? Shipped.getCount(stack) :
                Market.get(player.world).getShippingForPlayer(player).getCount(stack);
    }
}
