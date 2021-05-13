package uk.joshiejack.penguinlib.util.helpers.forge;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class OreDictionaryHelper {
    private static final Cache<HolderMeta, List<String>> ORE_NAMES = CacheBuilder.newBuilder().build();
    private static final List<String> EMPTY = Lists.newArrayList();

    @SubscribeEvent
    public static void onOreRegistered(OreDictionary.OreRegisterEvent event) {
        ORE_NAMES.invalidateAll();
    }

    public static List<String> getOreNames(ItemStack stack) {
        try {
            return ORE_NAMES.get(new HolderMeta(stack), () -> {
                List<String> names = Lists.newArrayList();
                if (stack.isEmpty()) return names;
                else {
                    int[] ids = OreDictionary.getOreIDs(stack);
                    for (int i: ids) {
                        names.add(OreDictionary.getOreName(i));
                    }
                }

                return names;
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
            return EMPTY;
        }
    }
}
