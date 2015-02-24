package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.helpers.CropHelper.getCropFromStack;
import static joshie.harvestmoon.core.helpers.CropHelper.getCropQuality;
import static joshie.harvestmoon.core.helpers.CropHelper.getCropType;
import static joshie.harvestmoon.core.helpers.CropHelper.isGiant;
import static joshie.harvestmoon.core.helpers.CropHelper.plantCrop;
import static joshie.harvestmoon.core.lib.HMModInfo.SEEDPATH;

import java.util.List;

import joshie.harvestmoon.api.interfaces.IRateable;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.lib.CropMeta;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSeeds extends ItemHMMeta implements IRateable {
    private IIcon seed_bag_body;
    private IIcon seed_bag_neck;

    public ItemSeeds() {
        setTextureFolder(SEEDPATH);
    }

    @Override
    public int getMetaCount() {
        return CropMeta.values().length;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return CropHelper.getCropFromStack(stack).getSeedsName(isGiant(stack.getItemDamage()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        Crop crop = CropHelper.getCropFromStack(stack);
        int quality = getCropQuality(stack.getItemDamage());
        for (Season season : crop.getSeasons()) {
            list.add(season.getTextColor() + season.getLocalized());
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (side != 1) {
            return false;
        } else {
            Crop crop = CropHelper.getCropFromStack(stack);
            int quality = CropHelper.getCropQuality(stack.getItemDamage());
            int y = yCoord;
            int planted = 0;
            labelTop: for (int x = xCoord - 1; x <= xCoord + 1; x++) {
                for (int z = zCoord - 1; z <= zCoord + 1; z++) {
                    if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
                        if (world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable) HMBlocks.crops) && world.isAirBlock(x, y + 1, z)) {
                            plantCrop(player, world, x, y + 1, z, crop, quality);
                            planted++;

                            if (Crops.ALWAYS_GROW) {
                                if (planted >= 2) {
                                    break labelTop;
                                }
                            }
                        }
                    }
                }
            }

            if (planted > 0) {
                --stack.stackSize;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int getRating(ItemStack stack) {
        Crop crop = getCropFromStack(stack);
        if (crop.isStatic()) return -1;
        else return getCropQuality(stack.getItemDamage()) / 10;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        int cropMeta = getCropType(damage);
        if (pass == 1) return seed_bag_body;
        else if (pass == 2) return seed_bag_neck;
        else {
            Crop crop = CropHelper.getCropFromOrdinal(cropMeta);
            return crop.getItemStack(0, cropMeta, 0).getIconIndex();
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return seed_bag_body;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        Crop crop = CropHelper.getCropFromStack(stack);
        if (pass == 1) return crop.getColor();
        else if (pass == 2) return 0xFFFFFF;
        else return super.getColorFromItemStack(stack, pass);
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 3;
    }

    @Override
    public String getName(ItemStack stack) {
        return getCropFromStack(stack).getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < getMetaCount(); i++) {
            for (int j = 0; j < 110; j += 110) {
                list.add(new ItemStack(item, 1, (j * 110) + i));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        seed_bag_body = register.registerIcon(HMModInfo.MODPATH + ":seed_bag_body");
        seed_bag_neck = register.registerIcon(HMModInfo.MODPATH + ":seed_bag_neck");
    }
}
