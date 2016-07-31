package joshie.harvest.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HFPotion extends Potion {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/potions.png");

    public HFPotion(String name, int color, int x, int y) {
        super(true, color);
        this.setPotionName(name);
        this.setIconIndex(x, y);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        return super.getStatusIconIndex();
    }
}
