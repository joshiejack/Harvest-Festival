package uk.joshiejack.penguinlib.item.custom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.item.CustomItemGuideData;
import uk.joshiejack.penguinlib.events.UniversalGuidePacketEvent;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import uk.joshiejack.penguinlib.network.PenguinNetwork;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemCustomGuide extends ItemSingular {
    private CustomItemGuideData cd;

    public ItemCustomGuide(ResourceLocation registryName, CustomItemGuideData main) {
        super(registryName);
        cd = main;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (!player.isSneaking()) {
            if (!world.isRemote) {
                UniversalGuidePacketEvent event = new UniversalGuidePacketEvent(player);
                MinecraftForge.EVENT_BUS.post(event);
                if (event.getPacket() != null) {
                    PenguinNetwork.sendToClient(event.getPacket().setHand(hand.ordinal()), player);
                } else player.openGui(PenguinLib.instance, 0, world, hand.ordinal(), 0, 0);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    public List<CustomItemGuideData.Tab> tabs() {
        return cd.tabs;
    }
}
