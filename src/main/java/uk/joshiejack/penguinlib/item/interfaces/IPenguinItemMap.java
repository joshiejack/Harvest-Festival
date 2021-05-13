package uk.joshiejack.penguinlib.item.interfaces;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public interface IPenguinItemMap<E> {
    ResourceLocation getRegistryName();

    String getLocalizedName();

    ModelResourceLocation getItemModelLocation();
}
