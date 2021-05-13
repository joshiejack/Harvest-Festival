package uk.joshiejack.penguinlib.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class PenguinTab extends CreativeTabs {
    private final CreateIcon icon;

    public PenguinTab(String modid, CreateIcon icon) {
        super(modid + ".tab");
        this.icon = icon;
    }

    @Override
    public ItemStack createIcon() {
        return icon.createIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public String getTranslationKey() {
        return getTabLabel();
    }

    public interface CreateIcon {
        ItemStack createIcon();
    }
}
