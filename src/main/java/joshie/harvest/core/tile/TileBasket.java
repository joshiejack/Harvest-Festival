package joshie.harvest.core.tile;

import joshie.harvest.core.base.tile.TileSingleStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileBasket extends TileSingleStack {
    public static final Item STONE = Item.getItemFromBlock(Blocks.STONE);

    @Override
    public boolean onRightClicked(@Nullable EntityPlayer player, ItemStack place) {
        if (place.getItem() != STONE) {
            stack = place;
        }

        saveAndRefresh();
        return false;
    }
}

