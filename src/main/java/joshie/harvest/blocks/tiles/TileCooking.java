package joshie.harvest.blocks.tiles;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.IAltItem;
import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncCooking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;

public abstract class TileCooking extends TileFaceable {
    public abstract static class TileCookingTicking extends TileCooking implements ITickable {}

    public static short COOK_TIMER = 100;
    private boolean cooking;
    private short cookTimer = 0;
    private ArrayList<ItemStack> ingredients = new ArrayList<ItemStack>();
    private ItemStack result;

    public float[] rotations = new float[20];
    public float[] offset1 = new float[20];
    public float[] offset2 = new float[20];
    public float[] heightOffset = new float[20];

    public TileCooking() {}

    public abstract IUtensil getUtensil();

    public boolean isCooking() {
        return cooking;
    }

    public boolean hasPrerequisites() {
        return true;
    }

    public boolean canAddItems() {
        return result == null;
    }

    public ItemStack getResult() {
        return result != null ? result.copy() : result;
    }

    public ArrayList<ItemStack> getIngredients() {
        return ingredients;
    }

    //reset everything ready for the next cooking batch
    public void clear() {
        result = null;
    }

    public void animate() {}

    public short getCookingTime() {
        return COOK_TIMER;
    }

    public void update() {
        //If we are server side perform the actions
        if (!worldObj.isRemote) {
            if (cooking) {
                cookTimer++;
                if (cookTimer >= getCookingTime()) {
                    result = HFApi.COOKING.getResult(getUtensil(), ingredients);
                    cooking = false;
                    ingredients = new ArrayList<ItemStack>();
                    cookTimer = 0;
                    this.markDirty();
                }

                if (!hasPrerequisites()) {
                    cooking = false;
                    this.markDirty();
                }
            }
        } else if (isCooking()) animate();
    }

    //Returns true if this was a valid ingredient to add
    public boolean addIngredient(ItemStack stack) {
        if (ingredients.size() >= 20) return false;
        if (!hasPrerequisites()) return false;
        if (HFApi.COOKING.getCookingComponents(stack).size() < 1) return false;
        else {
            if (worldObj.isRemote) return true;
            ItemStack clone = getRealIngredient(stack);
            clone.stackSize = 1;
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
    public void setFromPacket(boolean isCooking, ArrayList<ItemStack> ingredientList, ItemStack resultStack) {
        int size = ingredients.size();
        cooking = isCooking;
        ingredients = ingredientList;
        result = resultStack;

        if (isCooking) {
            rotations[size] = worldObj.rand.nextFloat() * 360F;
            offset1[size] = 0.5F - worldObj.rand.nextFloat();
            offset2[size] = worldObj.rand.nextFloat() / 1.75F;
            heightOffset[size] = 0.5F + (ingredients.size() * 0.001F);
        }
    }

    public IMessage getPacket() {
        return new PacketSyncCooking(worldObj.provider.getDimension(), getPos(), orientation, cooking, ingredients, result);
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (!worldObj.isRemote) {
            PacketHandler.sendAround(getPacket(), this);
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

        if (result != null) {
            StackHelper.writeItemStackToNBT(nbt, result);
        }
    }

    public boolean isAbove(Utensil utensil) {
        TileEntity tile = worldObj.getTileEntity(pos.down());
        if (tile instanceof TileCooking) {
            return ((TileCooking)tile).getUtensil() == utensil;
        }

        return false;
    }
}
