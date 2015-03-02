package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.helpers.CropHelper.getCropQuality;
import static joshie.harvestmoon.core.helpers.CropHelper.plantCrop;

import java.util.List;

import joshie.harvestmoon.api.core.IRateable;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.plugins.agricraft.HMAgricraftOverride;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import com.InfinityRaider.AgriCraft.farming.CropOverride;
import com.InfinityRaider.AgriCraft.farming.GrowthRequirement;
import com.InfinityRaider.AgriCraft.farming.ICropOverridingSeed;
import com.InfinityRaider.AgriCraft.tileentity.TileEntityCrop;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(modid = "AgriCraft", iface = "com.InfinityRaider.AgriCraft.farming.ICropOverridingSeed")
public class ItemSeeds extends net.minecraft.item.ItemSeeds implements IRateable, ICropOverridingSeed {
    private IIcon seed_bag_body;
    private IIcon seed_bag_neck;

    public ItemSeeds() {
        super(HMBlocks.crops, Blocks.farmland);
        setCreativeTab(HMTab.tabGeneral);
        setHasSubtypes(true);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        GameRegistry.registerItem(this, name.replace(".", "_"));
        return this;
    }

    public int getMetaCount() {
        return Crop.crops.size();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Crop crop = CropHelper.getCropFromDamage(stack.getItemDamage());
        return (crop == null) ? "Bloody Useless Seeds" : crop.getSeedsName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        Crop crop = CropHelper.getCropFromDamage(stack.getItemDamage());
        if (crop != null) {
            int quality = getCropQuality(stack.getItemDamage());
            for (Season season : crop.getSeasons()) {
                list.add(season.getTextColor() + season.getLocalized());
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (side != 1) {
            return false;
        } else {
            Crop crop = CropHelper.getCropFromDamage(stack.getItemDamage());
            int quality = CropHelper.getCropQuality(stack.getItemDamage());
            int planted = 0;

            if (player.isSneaking()) {
                planted = plantSeedAt(player, stack, world, xCoord, yCoord, zCoord, side, crop, quality, planted);
            } else {
                labelTop: for (int x = xCoord - 1; x <= xCoord + 1; x++) {
                    for (int z = zCoord - 1; z <= zCoord + 1; z++) {
                        planted = plantSeedAt(player, stack, world, x, yCoord, z, side, crop, quality, planted);
                        if (planted < 0) {
                            if (Crops.ALWAYS_GROW) {
                                planted = 2;
                                break labelTop;
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

    private int plantSeedAt(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side, Crop crop, int quality, int planted) {
        if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
            if (crop.getSoilHandler().canSustainPlant(world, x, y + 1, z, (IPlantable) HMBlocks.crops) && world.isAirBlock(x, y + 1, z)) {
                plantCrop(player, world, x, y + 1, z, crop, quality);
                if (!world.isRemote) {
                    world.setBlock(x, y + 1, z, HMBlocks.crops);
                }

                planted++;

                if (Crops.ALWAYS_GROW) {
                    if (planted >= 2) {
                        return -1;
                    }
                }
            }
        }

        return planted;
    }

    @Override
    public int getRating(ItemStack stack) {
        Crop crop = CropHelper.getCropFromDamage(stack.getItemDamage());
        if (crop == null || crop.isStatic()) return -1;
        else return getCropQuality(stack.getItemDamage()) / 10;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        if (pass == 1) return seed_bag_body;
        else if (pass == 2) return seed_bag_neck;
        else {
            Crop crop = CropHelper.getCropFromDamage(damage);
            return crop == null ? seed_bag_neck : crop.getCropStack().getIconIndex();
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return seed_bag_body;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        Crop crop = CropHelper.getCropFromDamage(stack.getItemDamage());
        if (pass == 1 && crop != null) return crop.getColor();
        else if (pass == 2) return 0xFFFFFF;
        else return super.getColorFromItemStack(stack, pass);
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 3;
    }

    public String getName(ItemStack stack) {
        return CropHelper.getCropFromOrdinal(stack.getItemDamage()).getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (Integer i : HMConfiguration.mappings.getMappings()) {
            Crop crop = HMConfiguration.mappings.getCrop(i);
            if (!crop.isStatic()) {
                for (int j = 0; j < 100; j += Crops.CROP_QUALITY_LOOP) {
                    list.add(new ItemStack(item, 1, (j * 100) + i));
                }
            } else list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        seed_bag_body = register.registerIcon(HMModInfo.MODPATH + ":seed_bag_body");
        seed_bag_neck = register.registerIcon(HMModInfo.MODPATH + ":seed_bag_neck");
    }

    //Agricraft
    @Optional.Method(modid = "AgriCraft")
    @Override
    public CropOverride getOverride(TileEntityCrop crop) {
        return HMAgricraftOverride.getCropOverride(crop);
    }

    @Optional.Method(modid = "AgriCraft")
    @Override
    public boolean hasGrowthRequirement() {
        return false;
    }

    @Optional.Method(modid = "AgriCraft")
    @Override
    public GrowthRequirement getGrowthRequirement() {
        return null;
    }
}
