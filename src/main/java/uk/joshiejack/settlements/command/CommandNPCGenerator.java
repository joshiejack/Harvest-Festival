package uk.joshiejack.settlements.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import uk.joshiejack.settlements.data.database.NPCLoader;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.npcs.DynamicNPC;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@PenguinLoader
public class CommandNPCGenerator extends AdventureCommand {
    public static List<String> NAMES = new ArrayList<>();

    @Nonnull
    @Override
    public String getName() {
        return "npc";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    public static void createEntityWithClassAndName(EntityPlayer player, String npcClassStr, @Nullable String npcName) {
        TownServer town = TownFinder.getFinder(player.getEntityWorld()).findOrCreate(player, player.getPosition());
        //Let's create a custom npc to show up in the book?
        ResourceLocation uniqueID = new ResourceLocation("custom", UUID.randomUUID().toString());
        DynamicNPC.Builder builder = new DynamicNPC.Builder(player.getEntityWorld().rand, uniqueID);
        NPCLoader.NPCClass npcClass = NPCLoader.NPCClass.REGISTRY.get(npcClassStr);
        if (npcClass != null) builder.setClass(npcClassStr);
        if (NAMES.size() == 0) NAMES.add("No random names registered!");
        if (npcName != null) builder.setPlayerSkin(npcName);
        if (npcName == null) npcName = NAMES.get(player.getEntityWorld().rand.nextInt(NAMES.size()));
        builder.setName(npcName);
        NBTTagCompound data = builder.build();
        town.getCensus().createCustomNPCFromData(player.getEntityWorld(), uniqueID, NPC.CUSTOM_NPC, data);
        EntityNPC entity = town.getCensus().getSpawner().getNPC(player.getEntityWorld(), NPC.CUSTOM_NPC, uniqueID, data, player.getPosition().up());
        if (entity != null) {
            player.getEntityWorld().spawnEntity(entity);
        }
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length >= 1) {
            //npc child joshiejack
            //npc adult darkhax
            EntityPlayer player = sender instanceof EntityPlayer ? (EntityPlayer) sender :
                    FakePlayerHelper.getFakePlayerWithPosition((WorldServer) sender.getEntityWorld(), sender.getPosition());
            createEntityWithClassAndName(player, args[0], args.length == 2 ? args[1] : NAMES.get(sender.getEntityWorld().rand.nextInt(NAMES.size())));
        }
    }
}
