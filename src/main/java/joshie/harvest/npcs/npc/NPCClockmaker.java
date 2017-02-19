package joshie.harvest.npcs.npc;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.shops.HFShops;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NPCClockmaker extends NPC {
    public NPCClockmaker(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource, gender, age, birthday, insideColor, outsideColor);
    }

    @Override
    public Shop getShop(World world, BlockPos pos, @Nullable EntityPlayer player) {
        long time = CalendarHelper.getTime(world);
        if (time >= 8000 && time <= 15000) return super.getShop(world, pos, player);
        else return HFShops.BLOODMAGE;
    }
}
