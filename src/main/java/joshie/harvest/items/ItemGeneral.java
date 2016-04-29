package joshie.harvest.items;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IMilkable;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;

public class ItemGeneral extends ItemHFMeta implements ICreativeSorted {
    public static final int BLUE_FEATHER = 0;
    public static final int MILKER = 1;
    public static final int BRUSH = 2;
    public static final int MEDICINE = 3;
    public static final int CHICKEN_FEED = 4;
    public static final int JUNK_ORE = 5;
    public static final int COPPER_ORE = 6;
    public static final int SILVER_ORE = 7;
    public static final int GOLD_ORE = 8;
    public static final int MYSTRIL_ORE = 9;
    public static final int MYTHIC_STONE = 10;
    public static final int FLOUR = 11;
    public static final int OIL = 12;
    public static final int RICEBALL = 13;
    public static final int SALT = 14;
    public static final int MIRACLE = 15;
    public static final int CHOCOLATE = 16;

    @Override
    public int getMetaCount() {
        return 17;
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BLUE_FEATHER:
                return "feather_blue";
            case MILKER:
                return "milker";
            case BRUSH:
                return "brush";
            case MEDICINE:
                return "medicine";
            case CHICKEN_FEED:
                return "feed";
            case JUNK_ORE:
                return "ore_junk";
            case COPPER_ORE:
                return "ore_copper";
            case SILVER_ORE:
                return "ore_silver";
            case GOLD_ORE:
                return "ore_gold";
            case MYSTRIL_ORE:
                return "ore_mystril";
            case MYTHIC_STONE:
                return "stone_mythic";
            case FLOUR:
                return "flour";
            case OIL:
                return "oil";
            case RICEBALL:
                return "riceball";
            case SALT:
                return "salt";
            case MIRACLE:
                return "miracle_potion";
            case CHOCOLATE:
                return "chocolate";
            default:
                return "invalid";
        }
    }

    @Override
    public boolean isValidTab(CreativeTabs tab, int meta) {
        switch (meta) {
            case BLUE_FEATHER:
                return tab == HFTab.TOWN;
            case BRUSH:
            case MILKER:
            case MEDICINE:
            case CHICKEN_FEED:
            case MIRACLE:
                return tab == HFTab.FARMING;
            case JUNK_ORE:
            case COPPER_ORE:
            case SILVER_ORE:
            case GOLD_ORE:
            case MYSTRIL_ORE:
            case MYTHIC_STONE:
                return tab == HFTab.MINING;
            case FLOUR:
            case OIL:
            case RICEBALL:
            case SALT:
            case CHOCOLATE:
                return tab == HFTab.COOKING;
            default:
                return false;
        }
    }

    //Doesn't weeeerk
    @SideOnly(Side.CLIENT)
    public boolean setFull3D(int meta) {
        return meta == BRUSH || meta == MILKER;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return stack.getItemDamage() == MILKER ? 32 : 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return stack.getItemDamage() == MILKER ? EnumAction.BOW : EnumAction.NONE;
    }

    private HashMap<EntityPlayer, IMilkable> milkables = new HashMap<EntityPlayer, IMilkable>();

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer && stack.getItemDamage() == MILKER) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            IMilkable milkable = milkables.get(player);
            if (milkable != null) {
                milkable.milk(player);
            }
        }
        return stack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living, EnumHand hand) {
        if (living instanceof IAnimalTracked) {
            IAnimalTracked tracked = (IAnimalTracked) living;
            IAnimalData data = tracked.getData();
            int metadata = stack.getItemDamage();
            EntityAnimal animal = (EntityAnimal) living;
            if (metadata == BRUSH && !(living instanceof EntityChicken)) {
                if (!player.worldObj.isRemote) {
                    data.clean(player);
                } else {
                    for (int j = 0; j < 30D; j++) {
                        double d7 = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                        double d8 = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                        double d9 = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                        animal.worldObj.spawnParticle(EnumParticleTypes.TOWN_AURA, d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                    }
                }

                return true;
            } else if (metadata == MEDICINE) {
                if (!player.worldObj.isRemote) {
                    if (data.heal(player)) {
                        stack.stackSize--;
                    }
                }

                return true;
            } else if (metadata == MIRACLE && !(living instanceof EntityChicken)) {
                if (!living.worldObj.isRemote) {
                    if (data.impregnate(player)) {
                        stack.stackSize--;
                    }
                }

                return true;
            } else if (metadata == MILKER && living instanceof IMilkable) {
                if (((IMilkable) living).canMilk()) {
                    milkables.put(player, (IMilkable) living);
                    player.setActiveHand(hand);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        if (stack.getItemDamage() == BLUE_FEATHER) return 1;
        if (stack.getItemDamage() >= JUNK_ORE && stack.getItemDamage() <= MYTHIC_STONE) {
            return 10 + stack.getItemDamage();
        }

        return 102;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BLUE_FEATHER:
            case MIRACLE:
                return Text.AQUA + super.getItemStackDisplayName(stack);
            case MYTHIC_STONE:
                return Text.LIME + super.getItemStackDisplayName(stack);
            default:
                return Text.WHITE + super.getItemStackDisplayName(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItemDamage() == MYTHIC_STONE)
            list.add(Translate.translate("tooltip.mythic_stone"));
    }
}