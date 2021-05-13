package uk.joshiejack.penguinlib.item.custom;

import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItem;
import uk.joshiejack.penguinlib.data.custom.item.CustomItemEdibleData;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemCustomEdible extends ItemFood implements IPenguinItem, ICustomItem {
    private final String prefix;
    private CustomItemEdibleData cd;

    public ItemCustomEdible(ResourceLocation registry, CustomItemEdibleData cd) {
        super(0, 0.0F, false);
        this.cd = cd;
        setMaxStackSize(1);
        prefix = registry.getNamespace() + ".item." + registry.getPath();
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
        if (cd.alwaysEdible) setAlwaysEdible();
    }

    @Override
    public void init() {
        this.cd.init(new ItemStack(this));
    }

    @Override
    public AbstractCustomData.ItemOrBlock getDefaults() {
        return cd;
    }

    @Override
    public AbstractCustomData.ItemOrBlock[] getStates() {
        return new AbstractCustomData.ItemOrBlock[] { cd };
    }

    @Override
    public CustomItemEdibleData getDataFromStack(ItemStack stack) {
        return cd;
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return cd.hunger == -1 ? 1 : cd.hunger;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return cd.saturation == -1 ? 0.6F : cd.saturation;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return cd.consumeTime == -1 ? 32 : cd.consumeTime;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return cd.action == null ? EnumAction.EAT : cd.action;
    }

    @Nonnull
    protected ItemStack getLeftovers(ItemStack stack) {
        return cd.getLeftovers();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (cd.getScript() != null) {
            Scripting.callFunction(cd.getScript(), "onFoodEaten", player, stack);
        } else {
            ItemStack leftovers = getLeftovers(stack);
            if (leftovers.isEmpty() && stack.getCount() > 0) {
                player.addItemStackToInventory(leftovers);
            }
        }
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, cd.getModel(getRegistryName(), "inventory"));
    }
}
