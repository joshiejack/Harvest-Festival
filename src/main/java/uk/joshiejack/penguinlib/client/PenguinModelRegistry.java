package uk.joshiejack.penguinlib.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID)
public class PenguinModelRegistry {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent event) {
        CustomLoader.getItems().forEach(IPenguinItem::registerModels);
        RegistryHelper.getItems().forEach(IPenguinItem::registerModels);
        RegistryHelper.clearItems();
    }
}
