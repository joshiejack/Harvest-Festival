package uk.joshiejack.penguinlib.client.renderer.block;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.block.custom.BlockCustomFloorWithOverlays;
import uk.joshiejack.penguinlib.client.renderer.block.model.BakedFloor;
import uk.joshiejack.penguinlib.data.custom.block.CustomBlockFloorWithOverlays;
import uk.joshiejack.penguinlib.client.util.WeightedFloorOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID, value = Side.CLIENT)
public class FloorClientEvents {
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent event) {
        for (BlockCustomFloorWithOverlays block: BlockCustomFloorWithOverlays.BLOCKS) {
            for (CustomBlockFloorWithOverlays.FloorOverlay overlay: block.overlays) {
                event.getMap().registerSprite(overlay.texture());
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onBaking(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        for (BlockCustomFloorWithOverlays block: BlockCustomFloorWithOverlays.BLOCKS) {
            IBakedModel overlayModel = registry.getObject(new ModelResourceLocation(block.getRegistryName(), "overlay")); //Grab the overlay from the default value
            List<WeightedFloorOverlay> overlays = new ArrayList<>();
            for (CustomBlockFloorWithOverlays.FloorOverlay overlayer: block.overlays) {
                overlays.add(new WeightedFloorOverlay(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(overlayer.texture().toString()), overlayer.weight()));
            }

            //Change the models
            for (BlockCustomFloorWithOverlays.TextureStyle ne : BlockCustomFloorWithOverlays.TextureStyle.values()) {
                for (BlockCustomFloorWithOverlays.TextureStyle nw : BlockCustomFloorWithOverlays.TextureStyle.values()) {
                    for (BlockCustomFloorWithOverlays.TextureStyle se : BlockCustomFloorWithOverlays.TextureStyle.values()) {
                        for (BlockCustomFloorWithOverlays.TextureStyle sw : BlockCustomFloorWithOverlays.TextureStyle.values()) {
                            String state = String.format("ne=%s,nw=%s,se=%s,sw=%s", ne.getName(), nw.getName(), se.getName(), sw.getName());
                            IBakedModel original = registry.getObject(new ModelResourceLocation(block.getRegistryName(), state));
                            registry.putObject(new ModelResourceLocation(block.getRegistryName(), state), new BakedFloor(original, overlayModel, overlays));
                        }
                    }
                }
            }
        }
    }
}
