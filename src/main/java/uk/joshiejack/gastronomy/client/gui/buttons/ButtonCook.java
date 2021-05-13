package uk.joshiejack.gastronomy.client.gui.buttons;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.client.gui.GuiCookbook;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.cooking.IngredientStack;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.gastronomy.network.PacketCookRecipe;
import uk.joshiejack.gastronomy.tile.base.TileCooking;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ButtonCook extends ButtonBook {
    private final Appliance appliance;
    private final Recipe recipe;

    public ButtonCook(Appliance appliance, Recipe recipe, GuiBook gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.width = 66;
        this.height = 34;
        this.recipe = recipe;
        this.appliance = appliance;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mc.getTextureManager().bindTexture(GuiCookbook.LEFT_GUI);
            GlStateManager.color(1F, 1F, 1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, 0, (hovered ? 135 : 101), 66, 34);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "" + StringHelper.localize("gastronomy.cook"), x + 19, y + 14, 4210752);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        //Check if there is a relevant machine nearby
        TileCooking cooking = Cooker.getNearbyAppliance(player, appliance);
        if (cooking == null) {
            //TODO: Complain about not finding the right appliance
            return;
        } else {
            TileCooking.PlaceIngredientResult result = cooking.hasPrereqs();
            if (result != TileCooking.PlaceIngredientResult.SUCCESS) {
                //TODO: If we're missing the right stuff below then complain
                return;
            }
        }

        List<IItemHandlerModifiable> inventories = Cooker.getFoodStorageAndPlayer(player);
        NonNullList<ItemStack> stacks = InventoryHelper.getAllStacks(inventories);
        List<FluidStack> fluids = InventoryHelper.getAllFluids(inventories);
        for (IngredientStack ingredients : recipe.getRequired()) {
            if (!Cooker.isInInventories(stacks, fluids, ingredients)) {
                //TODO: Display a missing ingredient message
                return;
            }
        }

        //All ingredients and appliances are here
        PenguinNetwork.sendToServer(new PacketCookRecipe(new HolderMeta(recipe.getResult()), appliance));
    }
}
