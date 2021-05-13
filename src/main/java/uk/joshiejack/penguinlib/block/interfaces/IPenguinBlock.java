package uk.joshiejack.penguinlib.block.interfaces;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPenguinBlock {
    ItemBlock createItemBlock();
    @SideOnly(Side.CLIENT)
    void registerModels(Item item);
}
