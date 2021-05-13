package uk.joshiejack.harvestcore.scripting;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.harvestcore.world.PostalOffice;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class MailScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("mail", MailScripting.class);
    }

    public static void send(PlayerJS playerW, String letter) {
        EntityPlayer player = playerW.penguinScriptingObject;
        PostalOffice.send(player.world, player, Letter.REGISTRY.get(new ResourceLocation(letter)));
    }

    @SuppressWarnings("unchecked")
    public static void remove(PlayerJS playerW, String letter) {
        EntityPlayer player = playerW.penguinScriptingObject;
        PostalOffice.deliver(player.world, player, Letter.REGISTRY.get(new ResourceLocation(letter)));
    }
}
