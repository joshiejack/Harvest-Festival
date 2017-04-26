package joshie.harvest.fishing.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.fishing.tile.TileTrap;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.item.ItemNPCTool.NPCTool;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SpecialRendererTrap extends TileSpecialRendererItem<TileTrap> {
    @Nonnull
    private static final ItemStack SPEECH = HFNPCs.TOOLS.getStackFromEnum(NPCTool.SPEECH);
    @Nonnull
    private static final ItemStack STICK = new ItemStack(Items.STICK);
    private static final Item WOOD = Item.getItemFromBlock(HFGathering.WOOD);

    @Override
    public void renderTileEntityAt(@Nonnull TileTrap tile, double x, double y, double z, float tick, int destroyStage) {
        if (!tile.isBaited()) {
            ItemStack stack = tile.getStack();
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);
                renderItem(SPEECH, 0F, 0F, 0F, 0F);
                if (stack.getItem() == WOOD) {
                    renderItem(STICK, -0.1F, 0F, 0F, 0.025F);
                } else renderItem(stack, -0.1F, 0F, 0F, 0.025F);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 1.5F, 0.5F);
        if (position != -0.1F) {
            position = 0F;
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
        } else {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }

        if (!isBlock) {
            GlStateManager.rotate(-MCClientHelper.getPlayer().rotationYaw, 0F, 1F, 0F);
            GlStateManager.translate(offset1 * 3F, offset2 * 3.5F, position * 0.75F);
        } else {
            GlStateManager.rotate(-MCClientHelper.getPlayer().rotationYaw, 0F, 1F, 0F);
            GlStateManager.translate(offset1 * 1.2F, 2F, position - 1F);
        }
    }
}
