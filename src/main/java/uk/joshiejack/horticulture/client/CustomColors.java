package uk.joshiejack.horticulture.client;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.block.HorticultureBlocks;
import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.item.ItemSpores;

@Mod.EventBusSubscriber(modid = Horticulture.MODID, value = Side.CLIENT)
public class CustomColors {
    @SubscribeEvent
    public static void onBlockColors(ColorHandlerEvent.Block event) {
        IBlockColor block = (state, worldIn, pos, tintIndex) -> tintIndex == 0 ?
                (worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) :
                        ColorizerFoliage.getFoliageColorBasic()) : -1;
        event.getBlockColors().registerBlockColorHandler(block, HorticultureBlocks.LEAVES_TEMPERATE);
        event.getBlockColors().registerBlockColorHandler(block, HorticultureBlocks.LEAVES_TROPICAL);
    }

    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event) {
        IItemColor item = (stack, index) -> ColorizerFoliage.getFoliageColorBasic();
        event.getItemColors().registerItemColorHandler(item, HorticultureBlocks.LEAVES_TEMPERATE);
        event.getItemColors().registerItemColorHandler(item, HorticultureBlocks.LEAVES_TROPICAL);
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> ItemSpores.getDataFromStack(stack).getColor(), HorticultureItems.SPORES);
    }
}
