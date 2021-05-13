package uk.joshiejack.penguinlib.item.custom;

import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItem;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemCustom extends Item implements IPenguinItem, ICustomItem {
    private final String prefix;
    private AbstractCustomData.ItemOrBlock cd;

    public ItemCustom(ResourceLocation registry, AbstractCustomData.ItemOrBlock data) {
        this.cd = data;
        setMaxStackSize(1);
        prefix = registry.getNamespace() + ".item." + registry.getPath();
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public void init() {
        this.cd.init(new ItemStack(this));
    }

    @Override
    public AbstractCustomData.ItemOrBlock getDataFromStack(ItemStack stack) {
        return cd;
    }

    @Override
    public AbstractCustomData.ItemOrBlock getDefaults() {
        return cd;
    }

    @Override
    public AbstractCustomData.ItemOrBlock[] getStates() {
        return new AbstractCustomData.ItemOrBlock[] { cd };
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (cd.getScript() != null) {
            EnumActionResult result = Scripting.getResult(cd.getScript(), "onRightClick", EnumActionResult.PASS, player, hand);
            return new ActionResult<>(result, player.getHeldItem(hand));
        } else return super.onItemRightClick(world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, cd.getModel(getRegistryName(), "inventory"));
    }
}
