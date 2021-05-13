package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.husbandry.network.PacketProduced;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AnimalTraitProduct extends AnimalTraitData {
    protected int productReset; //The counter for the product reset
    protected int productsProduced; //How many produced

    public AnimalTraitProduct(String name) {
        super(name);
    }

    public void setProduced(int amount) {
        if (entity.world.isRemote) {
            this.productsProduced = amount;
        } else {
            productsProduced += amount;
            PenguinNetwork.sendToNearby(entity, new PacketProduced(entity.getEntityId(), productsProduced));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("ProductReset", productReset);
        tag.setInteger("ProductsProduced", productsProduced);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        productReset = nbt.getInteger("ProductReset");
        productsProduced = nbt.getInteger("ProductsProduced");
    }
}
