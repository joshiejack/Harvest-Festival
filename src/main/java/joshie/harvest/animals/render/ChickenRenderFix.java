package joshie.harvest.animals.render;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChickenRenderFix {
    @SubscribeEvent
    public void renderPost(RenderLivingEvent.Pre event) {
        if (event.entity instanceof EntityChicken) {
            EntityChicken chicken = (EntityChicken) event.entity;
            if (chicken.ridingEntity != null) {
                GL11.glTranslatef(0F, -1.3F, 0F);
            }
        }
    }
}
