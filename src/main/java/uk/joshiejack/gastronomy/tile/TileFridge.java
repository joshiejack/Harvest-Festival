package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.inventory.slot.SlotFood;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.tile.inventory.TileInventoryRotatable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.handlers.ValidatedStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

@PenguinLoader("fridge")
public class TileFridge extends TileInventoryRotatable implements ITickable {
    private static final float f1 = 0.025F;
    public float prevLidAngleTop;
    public float lidAngleTop;
    public boolean animatingTop;
    public boolean openTop = true;
    public float prevLidAngleBottom;
    public float lidAngleBottom;
    public boolean animatingBottom;
    public boolean openBottom = true;

    public TileFridge() {
        super(35);
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return SlotFood.isValid(stack);
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return !handler.getStackInSlot(slot).isEmpty();
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        player.openGui(Gastronomy.instance, 0, player.world, pos.getX(), pos.getY(), pos.getZ());
        return true; //Open fridge gui
    }

    @Override
    public void update() {
        if (world.isRemote) {
            if (lidAngleTop >= 0.1F && handler.getPlayersWatching() <= 0) {
                animatingTop = true;
                openTop = false;
            }

            if (lidAngleBottom >= 0.1F && handler.getPlayersWatching() <= 0) {
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
    protected ValidatedStackHandler createHandler(int size) {
        return new FridgeInventory(this, size);
    }

    public static class FridgeInventory extends ValidatedStackHandler {
        public FridgeInventory(TileInventory tile, int size) {
            super(tile, size);
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 512;
        }
    }
}
