package joshie.harvest.cooking.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingHelper.PlaceIngredientResult;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
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
    protected NonNullList<ItemStack> result = NonNullList.create();
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

    int getCookTimer() {
        return cookTimer;
    }

    public PlaceIngredientResult hasPrerequisites() {
        return SUCCESS;
    }

    public boolean isFinishedCooking() {
        return result.size() > 0;
    }

    public NonNullList<ItemStack> getResult() {
        return result;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    //reset everything ready for the next cooking batch
    public void giveToPlayer(EntityPlayer player) {
        NonNullList<ItemStack> theItems = getResult();
        EntityBasket.findBasketAndShip(player, theItems);
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
            if (stack.hasTagCompound() && stack.getTagCompound().hasNoTags()) {
                stack.setTagCompound(null);
            }

            SpawnItemHelper.addToPlayerInventory(player, stack);
            ingredients.remove(ingredients.size() - 1); //Remove the last stack
            if (world.isRemote) return;
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
        if (!world.isRemote) {
            if (cooking) {
                cookTimer++;
                if (ingredients.size() == 0) {
                    cooking = false;
                    markDirty();
                } else if (cookTimer >= getCookingTime()) {
                    result.addAll(HFApi.cooking.getCookingResult(getUtensil(), ingredients));
                    cooking = false;
                    ingredients = new ArrayList<>();
                    cookTimer = 0;
                    markDirty();
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
            if (world.isRemote) return true;
            ItemStack clone = stack.copy();
            clone.setCount(1);
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
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        super.handleUpdateTag(tag);

        //Updated the renderer
        doRenderUpdate();
    }

    protected void doRenderUpdate() {
        if (cooking) {
            rotations[last] = world.rand.nextFloat() * 360F;
            offset1[last] = 0.5F - world.rand.nextFloat();
            offset2[last] = world.rand.nextFloat() / 1.75F;
            heightOffset[last] = 0.5F + (ingredients.size() * 0.001F);
        }

        world.markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    @Override
    public void markDirty() {
        if (!world.isRemote) {
            MCServerHelper.markForUpdate(world, getPos());
            MCServerHelper.markForUpdate(world, getPos().down());
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
                ingredients.add(NBTHelper.readItemStack(is.getCompoundTagAt(i)));
            }
        }

        //Resulting item
        result = NonNullList.create();
        if (nbt.hasKey("Result")) {
            NBTTagList is = nbt.getTagList("Result", 10);
            for (int i = 0; i < is.tagCount(); i++) {
                result.add(NBTHelper.readItemStack(is.getCompoundTagAt(i)));
            }
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        nbt.setBoolean("IsCooking", cooking);
        nbt.setShort("CookingTimer", cookTimer);
        nbt.setByte("Last", (byte) last);
        //Write out the saved Ingredients
        if (ingredients.size() > 0) {
            NBTTagList is = new NBTTagList();
            for (ItemStack ingredient : ingredients) {
                is.appendTag(NBTHelper.writeItemStack(ingredient, new NBTTagCompound()));
            }

            nbt.setTag("IngredientsInside", is);
        }

        //Write out the result items
        if (result.size() > 0) {
            NBTTagList is = new NBTTagList();
            for (ItemStack ingredient : result) {
                is.appendTag(NBTHelper.writeItemStack(ingredient, new NBTTagCompound()));
            }

            nbt.setTag("Result", is);
        }

        return super.writeToNBT(nbt);
    }

    boolean isAbove(Utensil utensil) {
        TileEntity tile = world.getTileEntity(pos.down());
        return tile instanceof TileCooking && ((TileCooking)tile).getUtensil() == utensil;
    }
}
