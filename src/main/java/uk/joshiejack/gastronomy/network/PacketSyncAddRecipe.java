package uk.joshiejack.gastronomy.network;

import uk.joshiejack.gastronomy.cooking.RecipeBook;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncAddRecipe extends PacketSyncNBTTagCompound {
    public PacketSyncAddRecipe() {}
    public PacketSyncAddRecipe(ItemStack stack) {
        super(stack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        RecipeBook.addLearntRecipe(new HolderMeta(new ItemStack(tag)));
    }
}
