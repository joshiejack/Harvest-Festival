package joshie.harvest.knowledge.item;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.knowledge.item.ItemBook.Book;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static joshie.harvest.core.handlers.GuiHandler.CALENDAR_GUI;
import static joshie.harvest.core.handlers.GuiHandler.STATS_BOOK;

public class ItemBook extends ItemHFEnum<ItemBook, Book> implements ICreativeSorted {
    public enum Book implements IStringSerializable {
        STATISTICS(STATS_BOOK), CALENDAR(CALENDAR_GUI);

        private final int guiID;

        Book(int guiID) {
            this.guiID = guiID;
        }

        public int getGuiID() {
            return guiID;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemBook() {
        super(Book.class);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isSneaking()) {
            player.openGui(HarvestFestival.instance, getEnumFromStack(stack).getGuiID(), world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return 1000;
    }
}

