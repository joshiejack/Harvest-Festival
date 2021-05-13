package uk.joshiejack.gastronomy.network;

import uk.joshiejack.gastronomy.cooking.RecipeBook;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncLearntRecipes extends PacketSyncNBTTagCompound {
    public PacketSyncLearntRecipes() {}
    public PacketSyncLearntRecipes(Collection<HolderMeta> collection) {
        super(NBTHelper.writeHolderCollectionToTag(collection));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        RecipeBook.setLearntRecipes(NBTHelper.readHolderCollectionFromTag(tag));
    }
}
