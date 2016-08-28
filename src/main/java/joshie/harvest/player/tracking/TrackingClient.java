package joshie.harvest.player.tracking;

import joshie.harvest.core.util.holder.ItemStackHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class TrackingClient extends Tracking {
    public Set<ResourceLocation> getLearntRecipes() {
        return recipes;
    }

    public void setObtained(Set<ItemStackHolder> obtained) {
        this.obtained = obtained;
    }

    public void setRecipes(Set<ResourceLocation> recipes) {
        this.recipes = recipes;
    }
}