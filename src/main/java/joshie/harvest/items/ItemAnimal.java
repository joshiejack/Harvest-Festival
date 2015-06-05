package joshie.harvest.items;

import joshie.harvest.animals.EntityHarvestCow;
import joshie.harvest.animals.EntityHarvestSheep;
import joshie.harvest.core.HFTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAnimal extends ItemHFMeta {
    public static final int COW = 0;
    public static final int SHEEP = 1;
    public static final int CHICKEN = 2;

    public ItemAnimal() {
        super(HFTab.tabFarming);
    }

    public EntityAgeable getEntityFromMeta(World world, int meta) {
        switch (meta) {
            case COW:
                return new EntityHarvestCow(world);
            case SHEEP:
                return new EntityHarvestSheep(world);
            default:
                return null;
        }
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case CHICKEN:
                return "chicken";
            case SHEEP:
                return "sheep";
            case COW:
                return "cow";
            default:
                return "null";
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() < getMetaCount()) {
            EntityAgeable entity = getEntityFromMeta(world, stack.getItemDamage());
            entity.setPosition(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return false;
    }

    @Override
    public int getMetaCount() {
        return 3;
    }
}
