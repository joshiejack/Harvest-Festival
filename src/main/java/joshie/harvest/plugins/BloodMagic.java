package joshie.harvest.plugins;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@ObjectHolder("bloodmagic")
@HFLoader(mods = "bloodmagic")
public class BloodMagic {
    public static final ItemStack ItemSoulSnare = null;
    public static final ItemStack ItemSoulGem = null;
    public static IShop BLOODMAGE;

    public static void init() {
        BLOODMAGE = HFApi.shops.newShop(new ResourceLocation(MODID, "bloodmage"), HFNPCs.CLOCK_WORKER);
        BLOODMAGE.addItem(100, ItemSoulSnare);
        BLOODMAGE.addItem(150, new ItemStack(Items.ROTTEN_FLESH));
        BLOODMAGE.addItem(500, new ItemStack(Items.BONE));
        BLOODMAGE.addItem(300, new ItemStack(Items.SPIDER_EYE));
        BLOODMAGE.addItem(3000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.REGENERATION));
        BLOODMAGE.addItem(4000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_REGENERATION));
        BLOODMAGE.addItem(5000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_REGENERATION));
        //TODO: unfilled or semi-filled tartaric gems, I don't wanna api, so need to investigate the nbt data myself :P
        //BLOODMAGE.addItem(100, ItemSoulGem);
    }
}


