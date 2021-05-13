package uk.joshiejack.gastronomy.tile.base;

import com.google.common.collect.Lists;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.api.ItemCookedEvent;
import uk.joshiejack.gastronomy.client.renderer.tile.SpecialRenderDataCooking;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.cooking.Ingredient;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.gastronomy.GastronomyConfig;
import uk.joshiejack.gastronomy.item.GastronomyItems;
import uk.joshiejack.gastronomy.network.PacketSyncCooking;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.tile.inventory.TileInventoryRotatable;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRenderData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

import static uk.joshiejack.penguinlib.util.handlers.ValidatedStackHandler.NO_SLOT_FOUND;

public class TileCooking extends TileInventoryRotatable {
    public static final String IN_UTENSIL = "InUtensil";
    public static final int FINISH_SLOT = 20;
    protected static final int COOK_TIMER = 100;
    protected LinkedList<Integer> last = new LinkedList<>();
    private final SpecialRenderData render = createRenderData();
    public final Appliance appliance;
    protected final int timeRequired;
    private boolean cooking;
    protected int cookTimer;

    public enum PlaceIngredientResult {
        SUCCESS, FAILURE, MISSING_OVEN, MISSING_COUNTER
    }

    //Slot id 20 is for the result item
    public TileCooking(Appliance appliance) {
        this(appliance, COOK_TIMER);
    }

    public TileCooking(Appliance appliance, int timeRequired) {
        super(21);
        this.timeRequired = timeRequired;
        this.appliance = appliance;
    }

    public SpecialRenderData createRenderData() {
        return new SpecialRenderDataCooking();
    }

    public SpecialRenderData getRenderData() {
        return render;
    }

    public boolean isCooking() {
        return cooking;
    }

    public void setCooking(boolean cooking, int cookTimer) {
        this.cooking = cooking;
        this.cookTimer = cookTimer;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (last.size() > 0) {
            render.doRenderUpdate(world, pos, last.peekLast());
        }
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        if (last.size() > 0) {
            render.doRenderUpdate(world, pos, last.peekLast());
        }
    }

    @Override //Only allow one item in each slot
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return handler.getStackInSlot(FINISH_SLOT).isEmpty() && !Ingredient.registry.getValue(stack).isNone() && handler.getStackInSlot(slot).isEmpty();
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return !handler.getStackInSlot(FINISH_SLOT).isEmpty();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        //If we've got a finished item in the finish slot, then take it out
        ItemStack result = handler.getStackInSlot(FINISH_SLOT);
        ItemStack held = player.getHeldItem(hand);
        if (!result.isEmpty() && held.getItem() != GastronomyItems.UTENSIL) {
            Recipe recipe = Recipe.RECIPE_BY_STACK.get(new HolderMeta(result));
            if (GastronomyConfig.enableDishRequirement && recipe != null && !recipe.getDish().isEmpty()) {
                if (held.getItem() == recipe.getDish().getItem() && held.getItemDamage() == recipe.getDish().getItemDamage() && held.getCount() >= result.getCount()) {
                    held.shrink(result.getCount()); //Reduce the held item
                    return giveToPlayer(player, result, FINISH_SLOT);
                } else {
                    //TODO: Display error in chat that we don't have the right dish equipped
                    return false;
                }
            } else return giveToPlayer(player, result, FINISH_SLOT);
        } else {
            if (!held.isEmpty()) {
                if (cooking && held.getItem() == GastronomyItems.UTENSIL && appliance == Appliance.COUNTER) {
                    if (result.isEmpty()) {
                        updateTick(); //Call on both sides, to allow for particles if neccessary
                    }

                    return true;
                }

                if (handleFluids(player, hand, held)) {
                    cookTimer = 0;
                    cooking = true;
                    markDirty();
                    return true;
                }

                if (addIngredient(held)) return true;
            }

            //Since we haven't chopped, removed finished or inserted, attempt to remove the last stack
            int lastSlot = last.size() > 0 ? last.pollLast() : NO_SLOT_FOUND;
            if (lastSlot != NO_SLOT_FOUND) {
                ItemStack stack = handler.getStackInSlot(lastSlot);
                if (!stack.isEmpty()) {
                    Cooker.removeUtensilTag(stack);
                    return giveToPlayer(player, stack, lastSlot);
                }
            }
        }

        return false;
    }

    protected boolean handleFluids(EntityPlayer player, EnumHand hand, ItemStack held) {
        return false;
    }

    public boolean addIngredient(ItemStack stack) {
        if (hasPrereqs() != PlaceIngredientResult.SUCCESS) return false;
        Ingredient ingredient = Ingredient.registry.getValue(stack);
        if (!ingredient.isNone()) {
            int slot = handler.findEmptySlot(FINISH_SLOT);
            if (slot != NO_SLOT_FOUND) {
                if (!world.isRemote) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    if (!copy.hasTagCompound()) {
                        copy.setTagCompound(new NBTTagCompound());
                    }

                    copy.getTagCompound().setBoolean(IN_UTENSIL, true);
                    handler.setStackInSlot(slot, copy);
                    stack.shrink(1);
                }

                last.add(slot);
                if (world.isRemote) {
                    render.doRenderUpdate(world, pos, last.peekLast());
                }

                cookTimer = 0;
                cooking = true;
                markDirty();
                return true;
            }
        }

        return false;
    }

    protected boolean giveToPlayer(EntityPlayer player, ItemStack result, int slot) {
        if (!world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new ItemCookedEvent(player, result, appliance));
            ItemHandlerHelper.giveItemToPlayer(player, result);
            handler.setStackInSlot(slot, ItemStack.EMPTY);
        }

        return true;
    }

    @Override //Do cooking stuff
    public void updateTick() {
        if (cooking) {
            cookTimer++;
            if (world.isRemote) {
                animate(); //Animate
            } else {
                if (last.size() == 0) {
                    cooking = false;
                    markDirty();
                } else if (cookTimer >= timeRequired) {
                    NonNullList<ItemStack> stacks = NonNullList.create();
                    for (int i = 0; i < FINISH_SLOT; i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty()) stacks.add(stack);
                        handler.setStackInSlot(i, ItemStack.EMPTY);
                    }

                    handler.setStackInSlot(FINISH_SLOT, Cooker.cook(appliance, stacks, getFluids()));
                    cooking = false;
                    cookTimer = 0;
                    markDirty();
                    onFinished();
                }

                if (hasPrereqs() != PlaceIngredientResult.SUCCESS) {
                    cooking = false;
                    markDirty();
                }
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!world.isRemote) {
            PenguinNetwork.sendToNearby(this, new PacketSyncCooking(pos, cooking, cookTimer));
        }
    }

    public void onFinished() {}

    public List<FluidStack> getFluids() {
        return Lists.newArrayList();
    }

    public PlaceIngredientResult hasPrereqs() {
        return PlaceIngredientResult.SUCCESS;
    }

    boolean isAbove(Appliance appliance) {
        TileEntity tile = world.getTileEntity(pos.down());
        return tile instanceof TileCooking && ((TileCooking)tile).appliance == appliance;
    }

    @SideOnly(Side.CLIENT)
    public void animate() {
        render.rotate(world); //Rotate stuff
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        cooking = nbt.getBoolean("Cooking");
        cookTimer = nbt.getInteger("CookTimer");
        for (int l: nbt.getIntArray("Last")) {
            last.add(l);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Cooking", cooking);
        nbt.setInteger("CookTimer", cookTimer);
        nbt.setIntArray("Last", last.stream().mapToInt(i->i).toArray());
        return super.writeToNBT(nbt);
    }

}
