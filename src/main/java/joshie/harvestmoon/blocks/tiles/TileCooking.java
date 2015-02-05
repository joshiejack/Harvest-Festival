package joshie.harvestmoon.blocks.tiles;

import java.util.ArrayList;

import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Utensil;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSyncCooking;
import joshie.lib.helpers.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

public abstract class TileCooking extends TileEntity {
    public static short COOK_TIMER = 100;
    private boolean cooking;
    private short cookTimer = 0;
    private ArrayList<ItemStack> ingredients = new ArrayList();
    private ArrayList<ItemStack> seasonings = new ArrayList();
    private ItemStack result;

    public TileCooking() {}

    public abstract Utensil getUtensil();

    public boolean hasPrerequisites() {
        return true;
    }

    public boolean canAddItems() {
        return result == null;
    }

    public ItemStack getStored() {
        return result;
    }

    public ArrayList<ItemStack> getIngredients() {
        return ingredients;
    }

    public ArrayList<ItemStack> getSeasonings() {
        return seasonings;
    }

    //reset everything ready for the next cooking batch
    public void clear() {
        result = null;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (cooking) {
                cookTimer++;
                if (cookTimer >= COOK_TIMER) {
                    result = FoodRegistry.getResult(getUtensil(), ingredients, seasonings);
                    cooking = false;
                    ingredients = new ArrayList();
                    seasonings = new ArrayList();
                    cookTimer = 0;
                    this.markDirty();
                }

                if (!hasPrerequisites()) {
                    cooking = false;
                    this.markDirty();
                }
            }
        }
    }

    //Returns true if this was a valid ingredient to add
    public boolean addIngredient(ItemStack stack) {
        if (ingredients.size() >= 9) return false;
        if (!hasPrerequisites()) return false;
        if (FoodRegistry.getIngredient(stack) == null) return false;
        else {
            if (worldObj.isRemote) return true;
            ItemStack clone = stack.copy();
            clone.stackSize = 1;
            this.ingredients.add(clone);
            this.cooking = true;
            this.cookTimer = 0;
            this.markDirty();
            return true;
        }
    }

    //Returns true if this was a valid seasoning to add
    public boolean addSeasoning(ItemStack stack) {
        if (!hasPrerequisites()) return false;
        if (FoodRegistry.getSeasoning(stack) == null) return false;
        else {
            if (worldObj.isRemote) return true;
            ItemStack clone = stack.copy();
            clone.stackSize = 1;
            this.seasonings.add(clone);
            this.cooking = true;
            this.cookTimer = Short.MIN_VALUE;
            this.markDirty();
            return true;
        }
    }

    //Called Clientside to update the client
    public void setFromPacket(boolean isCooking, ArrayList<ItemStack> ingredients, ArrayList<ItemStack> seasonings, ItemStack result) {
        this.cooking = isCooking;
        this.ingredients = ingredients;
        this.seasonings = seasonings;
        this.result = result;
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.getPacket(new PacketSyncCooking(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, cooking, ingredients, seasonings, result));
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (!worldObj.isRemote) {
            PacketHandler.sendAround(new PacketSyncCooking(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, cooking, ingredients, seasonings, result), worldObj.provider.dimensionId, xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        cooking = nbt.getBoolean("IsCooking");
        cookTimer = nbt.getShort("CookingTimer");
        if (nbt.hasKey("IngredientsInside")) {
            NBTTagList is = nbt.getTagList("IngredientsInside", 10);
            for (int i = 0; i < is.tagCount(); i++) {
                ingredients.add(StackHelper.getItemStackFromNBT(is.getCompoundTagAt(i)));
            }
        }

        if (nbt.hasKey("SeasoningsInside")) {
            NBTTagList ss = nbt.getTagList("SeasoningsInside", 10);
            for (int i = 0; i < ss.tagCount(); i++) {
                seasonings.add(StackHelper.getItemStackFromNBT(ss.getCompoundTagAt(i)));
            }
        }

        if (nbt.hasKey("Count")) {
            result = StackHelper.getItemStackFromNBT(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("IsCooking", cooking);
        nbt.setShort("CookingTimer", cookTimer);
        //Write out the saved Ingredients
        if (ingredients.size() > 0) {
            NBTTagList is = new NBTTagList();
            for (ItemStack ingredient : ingredients) {
                is.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), ingredient));
            }

            nbt.setTag("IngredientsInside", is);
        }

        //Write out the saved Seasonings
        if (seasonings.size() > 0) {
            NBTTagList ss = new NBTTagList();
            for (ItemStack seasoning : seasonings) {
                ss.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), seasoning));
            }

            nbt.setTag("SeasoningsInside", ss);
        }

        if (result != null) {
            StackHelper.writeItemStackToNBT(nbt, result);
        }
    }
}
