package uk.joshiejack.penguinlib.data.custom.block;

import uk.joshiejack.penguinlib.data.custom.ICustom;
import net.minecraft.block.state.IBlockState;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;

public interface ICustomBlock extends ICustom {
    AbstractCustomData.ItemOrBlock getDataFromState(IBlockState state);
}
