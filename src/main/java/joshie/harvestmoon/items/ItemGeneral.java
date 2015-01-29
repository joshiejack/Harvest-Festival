package joshie.harvestmoon.items;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.AnimalHelper.canProduceProduct;
import static joshie.harvestmoon.helpers.AnimalHelper.feed;
import static joshie.harvestmoon.helpers.AnimalHelper.setProducedProduct;
import joshie.harvestmoon.helpers.SizeableHelper;
import joshie.harvestmoon.lib.SizeableMeta;
import joshie.harvestmoon.lib.SizeableMeta.Size;
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

    @Override
    public int getMetaCount() {
        return 5;
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
                    if (handler.getServer().getAnimalTracker().setCleaned(animal)) {
                        handler.getServer().getPlayerData(player).affectRelationship(animal, 25);
                    }
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
                    if (!player.inventory.addItemStackToInventory(stack)) {
                        player.dropPlayerItemWithRandomChoice(stack, false);
                    }

                    setProducedProduct(animal);
                }

                return true;
            } else if (metadata == MEDICINE) {
                if (!player.worldObj.isRemote) {
                    if (handler.getServer().getAnimalTracker().heal(animal)) {
                        stack.stackSize--;
                    }
                }

                return true;
            } else if (metadata == CHICKEN_FEED && animal instanceof EntityChicken) {
                feed(player, animal);
            }
        }

        return false;
    }
}
