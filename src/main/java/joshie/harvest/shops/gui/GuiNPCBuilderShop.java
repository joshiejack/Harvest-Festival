package joshie.harvest.shops.gui;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketPurchaseItem;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.shops.purchaseable.PurchaseableBuilding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiNPCBuilderShop extends GuiNPCShop {
    public GuiNPCBuilderShop(EntityNPC npc, EntityPlayer player) {
        super(npc, player);
    }

    @Override
    public int getIncrease() {
        return 1;
    }

    @Override
    public int getMax() {
        return 5;
    }

    private static final ItemStack log = new ItemStack(Blocks.LOG);
    private static final ItemStack stone = new ItemStack(Blocks.STONE);

    @Override
    protected void drawShelves(int x, int y) {
        int index = 0;
        for (int i = start; i < contents.size(); i++) {
            if (index > 4) break;
            PurchaseableBuilding purchaseable = (PurchaseableBuilding) contents.get(i);
            ItemStack display = purchaseable.getDisplayStack();
            long cost = purchaseable.getCost();
            mc.renderEngine.bindTexture(shelve_texture);
            int y37 = y + 37;

            int posY = 41;

            drawTexturedModalRect(x + 29, y + posY + (index * 34), 0, 0, 32, 32);
            drawTexturedModalRect(x + 29 + 32, y + posY + (index * 34), 32, 0, 32, 32);
            drawTexturedModalRect(x + 29 + 64, y + posY + (index * 34), 32, 0, 32, 32);
            drawTexturedModalRect(x + 29 + 96, y + posY + (index * 34), 32, 0, 32, 32);
            drawTexturedModalRect(x + 29 + 128, y + posY + (index * 34), 32, 0, 32, 32);
            drawTexturedModalRect(x + 29 + 160, y + posY + (index * 34), 64, 0, 32, 32);
            int xOffset = 0;
            int posX = 190;

            if (mouseY >= posY + 26 + (index * 34) && mouseY <= posY + 46 + (index * 34) && mouseX >= 34 && mouseX <= 56) {
                List list = new ArrayList();
                purchaseable.addTooltip(list);
                addTooltip(list);
            }

            if (mouseY >= posY + 20 + (index * 34) && mouseY <= posY + 52 + (index * 34) && mouseX >= posX && mouseX <= posX + 32) {
                xOffset = 32;
            }

            drawTexturedModalRect(x + posX, y + posY + (index * 34), xOffset, 32, 32, 32);
            StackHelper.drawStack(display, x + 34, y + 46 + (index * 34), 1.4F);
            mc.renderEngine.bindTexture(HFModInfo.elements);
            drawTexturedModalRect(x + 34 + 100, y + 54 + (index * 34), 244, 0, 12, 12);
            mc.fontRendererObj.drawStringWithShadow("" + cost, x + 148, y + 57 + (index * 34), 0xC39753);

            //Wood
            StackHelper.drawStack(log, x + 56, y + 55 + (index * 34), 0.75F);
            mc.fontRendererObj.drawStringWithShadow("" + purchaseable.getLogCost(), x + 69, y + 57 + (index * 34), 0xC39753);

            //Stone
            StackHelper.drawStack(stone, x + 95, y + 55 + (index * 34), 0.75F);
            mc.fontRendererObj.drawStringWithShadow("" + purchaseable.getStoneCost(), x + 108, y + 57 + (index * 34), 0xC39753);

            mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.BOLD + purchaseable.getName(), x + 60, y + 46 + (index * 34), 0xC39753);

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            index++;

            if (index >= 10) {
                break;
            }
        }
    }

    @Override
    protected void onMouseClick(int x, int y) {
        int index = 0;
        for (int i = start; i < contents.size(); i++) {
            if (index > 4) break;
            PurchaseableBuilding purchaseable = (PurchaseableBuilding) contents.get(i);
            if (purchaseable.canBuy(player.worldObj, player)) {
                long cost = purchaseable.getCost();
                if (HFTrackers.getClientPlayerTracker().getStats().getGold() - purchaseable.getCost() >= 0) {
                    if (mouseY >= 61 + (index * 34) && mouseY <= 93 + (index * 34) && mouseX >= 190 && mouseX <= 222) {
                        PacketHandler.sendToServer(new PacketPurchaseItem(purchaseable));
                    }
                }
            }

            index++;

            if (index >= 10) {
                break;
            }
        }
    }
}
