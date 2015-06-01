package joshie.harvest.crops.icons;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.init.HFCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerSpinach extends AbstractIconHandler {
    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(PlantSection section, int stage) {
        if (stage <= 2) return stageIcons[0];
        else if (stage <= 5) return stageIcons[1];
        else return stageIcons[2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        stageIcons = new IIcon[3];
        for (int i = 0; i < stageIcons.length; i++) {
            stageIcons[i] = register.registerIcon(HFModInfo.CROPPATH + HFCrops.spinach.getUnlocalizedName() + "_" + (i + 1));
        }
    }
}
