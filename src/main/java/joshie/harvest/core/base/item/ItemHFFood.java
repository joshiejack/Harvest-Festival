package joshie.harvest.core.base.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ItemHFFood<I extends ItemHFFood> extends ItemFood {
    public ItemHFFood() {
        this(HFTab.FARMING);
    }

    public ItemHFFood(CreativeTabs tab) {
        super(0, 0F, false);
        setCreativeTab(tab);
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return TextHelper.localize(getTranslationKey());
    }

    @Override
    @Nonnull
    public String getTranslationKey() {
        return HFModInfo.MODID + "." + super.getTranslationKey().replace("item.", "");
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 32;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.EAT;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.canEat(false) && getHealAmount(stack) > 0) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            player.getFoodStats().addStats(this, stack);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }

        return stack;
    }

    @SuppressWarnings("unchecked")
    public I register(String name) {
        setTranslationKey(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameData.register_impl(this);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            registerModels(this, name);
        }

        return (I) this;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        if (item.getHasSubtypes()) {
            NonNullList<ItemStack> subItems = NonNullList.create();
            if (item.getCreativeTabs().length > 0) {
                for (CreativeTabs tab : item.getCreativeTabs()) {
                    item.getSubItems(item, tab, subItems);
                }
            }

            for (ItemStack stack : subItems) {
                String subItemName = item.getTranslationKey(stack).replace("item.", "").replace(".", "_");
                ModelLoader.setCustomModelResourceLocation(item, item.getDamage(stack), new ModelResourceLocation(new ResourceLocation(MODID, subItemName), "inventory"));
            }
        } else {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, name), "inventory"));
        }
    }
}
