package uk.joshiejack.harvestcore.client.renderer.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.harvestcore.client.mail.Inbox;
import uk.joshiejack.harvestcore.tile.TileMailbox;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.base.ItemSpecial;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRendererBubble;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@SuppressWarnings("ConstantConditions")
@SideOnly(Side.CLIENT)
@PenguinLoader(value = "mailbox", side = Side.CLIENT)
public class SpecialRendererMailbox extends SpecialRendererBubble<TileMailbox> {
    private static ItemStack MAIL = null;

    @Override
    public ItemStack getStack(TileMailbox tile) {
        if (MAIL == null) {
            MAIL = PenguinItems.SPECIAL.getStackFromEnum(ItemSpecial.Special.MAIL);
        }

        return MAIL;
    }

    @Override
    public boolean shouldRender(TileMailbox tile) {
        return Inbox.getLetters().size() > 0;
    }

    @Override
    public float getYOffset() {
        return 1.15F;
    }
}
