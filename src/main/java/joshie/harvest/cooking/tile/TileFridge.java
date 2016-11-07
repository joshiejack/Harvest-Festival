package joshie.harvest.cooking.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.base.tile.TileFaceable;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

/** Just a way to interact with the fridge inventory, the fridge inventory is global though, not stored in this block **/
public class TileFridge extends TileFaceable implements ITickable {
    private static final float f1 = 0.025F;
    public float prevLidAngleTop;
    public float lidAngleTop;
    public boolean animatingTop;
    public boolean openTop = true;
    public float prevLidAngleBottom;
    public float lidAngleBottom;
    public boolean animatingBottom;
    public boolean openBottom = true;
    protected final FridgeData data = new FridgeData(this);
    protected final InvWrapper handler = new InvWrapper(data);

    public static boolean isValid(ItemStack stack) {
        return stack.getItem() == HFCooking.MEAL || HFApi.cooking.isIngredient(stack) || (stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).getHealAmount(stack) > 0);
    }

    public FridgeData getContents() {
        return data;
    }

    @Override
    public void update() {
        if (worldObj.isRemote) {
            if (lidAngleTop >= 0.1F && data.getFieldCount() <= 0) {
                animatingTop = true;
                openTop = false;
            }

            if (lidAngleBottom >= 0.1F && data.getFieldCount() <= 0) {
                animatingBottom = true;
                openBottom= false;
            }

            //Top Door
            prevLidAngleTop = lidAngleTop;
            if (animatingTop) {
                if (openTop) {
                    lidAngleTop += f1;
                } else {
                    lidAngleTop -= f1;
                }

                if (lidAngleTop > 0.5F) {
                    lidAngleTop = 0.5F;
                    animatingTop = false;
                    openTop = false; //Once we hit critical, go down instead
                }

                if (lidAngleTop < 0.0F) {
                    lidAngleTop = 0.0F;
                    animatingTop = false;
                    openTop = true;
                }
            }

            //Bottom Door
            prevLidAngleBottom = lidAngleBottom;
            if (animatingBottom) {
                if (openBottom) {
                    lidAngleBottom += f1;
                } else {
                    lidAngleBottom -= f1;
                }

                if (lidAngleBottom > 0.5F) {
                    lidAngleBottom = 0.5F;
                    openBottom = false; //Once we hit critical, go down instead
                    animatingBottom = false;
                }

                if (lidAngleBottom < 0.0F) {
                    lidAngleBottom = 0.0F;
                    animatingBottom = false;
                    openBottom = true;
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) handler;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        data.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        return data.writeToNBT(nbt);
    }
}