package joshie.harvest.plugins;

import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static joshie.harvest.shops.HFShops.BLOODMAGE;

@ObjectHolder("BloodMagic")
@HFLoader(mods = "BloodMagic")
@SuppressWarnings("unused")
public class BloodMagic {
    public static final Item ItemSoulSnare = null;
    public static final Item ItemSoulGem = null;

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        BLOODMAGE.addPurchasable(100, new ItemStack(ItemSoulSnare));
        BLOODMAGE.addPurchasable(150, new ItemStack(Items.ROTTEN_FLESH));
        BLOODMAGE.addPurchasable(500, new ItemStack(Items.BONE));
        BLOODMAGE.addPurchasable(300, new ItemStack(Items.SPIDER_EYE));
        BLOODMAGE.addPurchasable(3000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.REGENERATION));
        BLOODMAGE.addPurchasable(4000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_REGENERATION));
        BLOODMAGE.addPurchasable(5000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_REGENERATION));
        BLOODMAGE.addPurchasable(1200, getSoulGem(32));
        BLOODMAGE.addPurchasable(2000, getSoulGem(64));

        //Make NPCs Give 0 LP
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCBuilder;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCGoddess;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCVillager;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCHuman;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCMiner;0");
    }

    @SuppressWarnings("ConstantConditions")
    private static ItemStack getSoulGem(int amount) {
        ItemStack stack = new ItemStack(ItemSoulGem);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setDouble("souls", amount);
        return stack;
    }
}


