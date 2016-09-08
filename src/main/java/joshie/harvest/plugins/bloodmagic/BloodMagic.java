package joshie.harvest.plugins.bloodmagic;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static joshie.harvest.api.calendar.Weekday.SATURDAY;
import static joshie.harvest.api.calendar.Weekday.WEDNESDAY;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@ObjectHolder("BloodMagic")
@HFLoader(mods = "BloodMagic")
public class BloodMagic {
    public static final Item ItemSoulSnare = null;
    public static final Item ItemSoulGem = null;
    public static IShop BLOODMAGE;

    public static void init() {
        BLOODMAGE = HFApi.shops.newShop(new ResourceLocation(MODID, "bloodmage"), HFNPCs.CLOCK_WORKER);
        BLOODMAGE.addItem(100, new ItemStack(ItemSoulSnare));
        BLOODMAGE.addItem(150, new ItemStack(Items.ROTTEN_FLESH));
        BLOODMAGE.addItem(500, new ItemStack(Items.BONE));
        BLOODMAGE.addItem(300, new ItemStack(Items.SPIDER_EYE));
        BLOODMAGE.addItem(3000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.REGENERATION));
        BLOODMAGE.addItem(4000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_REGENERATION));
        BLOODMAGE.addItem(5000, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_REGENERATION));
        BLOODMAGE.addItem(1200, getSoulGem(32));
        BLOODMAGE.addItem(2000, getSoulGem(64));
        BLOODMAGE.addOpening(WEDNESDAY, 19000, 24000).addOpening(WEDNESDAY, 0, 5000).addOpening(SATURDAY, 18000, 24000).addOpening(SATURDAY, 0, 3500);

        //Make NPCs Give 0 LP
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCBuilder;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCGoddess;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCShopkeeper;0");
        FMLInterModComms.sendMessage("BloodMagic", "sacrificeValue", "EntityNPCVillager;0");
    }

    private static ItemStack getSoulGem(int amount) {
        ItemStack stack = new ItemStack(ItemSoulGem);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setDouble("souls", amount);
        return stack;
    }
}


