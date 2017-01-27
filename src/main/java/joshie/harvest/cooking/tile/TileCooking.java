package joshie.harvest.cooking.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingHelper.PlaceIngredientResult;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.SUCCESS;

public abstract class TileCooking extends TileFaceable {
    public static final String IN_UTENSIL = "InUtensil";
    public abstract static class TileCookingTicking extends TileCooking implements ITickable {
        @Override
        public void update() {
            super.update();
        }
    }

    private static final int COOK_TIMER = 100;
    private boolean cooking;
    private short cookTimer = 0;
    private ArrayList<ItemStack> ingredients = new ArrayList<>();
    protected List<ItemStack> result = new ArrayList<>();
    private int last;

    public final float[] rotations = new float[20];
    public final float[] offset1 = new float[20];
    public final float[] offset2 = new float[20];
    public final float[] heightOffset = new float[20];

    public TileCooking() {}

    public abstract Utensil getUtensil();

    public boolean isCooking() {
        return cooking;
    }

    public int getCookTimer() {
        return cookTimer;
    }

    public PlaceIngredientResult hasPrerequisites() {
        return SUCCESS;
    }

    public boolean isFinishedCooking() {
        return result.size() > 0;
    }

    public List<ItemStack> getResult() {
        return result;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    //reset everything ready for the next cooking batch
    public void giveToPlayer(EntityPlayer player) {
        List<ItemStack> theItems = getResult();
        for (ItemStack theItem: theItems) {
            if (theItem.hasTagCompound()) {
                player.addStat(HFAchievements.cooking);
            }

            HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().addAsObtained(theItem);
            SpawnItemHelper.addToPlayerInventory(player, theItem);
        }

        result.clear(); //Clear out the result
    }

    @SuppressWarnings("ConstantConditions")
    public void takeBackLastStack(EntityPlayer player) {
        if (ingredients.size() > 0) {
            ItemStack stack = ingredients.get(ingredients.size() - 1);
            if (stack.hasTagCompound()) stack.getTagCompound().removeTag(IN_UTENSIL);
            SpawnItemHelper.addToPlayerInventory(player, stack);
            ingredients.remove(ingredients.size() - 1); //Remove the last stack
            if (worldObj.isRemote) return;
            this.last = this.ingredients.size();
            this.cooking = true;
            this.cookTimer = 0;
            this.markDirty();
        }
    }

    public void animate() {}

    public short getCookingTime() {
        return COOK_TIMER;
    }

    public void update() {
        if (isCooking()) animate();
        //If we are server side perform the actions
        if (!worldObj.isRemote) {
            if (cooking) {
                cookTimer++;
                if (cookTimer >= getCookingTime()) {
                    result.addAll(HFApi.cooking.getCookingResult(getUtensil(), ingredients));
                    cooking = false;
                    ingredients = new ArrayList<>();
                    cookTimer = 0;
                    this.markDirty();
                }

                if (hasPrerequisites() != SUCCESS) {
                    cooking = false;
                    this.markDirty();
                }
            }
        }
    }

    //Returns true if this was a valid ingredient to add
    @SuppressWarnings("ConstantConditions")
    public boolean addIngredient(ItemStack stack) {
        if (ingredients.size() >= 20) return false;
        if (hasPrerequisites() != SUCCESS) return false;
        if (!HFApi.cooking.isIngredient(stack)) return false;
        else {
            if (worldObj.isRemote) return true;
            ItemStack clone = stack.copy();
            clone.stackSize = 1;
            if (!clone.hasTagCompound()) {
                clone.setTagCompound(new NBTTagCompound());
            }

            clone.getTagCompound().setBoolean(IN_UTENSIL, true);

            this.last = this.ingredients.size();
            this.ingredients.add(clone);
            this.cooking = true;
            this.cookTimer = 0;
            this.markDirty();
            return true;
        }
    }

    //Called Clientside to update the client
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);

        //Update the renderer
        doRenderUpdate();
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);

        //Updated the renderer
        doRenderUpdate();
    }

    protected void doRenderUpdate() {
        if (cooking) {
            rotations[last] = worldObj.rand.nextFloat() * 360F;
            offset1[last] = 0.5F - worldObj.rand.nextFloat();
            offset2[last] = worldObj.rand.nextFloat() / 1.75F;
            heightOffset[last] = 0.5F + (ingredients.size() * 0.001F);
        }

        worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    @Override
    public void markDirty() {
        if (!worldObj.isRemote) {
            MCServerHelper.markForUpdate(worldObj, getPos());
            MCServerHelper.markForUpdate(worldObj, getPos().down());
        }

        super.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        cooking = nbt.getBoolean("IsCooking");
        cookTimer = nbt.getShort("CookingTimer");
        last = nbt.getByte("Last");
        ingredients = new ArrayList<>();
        if (nbt.hasKey("IngredientsInside")) {
            NBTTagList is = nbt.getTagList("IngredientsInside", 10);
            for (int i = 0; i < is.tagCount(); i++) {
                ingredients.add(StackHelper.getItemStackFromNBT(is.getCompoundTagAt(i)));
            }
        }

        //Resulting item
        result = new ArrayList<>();
        if (nbt.hasKey("Result")) {
            NBTTagList is = nbt.getTagList("Result", 10);
            for (int i = 0; i < is.tagCount(); i++) {
                result.add(StackHelper.getItemStackFromNBT(is.getCompoundTagAt(i)));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("IsCooking", cooking);
        nbt.setShort("CookingTimer", cookTimer);
        nbt.setByte("Last", (byte) last);
        //Write out the saved Ingredients
        if (ingredients.size() > 0) {
            NBTTagList is = new NBTTagList();
            for (ItemStack ingredient : ingredients) {
                is.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), ingredient));
            }

            nbt.setTag("IngredientsInside", is);
        }

        //Write out the result items
        if (result.size() > 0) {
            NBTTagList is = new NBTTagList();
            for (ItemStack ingredient : result) {
                is.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), ingredient));
            }

            nbt.setTag("Result", is);
        }

        return super.writeToNBT(nbt);
    }

    public boolean isAbove(Utensil utensil) {
        TileEntity tile = worldObj.getTileEntity(pos.down());
        if (tile instanceof TileCooking) {
            return ((TileCooking)tile).getUtensil() == utensil;
        }

        return false;
    }
}
