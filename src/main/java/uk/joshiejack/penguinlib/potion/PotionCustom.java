package uk.joshiejack.penguinlib.potion;

import uk.joshiejack.penguinlib.data.custom.potion.Attributes;
import uk.joshiejack.penguinlib.data.custom.potion.CustomPotionData;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;

public class PotionCustom extends PenguinPotion {
    public PotionCustom(ResourceLocation resource, CustomPotionData data) {
        super(resource, data.getColor(), data.x, data.y);
        if (data.attributes != null) {
            for (Attributes attribute : data.attributes) {
                IAttribute attribute1 = attribute.get();
                if (attribute1 != null) {
                    registerPotionAttributeModifier(attribute1, attribute.getUUID(resource).toString(), attribute.amount, attribute.operation);
                }
            }
        }
    }
}
