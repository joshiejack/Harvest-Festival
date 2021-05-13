package uk.joshiejack.harvestcore.registry.letter;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class LetterGift extends Letter {
    private final List<ItemStack> stacks = Lists.newArrayList();
    private final String[] items;

    public LetterGift(ResourceLocation resource, String text, boolean repeatable, int delay, String[] items) {
        super(resource, text, repeatable, delay);
        this.items = items;
    }

    @Override
    public boolean init() {
        for (String s: items) {
            this.stacks.add(StackHelper.getStackFromString(s));
        }

        return stacks.stream().noneMatch(ItemStack::isEmpty);
    }

    public LetterGift addGift(ItemStack stack) {
        this.stacks.add(stack);
        return this;
    }

    public ItemStack getStack() {
        if (stacks.isEmpty()) {
            addGift(StackHelper.getStackFromString(getRegistryName().toString()));
        }

        return stacks.get(0);
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    @Override
    public void renderLetter(GuiScreen gui, FontRenderer font, int x, int y, int mouseX, int mouseY) {
        super.renderLetter(gui, font, x, y, mouseX, mouseY);
        StackRenderHelper.drawStack(getStack(), 45, 100, 5F);
    }

    @Override
    public void accept(EntityPlayer player) {
        ItemHandlerHelper.giveItemToPlayer(player, getStack().copy());
    }
}
