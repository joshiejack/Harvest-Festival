package joshie.harvestmoon.crops.icons;

import joshie.harvestmoon.api.crops.ICropRenderHandler;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerSweetPotato implements ICropRenderHandler {
    private IIcon[] stageIcons;

    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(int stage) {
        if (stage <= 3) return stageIcons[0];
        else if (stage <= 5) return stageIcons[1];
        else return stageIcons[2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        stageIcons = new IIcon[3];
        for (int i = 0; i < stageIcons.length; i++) {
            stageIcons[i] = register.registerIcon(HMModInfo.CROPPATH + HMCrops.sweet_potato.getUnlocalizedName() + "_" + (i + 1));
        }
    }
}
