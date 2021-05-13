package uk.joshiejack.settlements.building;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.penguinlib.template.render.TemplateRendererer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

import static uk.joshiejack.settlements.Settlements.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class BuildingPreview {
    private static final Cache<BuildingPlacement, TemplateRendererer<?>> renderers = CacheBuilder.newBuilder().build();

    @SuppressWarnings("ConstantConditions")
    @Nullable
    private static TemplateRendererer<?> getRenderer(EntityPlayer player) throws ExecutionException {
        ItemStack stack = player.getHeldItemMainhand();
        //renderers.invalidateAll();
        if (stack.getItem() == AdventureItems.BUILDING || stack.getItem() == AdventureItems.BLUEPRINT || stack.getItem() == AdventureItems.DESTROY) {
            //Building building = ;
            BuildingPlacement location = BuildingPlacement.getClientPreview(stack, player);
            if (location != null) {
                TemplateRendererer<?> rendererer = renderers.get(location, () -> new TemplateRendererer<>(location.isDemolish(), new BuildingWorldAccess(location.getBuilding(), location.getRotation()), location.getPosition()));
                rendererer.setPosition(location.getPosition()); //Change the position instead of caching a new renderer each time
                return rendererer;
            }
        }

        return null;
    }

    @SubscribeEvent
    public static void renderBuildingPreview(RenderWorldLastEvent event) throws ExecutionException {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        TemplateRendererer<?> renderer = getRenderer(player);
        if (renderer != null) {
            renderer.render(player, event.getPartialTicks());
        }
    }
}
