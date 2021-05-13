package uk.joshiejack.harvestcore.item.custom;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.harvestcore.data.custom.item.CustomItemAchievement;
import uk.joshiejack.harvestcore.scripting.AchievementScripting;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.item.custom.ItemMultiCustom;

public class ItemAchievement extends ItemMultiCustom<CustomItemAchievement> {
    public ItemAchievement(ResourceLocation registry, CustomItemAchievement defaults, CustomItemAchievement... data) {
        super(registry, defaults, data);
    }

    @Override
    public void init() {
        super.init();
        for (CustomItemAchievement cd: getStates()) {
            AchievementScripting.ACHIEVEMENTS.register(Holder.getFromStack(getCreativeStack(cd)), cd.getScript());
        }
    }
}
