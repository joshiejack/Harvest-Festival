package joshie.harvest.cooking.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.base.tile.TileFaceable;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Just a way to interact with the fridge inventory, the fridge inventory is global though, not stored in this block **/
public class TileFridge extends TileFaceable implements ITickable {
    private static final float f1 = 0.025F;
    @SideOnly(Side.CLIENT)
    public float prevLidAngleTop;
    @SideOnly(Side.CLIENT)
    public float lidAngleTop;
    @SideOnly(Side.CLIENT)
    public boolean animatingTop;
    @SideOnly(Side.CLIENT)
    public boolean openTop = true;
    @SideOnly(Side.CLIENT)
    public float prevLidAngleBottom;
    @SideOnly(Side.CLIENT)
    public float lidAngleBottom;
    @SideOnly(Side.CLIENT)
    public boolean animatingBottom;
    @SideOnly(Side.CLIENT)
    public boolean openBottom = true;
    protected FridgeData data = new FridgeData(this);

    public static boolean isValid(ItemStack stack) {
        return stack.getItem() == HFCooking.MEAL || HFApi.cooking.isIngredient(stack) || stack.getItem() instanceof ItemFood;
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