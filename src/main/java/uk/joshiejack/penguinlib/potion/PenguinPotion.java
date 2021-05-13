package uk.joshiejack.penguinlib.potion;

import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PenguinPotion extends Potion {
    private final ResourceLocation texture;

    public PenguinPotion(ResourceLocation resource, int color, int x, int y) {
        super(true, color);
        this.texture = GuiElements.getTexture(resource.getNamespace(), "potions");
        RegistryHelper.setRegistryAndName(resource, this);
        this.setIconIndex(x, y);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        return super.getStatusIconIndex();
    }
}
