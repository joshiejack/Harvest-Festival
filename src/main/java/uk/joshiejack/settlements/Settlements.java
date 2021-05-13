package uk.joshiejack.settlements;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.client.gui.GuiQuestBoard;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.npcs.gifts.GiftCategory;
import uk.joshiejack.settlements.npcs.gifts.GiftRegistry;
import uk.joshiejack.settlements.proxy.CommonProxy;
import uk.joshiejack.settlements.world.storage.loot.conditions.ConditionQuest;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Mod(modid = Settlements.MODID, name = "Settlements", version = "@ADVENTURE_VERSION@", dependencies = "required-after:penguinlib")
public class Settlements implements IGuiHandler {
    public static final String MODID = "settlements";
    @SuppressWarnings("ConstantConditions")
    public static final CreativeTabs TAB = new PenguinTab(MODID, () -> AdventureItems.NPC_SPAWNER.getStackFromResource(NPC.all().get(0).getRegistryName()));
    public static final int JOURNAL = 0;
    public static final int QUEST_BOARD = 1;

    @SidedProxy(clientSide = "uk.joshiejack.settlements.proxy.ClientProxy", serverSide = "uk.joshiejack.settlements.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    @Mod.Instance(Settlements.MODID)
    public static Settlements instance;
    public static File directory;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        directory = new File(event.getModConfigurationDirectory(), MODID);
        proxy.preInit();
        LootConditionManager.registerCondition(new ConditionQuest.Serializer());
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, this);
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        if (AdventureConfig.enableGiftLogging) {
            List<ItemStack> stacks = StackHelper.getAllItems();
            for (ItemStack stack : stacks) {
                GiftCategory clazz = GiftRegistry.CATEGORY_REGISTRY.getValue(stack);
                if (clazz.name().equals("none") && GiftRegistry.isValidMod(Objects.requireNonNull(stack.getItem().getRegistryName()).getNamespace())) {
                    Settlements.logger.log(Level.WARN, "Item with the registry name: " + stack.getItem().getRegistryName() + " had no gift class assigned to it!!!, metadata: " + stack.getItemDamage() + " name: " + stack.getDisplayName());
                }
            }
        }
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int guiID, EntityPlayer player, World world, int npcID, int scriptID, int hand) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int npcID, int scriptID, int hand) {
        if (guiID == QUEST_BOARD) {
            return new GuiQuestBoard();
        } else if (guiID == JOURNAL) {
            return GuiJournal.INSTANCE;
        }

        return null;
    }
}
