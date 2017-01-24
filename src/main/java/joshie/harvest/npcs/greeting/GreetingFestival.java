package joshie.harvest.npcs.greeting;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Holiday;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.IConditionalGreeting;
import joshie.harvest.calendar.HolidayRegistry;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class GreetingFestival implements IConditionalGreeting {
    private final String text;
    private final Holiday holiday;

    public GreetingFestival(Holiday holiday) {
        this.holiday = holiday;
        this.text = "harvestfestival.npc.%s.festival." + holiday.getResource().getResourcePath();
    }

    @Override
    public boolean canDisplay(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return HolidayRegistry.INSTANCE.getHoliday(player.worldObj, new BlockPos(player), HFApi.calendar.getDate(player.worldObj)) == holiday;
    }

    @Override
    public double getDisplayChance() {
        return 20D;
    }

    @Override
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        return TextHelper.getRandomSpeech(npc, String.format(text, npc.getRegistryName().getResourcePath()), 10);
    }
}
