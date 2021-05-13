package uk.joshiejack.penguinlib.client;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.ICustom;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.block.ICustomBlock;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID, value = Side.CLIENT)
public class ClientCustomLoader extends CustomLoader {
    @SubscribeEvent
    public static void onBlockColors(ColorHandlerEvent.Block event) {
        CustomLoader.getColoredBlocks().forEach(c -> {
            ICustomBlock custom = (ICustomBlock) c;
            event.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
                AbstractCustomData.ItemOrBlock<?, ?> defaults_ = custom.getDefaults();
                AbstractCustomData.ItemOrBlock<?, ?> data = custom.getDataFromState(state);
                return data.color == -1 ? defaults_.getColor(worldIn, pos) : data.getColor(worldIn, pos);
            }, (Block) custom);
        });
    }

    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event) {
        CustomLoader.getColoredItems().forEach(c -> {
            ICustomItem custom = (ICustomItem) c;
            event.getItemColors().registerItemColorHandler((stack, tintIndex) -> {
                AbstractCustomData.ItemOrBlock<?, ?> defaults_ = custom.getDefaults();
                AbstractCustomData.ItemOrBlock<?, ?> data = custom.getDataFromStack(stack);
                return data.color == -1 ? defaults_.getColor(null, null) : data.getColor(null, null); //Null the colors
            }, (Item) custom);
        });
    }

    private static boolean hasColoring(ICustom custom) {
        if (custom.getDefaults().color != -1) return true;
        for (AbstractCustomData.ItemOrBlock<?, ?> data : custom.getStates()) {
            if (data.color != -1) return true;
        }

        return false;
    }
}