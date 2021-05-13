package uk.joshiejack.settlements.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.network.book.PacketSyncInformation;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static uk.joshiejack.settlements.Settlements.MODID;

public class ItemJournal extends ItemSingular {
    public ItemJournal() {
        super(new ResourceLocation(MODID, "journal"));
        setCreativeTab(Settlements.TAB);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (!player.isSneaking()) {
            if (!world.isRemote) {
                PenguinNetwork.sendToClient(new PacketSyncInformation(AdventureDataLoader.get(world).getInformation(player)), player);
            }

            player.openGui(Settlements.instance, Settlements.JOURNAL, world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
