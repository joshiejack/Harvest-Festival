package joshie.harvest.crops.icons;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.init.HFCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerStrawberry extends AbstractIconHandler {
    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(PlantSection section, int stage) {
        if (stage <= 3) return stageIcons[0];
        else if (stage <= 6) return stageIcons[1];
        else if (stage <= 8) return stageIcons[2];
        else return stageIcons[3];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        stageIcons = new IIcon[4];
        for (int i = 0; i < stageIcons.length; i++) {
            stageIcons[i] = register.registerIcon(HFModInfo.CROPPATH + HFCrops.strawberry.getUnlocalizedName() + "_" + (i + 1));
        }
    }
}
