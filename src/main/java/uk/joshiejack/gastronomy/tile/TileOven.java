package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.api.ItemCookedEvent;
import uk.joshiejack.gastronomy.GastronomySounds;
import uk.joshiejack.gastronomy.tile.base.TileCookingTicking;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

@PenguinLoader("oven")
public class TileOven extends TileCookingTicking {
    private final static int GIVE_TIME = 15;
    public float prevLidAngle;
    public float lidAngle;
    private boolean animating;
    private boolean up = true;
    private EntityPlayer givePlayer;
    private ItemStack giveStack;
    private int giveSlot;
    private int giveTimer;


    public TileOven() {
        super(Appliance.OVEN, COOK_TIMER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void animate() {
        if (cookTimer == 1) {
            world.playSound(getPos().getX(), getPos().getY() + 0.5D, getPos().getZ(), GastronomySounds.OVEN, SoundCategory.BLOCKS, 2F, world.rand.nextFloat() * 0.1F + 0.9F, false);
        }
    }

    @Override
    public void onFinished() {
        super.onFinished();
        world.playSound(null, pos, GastronomySounds.OVEN_DONE, SoundCategory.BLOCKS, 2F, 1F);
    }

    private boolean openDoor() {
        world.playSound(getPos().getX(), getPos().getY() + 0.5D, getPos().getZ(), GastronomySounds.OVEN_DOOR, SoundCategory.BLOCKS, 2F, world.rand.nextFloat() * 0.1F + 0.9F, false);
        if (world.isRemote) {
            animating = true;
        }

        return true;
    }

    @Override
    public boolean handleFluids(EntityPlayer player, EnumHand hand, ItemStack held) {
        return super.handleFluids(player, hand, held) && openDoor();
    }

    @Override
    public boolean addIngredient(ItemStack stack) {
        return super.addIngredient(stack) && openDoor();
    }

    @Override
    protected boolean giveToPlayer(EntityPlayer player, ItemStack result, int slot) {
        if (givePlayer == null) { //Play the sound if we can remove an ingredient
            world.playSound(getPos().getX(), getPos().getY() + 0.5D, getPos().getZ(), GastronomySounds.OVEN_DOOR, SoundCategory.BLOCKS, 2F, world.rand.nextFloat() * 0.1F + 0.9F, false);
            if (world.isRemote) {
                animating = true;
            }

            givePlayer = player;
            giveTimer = GIVE_TIME;
            giveStack = result;
            giveSlot = slot;
        }

        return true;
    }

    @Override
    public void updateTick() {
        if (givePlayer != null && giveStack != null) {
            giveTimer--;

            if (giveTimer <= 0) {
                MinecraftForge.EVENT_BUS.post(new ItemCookedEvent(givePlayer, giveStack, appliance));
                ItemHandlerHelper.giveItemToPlayer(givePlayer, giveStack);
                handler.setStackInSlot(giveSlot, ItemStack.EMPTY);
                givePlayer = null; //Turn it back to null
            }
        }

        super.updateTick();
        //Only do render updates on the client
        if (world.isRemote) {
            prevLidAngle = lidAngle;
            float f1 = 0.025F;
            if (animating) {
                if (up) {
                    lidAngle -= f1;
                } else {
                    lidAngle += f1;
                }

                if (lidAngle < -0.25F) {
                    lidAngle = -0.25F;
                    up = false; //Once we hit critical, go down instead
                }

                if (lidAngle > 0.0F) {
                    lidAngle = 0.0F;
                    animating = false;
                    up = true;
                }
            }
        }
    }
}
