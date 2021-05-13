package uk.joshiejack.penguinlib.block.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public interface IInteractable {
    boolean onRightClicked(EntityPlayer player, EnumHand hand);
}
