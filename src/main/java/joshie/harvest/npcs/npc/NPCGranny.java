package joshie.harvest.npcs.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.calendar.HolidayRegistry;
import joshie.harvest.shops.HFShops;
import joshie.harvest.api.shops.Shop;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NPCGranny extends NPC {
    public NPCGranny(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource, gender, age, birthday, insideColor, outsideColor);
    }

    @Override
    public Shop getShop(World world) {
        ResourceLocation holiday = HolidayRegistry.INSTANCE.getHoliday(HFApi.calendar.getDate(world));
        if (holiday.equals(HFCalendar.COOKING_FESTIVAL)) return HFShops.COOKING;
        else return super.getShop(world);
    }
}
