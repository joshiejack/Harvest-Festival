package uk.joshiejack.harvestcore.network.blueprint;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

@PenguinLoader
public class PacketCraftBlueprint extends PacketSendPenguin<Blueprint> {
    private int multi;
    public PacketCraftBlueprint() {
        super(Blueprint.REGISTRY);
    }
    public PacketCraftBlueprint(Blueprint blueprint, int multi) {
        super(Blueprint.REGISTRY, blueprint);
        this.multi = multi;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeInt(multi);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        multi = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player, Blueprint blueprint) {
        if (!player.world.isRemote) {
            int crafted = 0;
            for (int i = 0; i < multi; i++) {
                if (blueprint.hasAllItems(player, 1)) {
                    blueprint.craft(player);
                    crafted++;
                }
            }

            //Server side
            if (crafted > 0) {
                //player.openGui(PenguinLib.instance, 0, player.world, 0, 0, 0);
                ItemStack held = player.inventory.getItemStack();
                ItemStack result = StackHelper.toStack(blueprint.getResult(), crafted * blueprint.getResult().getCount());
                if (ItemStack.areItemsEqual(held, result) && ItemStack.areItemStackTagsEqual(held, result)) {
                    held.setCount(held.getCount() + result.getCount());
                    PenguinNetwork.sendToClient(new PacketCraftBlueprint(blueprint, crafted), player); //Send the confirmation back to the client
                } else if (held.isEmpty()) {
                    player.inventory.setItemStack(result);
                    PenguinNetwork.sendToClient(new PacketCraftBlueprint(blueprint, crafted), player); //Send the confirmation back to the client
                } else ItemHandlerHelper.giveItemToPlayer(player, result);
            }
        } else { //Client side
            ItemStack held = player.inventory.getItemStack();
            ItemStack result = StackHelper.toStack(blueprint.getResult(), multi * blueprint.getResult().getCount());
            if (ItemStack.areItemsEqual(held, result) && ItemStack.areItemStackTagsEqual(held, result)) {
                held.setCount(held.getCount() + result.getCount());
            } else if (held.isEmpty()) {
                player.inventory.setItemStack(result);
            } else ItemHandlerHelper.giveItemToPlayer(player, result);

            for (int i = 0; i < multi; i++) {
                if (blueprint.hasAllItems(player, 1)) blueprint.craft(player);
            }
        }
    }
}