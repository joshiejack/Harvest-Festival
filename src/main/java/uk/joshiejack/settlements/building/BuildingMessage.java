package uk.joshiejack.settlements.building;

import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ChatHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.util.Strings;

import java.util.Locale;
import java.util.UUID;

import static uk.joshiejack.settlements.Settlements.MODID;

public enum BuildingMessage {
    PLACED {
        @SideOnly(Side.CLIENT)
        @Override
        public void display(Building building, BlockPos pos) {
            World world = Minecraft.getMinecraft().world;
            Town<?> town = TownFinder.find(world, pos);
            EntityPlayer player = Minecraft.getMinecraft().player;
            UUID uuid = PlayerHelper.getUUIDForPlayer(player);
            UUID team = PenguinTeamsClient.getInstance().getID();
            String warning = !uuid.equals(team) && town == TownClient.NULL ? TextFormatting.RED + StringHelper.localize(getPrefix() + "warning") : Strings.EMPTY;
            ChatHelper.displayChat(TextFormatting.YELLOW + building.getLocalizedName() + TextFormatting.RESET + " " + StringHelper.localize(getPrefix() + "marked"),
                    getPrefix() + "confirm", getPrefix() + "cancel", "",
                    (town == TownClient.NULL) ? getPrefix() + "new" : StringHelper.localize(getPrefix() + "existing") + " \"" + town.getCharter().getName() + "\"", "", warning);
        }
    }, CANCELLED {
        @SideOnly(Side.CLIENT)
        @Override
        public void display(Building building, BlockPos pos) {
            ChatHelper.displayChat(TextFormatting.RED + building.getLocalizedName() + " " + StringHelper.localize(PREFIX + "cancelled"));
        }
    }
    , TEAM_BUSY, DIMENSION, BUILDING, LIMIT {
        @SideOnly(Side.CLIENT)
        @Override
        public void display(Building building, BlockPos pos) {
            World world = Minecraft.getMinecraft().world;
            Town<?> town = TownFinder.find(world, pos);
            ChatHelper.displayChat(TextFormatting.RED + StringHelper.format(PREFIX + "limit", town.getCharter().getName()));
        }
    }, DEMOLISH {
        @SideOnly(Side.CLIENT)
        @Override
        public void display(Building building, BlockPos pos) {
            World world = Minecraft.getMinecraft().world;
            Town<?> town = TownFinder.find(world, pos);
            ChatHelper.displayChat(TextFormatting.YELLOW + building.getLocalizedName() + TextFormatting.RESET + " " + StringHelper.localize(getPrefix() + "demolish"),
                    getPrefix() + "confirm", getPrefix() + "cancel", "",
                    (town == TownClient.NULL) ? getPrefix() + "new" : StringHelper.localize(getPrefix() + "existing") + " \"" + town.getCharter().getName() + "\"");
        }
    };

    private static final String PREFIX = MODID + ".message.";

    public String getPrefix() {
        return PREFIX + name().toLowerCase(Locale.ENGLISH) + ".";
    }

    @SideOnly(Side.CLIENT)
    public void display(Building building, BlockPos pos) {
        ChatHelper.displayChat(MODID + ".message." + name().toLowerCase(Locale.ENGLISH));
    }
}
