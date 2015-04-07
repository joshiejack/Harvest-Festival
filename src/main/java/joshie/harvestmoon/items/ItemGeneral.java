package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.helpers.AnimalHelper.canProduceProduct;
import static joshie.harvestmoon.core.helpers.AnimalHelper.setProducedProduct;
import joshie.harvestmoon.api.core.ISizeable.Size;
import joshie.harvestmoon.core.helpers.AnimalHelper;
import joshie.harvestmoon.core.helpers.SizeableHelper;
import joshie.harvestmoon.core.lib.SizeableMeta;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemGeneral extends ItemHMMeta {
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
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living) {
        if (living instanceof EntityAnimal) {
            EntityAnimal animal = (EntityAnimal) living;
            int metadata = stack.getItemDamage();
            if (metadata == BRUSH && !(living instanceof EntityChicken)) {
                if (!player.worldObj.isRemote) {
                    AnimalHelper.clean(player, animal);
                } else {
                    for (int j = 0; j < 30D; j++) {
                        double d7 = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                        double d8 = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                        double d9 = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                        animal.worldObj.spawnParticle("townaura", d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                    }
                }

                return true;
            } else if (metadata == MILKER && animal instanceof EntityCow) {
                if (canProduceProduct(animal)) {
                    ItemStack product = SizeableHelper.getSizeable(player, animal, SizeableMeta.MILK, Size.SMALL);
                    if (!player.inventory.addItemStackToInventory(product)) {
                        player.dropPlayerItemWithRandomChoice(product, false);
                    }

                    setProducedProduct(animal);
                }

                return true;
            } else if (metadata == MEDICINE) {
                if (!player.worldObj.isRemote) {
                    if (AnimalHelper.heal(animal)) {
                        stack.stackSize--;
                    }
                }

                return true;
            } else if (metadata == MIRACLE && !(living instanceof EntityChicken)) {
                if (!living.worldObj.isRemote) {
                    if (AnimalHelper.impregnate(player, animal)) {
                        stack.stackSize--;
                    }
                }
            }
        }

        return false;
    }
}
