package joshie.harvestmoon.plugins.agricraft;

import java.util.ArrayList;
import java.util.WeakHashMap;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.helpers.SeedHelper;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.InfinityRaider.AgriCraft.farming.CropOverride;
import com.InfinityRaider.AgriCraft.tileentity.TileEntityCrop;

public class HMAgricraftOverride extends CropOverride {
    private static final WeakHashMap<Integer, CropOverride> server_cache = new WeakHashMap();
    private static final WeakHashMap<Integer, CropOverride> client_cache = new WeakHashMap();

    private TileEntityCrop crop;
    private World world;
    private int x, y, z;

    public HMAgricraftOverride() {}

    public HMAgricraftOverride(TileEntityCrop crop) {
        this.crop = crop;
        this.world = crop.getWorldObj();
        this.x = crop.xCoord;
        this.y = crop.yCoord;
        this.z = crop.zCoord;
    }

    private static int getKey(World world, int x, int y, int z) {
        int prime = 31;
        int result = 1;
        result = prime * result + world.provider.dimensionId;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    public static CropOverride getOverride(TileEntityCrop crop, WeakHashMap<Integer, CropOverride> map) {
        int key = getKey(crop.getWorldObj(), crop.xCoord, crop.yCoord, crop.zCoord);
        CropOverride override = map.get(key);
        if (override != null) {
            return override;
        } else {
            override = new HMAgricraftOverride(crop);
            map.put(key, override);
            return override;
        }
    }

    public static CropOverride getCropOverride(TileEntityCrop crop) {
        if (crop.getWorldObj() == null) return new HMAgricraftOverride();
        return getOverride(crop, crop.getWorldObj().isRemote ? client_cache : server_cache);
    }

    @Override
    public boolean hasDefaultGrowth() {
        return false;
    }

    @Override
    public void increaseGrowth() {}

    @Override
    public boolean hasDefaultHarvesting() {
        return false;
    }

    @Override
    public void onHarvest() {
        ICropData cropData = HMApi.CROPS.getCropAtLocation(world, x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, 0, 2);

        ICrop crop = cropData.getCrop();
        int quality = cropData.getQuality();
        if (cropData.getCrop().getRegrowStage() > 0) {
            CropHelper.plantCrop(null, world, x, y, z, crop, quality, crop.getRegrowStage());
        } else {
            CropHelper.removeCrop(world, x, y, z);
            this.crop.clearPlant();
        }

        ItemHelper.dropBlockAsItem(world, x, y, z, crop.getCropStackForQuality(quality));
    }

    @Override
    public boolean hasDefaultBreaking() {
        return false;
    }

    @Override
    public void onBreak() {
        if (!world.isRemote) {
            ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
            if (crop.crossCrop) {
                drops.add(new ItemStack(AgriCraft.crops, 2));
            } else {
                drops.add(new ItemStack(AgriCraft.crops, 1));
                if (crop.hasPlant()) {
                    if (AgriCraft.blockCrops.isMature(world, x, y, z)) {
                        drops.add(HMApi.CROPS.getCropAtLocation(world, x, y, z).harvest(null, false));
                    }
                }

                CropHelper.removeCrop(world, x, y, z);
            }

            for (ItemStack drop : drops) {
                ItemHelper.dropBlockAsItem(world, x, y, z, drop);
            }
        }
    }

    @Override
    public void onSeedPlanted(EntityPlayer player) {
        if (!world.isRemote) {
            ICrop theCrop = HMCrops.null_crop;
            int quality = 0;
            if (player != null) {
                theCrop = SeedHelper.getCropFromSeed(player.getCurrentEquippedItem());
                quality = SeedHelper.getQualityFromSeed(player.getCurrentEquippedItem());
            }

            if (theCrop != HMCrops.null_crop) {
                CropHelper.plantCrop(player, world, x, y, z, theCrop, quality, 1);
            } else {
                crop.clearPlant();
            }
        }
    }

    @Override
    public boolean immuneToWeed() {
        return true;
    }

    @Override
    public IIcon getIcon() {
        ICropData data = CropHelper.getCropAtLocation(world, x, y, z);
        if (data == null) return net.minecraft.init.Blocks.obsidian.getIcon(0, 0);
        else {
            return data.getCrop().getCropRenderHandler().getIconForStage(BlockCrop.getSection(world, x, y, z), data.getStage());
        }
    }

    @Override
    public NBTTagCompound saveToNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void loadFromNBT(NBTTagCompound tag) {}
}
