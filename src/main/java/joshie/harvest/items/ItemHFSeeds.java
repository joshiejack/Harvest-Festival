package joshie.harvest.items;

import static joshie.harvest.core.helpers.CropHelper.plantCrop;

import java.util.List;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.calendar.Season;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.Crop;
import joshie.harvest.init.HFBlocks;
import joshie.harvest.plugins.agricraft.HFAgricraftOverride;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
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
public class ItemHFSeeds extends ItemSeeds implements ICropOverridingSeed, ICreativeSorted {
    private IIcon seed_bag_body;
    private IIcon seed_bag_neck;

    public ItemHFSeeds() {
        super(HFBlocks.crops, Blocks.farmland);
        setCreativeTab(HFTab.tabFarming);
        setHasSubtypes(true);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SEEDS;
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
        if (!stack.hasTagCompound()) {
            return "Corrupted Seeds, Yo!";
        }

        ICrop crop = SeedHelper.getCropFromSeed(stack);
        return (crop == null) ? "Bloody Useless Seeds" : crop.getSeedsName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        if (!stack.hasTagCompound()) return;
        ICrop crop = SeedHelper.getCropFromSeed(stack);
        if (crop != null) {
            for (Season season : crop.getSeasons()) {
                list.add(season.getTextColor() + season.getLocalized());
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (side != 1 || !stack.hasTagCompound()) {
            return false;
        } else {
            ICrop crop = SeedHelper.getCropFromSeed(stack);
            int planted = 0;

            if (player.isSneaking()) {
                planted = plantSeedAt(player, stack, world, xCoord, yCoord, zCoord, side, crop, planted);
            } else {
                labelTop: for (int x = xCoord - 1; x <= xCoord + 1; x++) {
                    for (int z = zCoord - 1; z <= zCoord + 1; z++) {
                        if (crop.growsToSide() == null || !((x == xCoord && z == zCoord))) {
                            planted = plantSeedAt(player, stack, world, x, yCoord, z, side, crop, planted);
                        }

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

    private int plantSeedAt(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side, ICrop crop, int planted) {
        if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
            if (crop.getSoilHandler().canSustainPlant(world, x, y + 1, z, (IPlantable) HFBlocks.crops) && world.isAirBlock(x, y + 1, z)) {
                plantCrop(player, world, x, y + 1, z, crop, 1);
                if (!world.isRemote) {
                    world.setBlock(x, y + 1, z, HFBlocks.crops);
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
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (!stack.hasTagCompound()) return seed_bag_body;
        if (pass == 0) return seed_bag_body;
        else return seed_bag_neck;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIcon(stack, pass, null, null, 0);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return seed_bag_body;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (!stack.hasTagCompound()) return super.getColorFromItemStack(stack, pass);
        ICrop crop = SeedHelper.getCropFromSeed(stack);
        if (pass == 0 && crop != null) return crop.getColor();
        else return super.getColorFromItemStack(stack, pass);
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (ICrop crop : Crop.crops) {
            list.add(SeedHelper.getSeedsFromCrop(crop));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        seed_bag_body = register.registerIcon(HFModInfo.MODPATH + ":seed_bag_body");
        seed_bag_neck = register.registerIcon(HFModInfo.MODPATH + ":seed_bag_neck");
    }

    //Agricraft
    @Optional.Method(modid = "AgriCraft")
    @Override
    public CropOverride getOverride(TileEntityCrop crop) {
        return HFAgricraftOverride.getCropOverride(crop);
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
