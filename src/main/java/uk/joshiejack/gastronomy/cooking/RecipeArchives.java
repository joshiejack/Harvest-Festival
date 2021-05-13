package uk.joshiejack.gastronomy.cooking;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import uk.joshiejack.gastronomy.GastronomyConfig;
import uk.joshiejack.gastronomy.network.PacketSyncAddRecipe;
import uk.joshiejack.gastronomy.network.PacketSyncLearntRecipes;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class RecipeArchives extends WorldSavedData {
    private static final String DATA_NAME = "Gastronomy-Recipes";
    private static RecipeArchives instance;

    private final Multimap<UUID, HolderMeta> recipes = HashMultimap.create();

    public RecipeArchives(String data) {
        super(data);
    }

    public static RecipeArchives get(World world) {
        instance = (RecipeArchives) world.getPerWorldStorage().getOrLoadData(RecipeArchives.class, DATA_NAME);
        if (instance == null) {
            instance = new RecipeArchives(DATA_NAME);
            world.getPerWorldStorage().setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance;
    }

    public boolean learnRecipe(EntityPlayer player, ItemStack stack) {
        HolderMeta meta = new HolderMeta(stack);
        Collection<HolderMeta> collection = recipes.get(PenguinTeams.get(player.world).getTeamUUIDForPlayer(player));
        if (!collection.contains(meta)) {
            PenguinNetwork.sendToClient(new PacketSyncAddRecipe(stack), player);
            collection.add(meta);
            markDirty();
            return true;
        } else return false;
    }

    @SuppressWarnings("ConstantConditions")
    private Collection<HolderMeta> getCollection(EntityPlayer player) {
        Collection<HolderMeta> collection = recipes.get(PenguinTeams.get(player.world).getTeamUUIDForPlayer(player));
        if (!GastronomyConfig.findRecipes) {
            collection.addAll(Recipe.RECIPE_BY_STACK.keySet());  //Add all the recipes
            markDirty(); //Changed so mark dirty
        } else if (GastronomyConfig.vanillaRecipes) {
            Recipe.RECIPE_BY_STACK.keySet().stream()
                    .filter((m) -> m.getStacks().get(0).getItem().getRegistryName().getNamespace().equals("minecraft"))
                    .forEach(collection::add); //Add all the recipes
            markDirty(); //Changed so mark dirty
        }

        return collection;
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Collection<HolderMeta> collection = get(event.player.world).getCollection(event.player);
        PenguinNetwork.sendToClient(new PacketSyncLearntRecipes(collection), event.player);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("Teams", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            Set<HolderMeta> set = Sets.newHashSet();
            NBTHelper.readHolderCollection(tag.getTagList("Recipes", 10), set);
            set.forEach((holder) -> recipes.get(uuid).add(holder));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (UUID uuid: recipes.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", uuid.toString());
            tag.setTag("Recipes", NBTHelper.writeHolderCollection(recipes.get(uuid)));
            list.appendTag(tag);
        }

        nbt.setTag("Teams", list);
        return nbt;
    }
}
