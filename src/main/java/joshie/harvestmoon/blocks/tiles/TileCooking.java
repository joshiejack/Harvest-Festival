package joshie.harvestmoon.blocks.tiles;

import java.util.ArrayList;

import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Utensil;
import joshie.harvestmoon.core.helpers.generic.StackHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSyncCooking;
import joshie.harvestmoon.core.util.generic.IFaceable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class TileCooking extends TileEntity implements IFaceable {
    public static short COOK_TIMER = 100;
    private boolean cooking;
    private short cookTimer = 0;
    private ArrayList<ItemStack> ingredients = new ArrayList();
    private ItemStack result;
    private ForgeDirection orientation = ForgeDirection.NORTH;
    private float rotation;
    protected Utensil utensil;

    public TileCooking() {}

    public Utensil getUtensil() {
        return Utensil.KITCHEN;
    }

    public Utensil getUtensil(World world, int x, int y, int z) {
        return getUtensil();
    }

    public boolean hasPrerequisites() {
        return true;
    }

    public boolean canAddItems() {
        return result == null;
    }

    public ItemStack getStored() {
        return result != null? result.copy(): result;
    }

    public ArrayList<ItemStack> getIngredients() {
        return ingredients;
    }

    //reset everything ready for the next cooking batch
    public void clear() {
        result = null;
    }

    @Override
    public void setFacing(ForgeDirection dir) {
        orientation = dir;
    }

    @Override
    public ForgeDirection getFacing() {
        return orientation;
    }

    public float getRotation() {
        return rotation;
    }

    public void animate(Utensil utensil) {
        rotation += worldObj.rand.nextFloat();
        worldObj.spawnParticle("smoke", xCoord + 0.5D + +worldObj.rand.nextFloat() - worldObj.rand.nextFloat() / 2, yCoord + 0.5D + worldObj.rand.nextFloat() - worldObj.rand.nextFloat() / 2, zCoord + 0.5D + +worldObj.rand.nextFloat() - worldObj.rand.nextFloat() / 2, 0, 0, 0);
    }

    public short getCookingTime(Utensil utensil) {
        return COOK_TIMER;
    }

    public void updateUtensil() {
        if (worldObj.getWorldTime() % 20 == 0) {
            utensil = getUtensil(worldObj, xCoord, yCoord + 1, zCoord);
        }
    }

    @Override
    public void updateEntity() {
        //Update the utensil every second
        updateUtensil();
        //If we are server side perform the actions
        if (!worldObj.isRemote) {
            if (cooking) {
                cookTimer++;
                if (cookTimer >= getCookingTime(utensil)) {
                    result = FoodRegistry.getResult(utensil, ingredients);
                    cooking = false;
                    ingredients = new ArrayList();
                    cookTimer = 0;
                    this.markDirty();
                }

                if (!hasPrerequisites()) {
                    cooking = false;
                    this.markDirty();
                }
            }
        } else if (cooking) animate(utensil);
    }

    //Returns true if this was a valid ingredient to add
    public boolean addIngredient(ItemStack stack) {
        if (ingredients.size() >= 9) return false;
        if (!hasPrerequisites()) return false;
        if (FoodRegistry.getIngredients(stack) == null) return false;
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

    //Called Clientside to update the client
    public void setFromPacket(boolean isCooking, ArrayList<ItemStack> ingredients, ItemStack result) {
        this.cooking = isCooking;
        this.ingredients = ingredients;
        this.result = result;
    }

    public IMessage getPacket() {
        return new PacketSyncCooking(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, orientation, cooking, ingredients, result);
    }

    @Override
    public Packet getDescriptionPacket() {
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
        orientation = ForgeDirection.getOrientation(nbt.getInteger("Orientation"));
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
        nbt.setInteger("Orientation", orientation.ordinal());
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
}
