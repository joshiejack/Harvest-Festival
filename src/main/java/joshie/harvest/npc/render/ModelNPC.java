package joshie.harvest.npc.render;

import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPC.Mode;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class ModelNPC extends ModelBiped {
    public ModelNPC() {
        super(0.0F);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        setAnglesBasedOnMode((EntityNPC) entity);
    }

    private void setAnglesBasedOnMode(EntityNPC npc) {
        Mode mode = npc.getMode();
        if (mode == Mode.GIFT) {
            bipedRightArm.rotateAngleX = -45F;
            bipedLeftArm.rotateAngleX = -45F;
        }
    }
}
