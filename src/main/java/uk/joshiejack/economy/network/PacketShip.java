package uk.joshiejack.economy.network;

import com.google.common.collect.Sets;
import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.shipping.Shipping;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Set;

@PenguinLoader(side = Side.CLIENT)
public class PacketShip extends PacketSyncNBTTagCompound {
    public PacketShip(){}
    public PacketShip(NBTTagCompound tag) {
        super(tag);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Set<Shipping.HolderSold> toSell = Sets.newHashSet();
        NBTHelper.readHolderCollection(tag.getTagList("ToSell", 10), toSell);
        MinecraftForge.EVENT_BUS.register(new TrackingRenderer(toSell));
        //Merge in the newly sold items to the sold list
        Set<Shipping.HolderSold> merged = Sets.newHashSet();
        for (Shipping.HolderSold holder: toSell) {
            for (Shipping.HolderSold sold : Shipped.getSold()) {
                if (sold.matches(holder.getStack())) {
                    sold.merge(holder); //Merge in the holder
                    merged.add(holder);
                }
            }
        }

        Shipped.clearCountCache();
        toSell.stream().filter(s -> !merged.contains(s)).forEach(r -> Shipped.getSold().add(r));
    }

    @SuppressWarnings("unused") //TODO: Add sound effect for shipping, "kerching"
    public class TrackingRenderer {
        private boolean loading;
        private int ticker = 0;
        private int yOffset = 0;
        private long last;
        private final Set<Shipping.HolderSold> sold;

        TrackingRenderer(Set<Shipping.HolderSold> sold) {
            this.sold = sold;
            this.loading = true;
        }

        private void renderAt(Minecraft mc, Shipping.HolderSold stack, int x, int y) {
            StackRenderHelper.drawStack(stack.getStack(), x + 4, y - 24, 1.25F);
            mc.getTextureManager().bindTexture(GuiShop.EXTRA);
            mc.ingameGUI.drawTexturedModalRect(x + 30, y - 16, 244, 244, 12, 12);
            String text = StringHelper.convertNumberToString(stack.getValue());
            mc.fontRenderer.drawStringWithShadow(text, x + 44, y - 13, 0xFFFFFFFF);
        }

        private boolean hasFinishedOrUpdateTickerUp() {
            long current = System.currentTimeMillis();
            if (current - last >= 30) {
                if (yOffset + 1 >= sold.size() * 20) {
                    if (current - last >= 1500) {
                        last = System.currentTimeMillis();
                        return true;
                    }
                } else {
                    last = System.currentTimeMillis();
                    yOffset++;
                }
            }

            return false;
        }

        private void moveItemsDown() {
            long current = System.currentTimeMillis();
            if (current - last >= 100) {
                if (yOffset <= 0) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                } else {
                    last = System.currentTimeMillis();
                    yOffset -= 2;
                }
            }
        }

        @SubscribeEvent
        public void onGuiRender(RenderGameOverlayEvent.Pre event) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                int maxHeight = event.getResolution().getScaledHeight();
                Minecraft mc = Minecraft.getMinecraft();
                GlStateManager.pushMatrix();
                int y = 0;
                int currentY = maxHeight + (20 * sold.size()) - yOffset;
                for (Shipping.HolderSold stack: sold) {
                    renderAt(mc, stack, 0, currentY - y);
                    y += 20; //Increase the y
                }

                GlStateManager.popMatrix();
                if (loading && hasFinishedOrUpdateTickerUp()) loading = false;
                else if (!loading) moveItemsDown();
            }
        }
    }
}
