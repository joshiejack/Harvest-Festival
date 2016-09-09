package joshie.harvest.cooking.tile;

import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.tile.TileCooking.TileCookingTicking;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileOven extends TileCookingTicking {
    @SideOnly(Side.CLIENT)
    public float prevLidAngle;
    @SideOnly(Side.CLIENT)
    public float lidAngle;
    @SideOnly(Side.CLIENT)
    private boolean animating;
    @SideOnly(Side.CLIENT)
    private boolean up = true;

    private EntityPlayer givePlayer;
    private int giveTimer = 0;

    @Override
    public Utensil getUtensil() {
        return Utensil.OVEN;
    }

    @Override
    public void giveToPlayer(EntityPlayer player) {
        if (givePlayer == null) { //Play the sound if we can remove an ingredient
            worldObj.playSound(null, getPos().getX(), getPos().getY() + 0.5D, getPos().getZ(), HFSounds.OVEN_DOOR, SoundCategory.BLOCKS, 2F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            if (worldObj.isRemote) animating = true;
            givePlayer = player;
            giveTimer = 15;
        }
    }

    @Override
    public boolean addIngredient(ItemStack stack) {
        boolean ret = super.addIngredient(stack);
        if (ret) { //Play the sound if we add an ingredient
            worldObj.playSound(null, getPos().getX(), getPos().getY() + 0.5D, getPos().getZ(), HFSounds.OVEN_DOOR, SoundCategory.BLOCKS, 2F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            if (worldObj.isRemote) animating = true;
            return true;
        } else return false;
    }

    @Override
    public void update() {
        super.update();
        if (giveTimer > 0) {
            giveTimer--;
            if (giveTimer <= 0) {
                SpawnItemHelper.addToPlayerInventory(givePlayer, getResult());
                result = null; //Clear out the result
                givePlayer = null;
                giveTimer = 0;
            }
        }

        //Only do render updates on the client
        if (worldObj.isRemote) {
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

    @Override
    public void animate() {
        super.animate();

        if (getCookTimer() == 1) worldObj.playSound(null, getPos(), HFSounds.OVEN, SoundCategory.BLOCKS, 2F, 1F);
        else if (getCookTimer() >= getCookingTime() - 1) {
            worldObj.playSound(null, getPos(), HFSounds.OVEN_DONE, SoundCategory.BLOCKS, 2F, 1F);
        }
    }
}