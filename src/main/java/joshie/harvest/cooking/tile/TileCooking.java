package joshie.harvest.cooking.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.IAltItem;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingHelper.PlaceIngredientResult;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.MCServerHelper;
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

import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.SUCCESS;

public abstract class TileCooking extends TileFaceable {
    public abstract static class TileCookingTicking extends TileCooking implements ITickable {
        @Override
        public void update() {
            super.update();
        }
    }

    public static final int COOK_TIMER = 100;
    private boolean cooking;
    private short cookTimer = 0;
    private ArrayList<ItemStack> ingredients = new ArrayList<>();
    protected ItemStack result;
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
        return result == null;
    }

    public ItemStack getResult() {
        return result != null ? result.copy() : result;
    }

    public ArrayList<ItemStack> getIngredients() {
        return ingredients;
    }

    //reset everything ready for the next cooking batch
    public void giveToPlayer(EntityPlayer player) {
        ItemStack theItem = getResult();
        if (theItem.hasTagCompound()) {
            player.addStat(HFAchievements.cooking);
        }

        SpawnItemHelper.addToPlayerInventory(player, theItem);
        result = null; //Clear out the result
    }

    public void takeBackLastStack(EntityPlayer player) {
        if (ingredients.size() > 0) {
            SpawnItemHelper.addToPlayerInventory(player, ingredients.get(ingredients.size() - 1));
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
                    result = HFApi.cooking.getResult(getUtensil(), ingredients);
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
    public boolean addIngredient(ItemStack stack) {
        if (ingredients.size() >= 20) return false;
        if (hasPrerequisites() != SUCCESS) return false;
        if (!HFApi.cooking.isIngredient(stack)) return false;
        else {
            if (worldObj.isRemote) return true;
            ItemStack clone = getRealIngredient(stack);
            clone.stackSize = 1;
            this.last = this.ingredients.size();
            this.ingredients.add(clone);
            this.cooking = true;
            this.cookTimer = 0;
            this.markDirty();
            return true;
        }
    }

    private ItemStack getRealIngredient(ItemStack stack) {
        ItemStack alt = null;
        if (stack.getItem() instanceof IAltItem) {
            alt = ((IAltItem)stack.getItem()).getAlternativeWhenCooking(stack);
        }

        return alt == null ? stack.copy() : alt.copy();
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

        if (nbt.hasKey("Count")) {
            result = StackHelper.getItemStackFromNBT(nbt);
        } else result = null;
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

        if (result != null) {
            StackHelper.writeItemStackToNBT(nbt, result);
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
