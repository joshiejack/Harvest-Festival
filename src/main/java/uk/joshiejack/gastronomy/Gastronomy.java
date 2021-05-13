package uk.joshiejack.gastronomy;

import com.google.common.collect.Sets;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.gastronomy.api.GastronomyAPI;
import uk.joshiejack.gastronomy.api.IGastronomyAPI;
import uk.joshiejack.gastronomy.block.BlockCookware;
import uk.joshiejack.gastronomy.block.GastronomyBlocks;
import uk.joshiejack.gastronomy.client.gui.GuiCookbook;
import uk.joshiejack.gastronomy.client.gui.GuiCupboard;
import uk.joshiejack.gastronomy.client.gui.GuiFridge;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.gastronomy.inventory.ContainerCupboard;
import uk.joshiejack.gastronomy.inventory.ContainerFridge;
import uk.joshiejack.gastronomy.item.GastronomyItems;
import uk.joshiejack.gastronomy.tile.TileCupboard;
import uk.joshiejack.gastronomy.tile.TileFridge;
import uk.joshiejack.gastronomy.world.NatureGenerator;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;

import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber
@Mod(modid = Gastronomy.MODID, name = "Gastronomy", version = "@GASTRONOMY_VERSION@", dependencies = "required-after:penguinlib")
public class Gastronomy implements IGuiHandler, IGastronomyAPI {
    public static final Set<Class<? extends TileEntity>> FOOD_STORAGE = Sets.newHashSet();
    public static final String MODID = "gastronomy";
    public static final CreativeTabs TAB = new PenguinTab(MODID, () -> GastronomyBlocks.COOKWARE.getStackFromEnum(BlockCookware.Cookware.FRYING_PAN));

    @Mod.Instance(MODID)
    public static Gastronomy instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GastronomyAPI.instance = this;
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, instance);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GastronomyBlocks.init();
        GastronomyItems.init();
        if (GastronomyConfig.enableWorldGen) {
            GameRegistry.registerWorldGenerator(new NatureGenerator(), 0);
            FOOD_STORAGE.add(TileFridge.class);
            FOOD_STORAGE.add(TileCupboard.class);
        }
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileFridge) {
            return new ContainerFridge(player.inventory, (TileFridge) tile);
        } else if (tile instanceof TileCupboard) {
            return new ContainerCupboard(player.inventory, (TileCupboard) tile);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileFridge) {
            return new GuiFridge(new ContainerFridge(player.inventory, (TileFridge) tile));
        } else if (tile instanceof TileCupboard) {
            return new GuiCupboard(new ContainerCupboard(player.inventory, (TileCupboard) tile));
        }

        return GuiCookbook.INSTANCE;
    }

    @Override
    public boolean hasRecipe(ItemStack stack) {
        for (HolderMeta holder: Recipe.RECIPE_BY_STACK.keySet()) {
            if (holder.matches(stack)) return true;
        }

        return false;
    }
}
