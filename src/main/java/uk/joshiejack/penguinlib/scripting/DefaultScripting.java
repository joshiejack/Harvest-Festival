package uk.joshiejack.penguinlib.scripting;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingBindings;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.*;
import uk.joshiejack.penguinlib.scripting.wrappers.event.HarvestDropsEventJS;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class DefaultScripting {
    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        //Extendable Objects vs Non Extendable
        event.registerExtensible(BiomeJS.class, Biome.class);
        event.register(DataJS.class, NBTTagCompound.class);
        event.register(EntityItemJS.class, EntityItem.class).setDynamic().setSided();
        event.registerExtensible(EntityLivingJS.class, EntityLivingBase.class).setDynamic().setPriority(EventPriority.LOW).setSided();
        event.registerExtensible(EntityJS.class, Entity.class).setDynamic().setPriority(EventPriority.LOWEST).setSided();
        event.register(ItemStackJS.class, ItemStack.class);
        event.register(PlayerStatusJS.class, PlayerJS.class).setSided().disable();
        event.registerExtensible(PlayerJS.class, EntityPlayer.class).setDynamic().setSided();
        event.registerExtensible(PositionJS.class, BlockPos.class);
        event.registerExtensible(StateJS.class, IBlockState.class);
        event.register(TeamStatusJS.class, TeamJS.class).setSided().disable();
        event.register(TeamJS.class, PenguinTeam.class).setSided();
        event.registerExtensible(WorldServerJS.class, WorldServer.class);
        event.registerExtensible(WorldJS.class, World.class).setPriority(EventPriority.LOW);
        event.register(HarvestDropsEventJS.class, BlockEvent.HarvestDropsEvent.class);
    }

    @SubscribeEvent
    public static void onCollectBindings(CollectScriptingBindings event) {
        event.registerEnum(EnumHand.class);
        event.registerEnum(EnumParticleTypes.class);
        event.registerEnum(SoundCategory.class);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("scripting", DefaultScripting.class);
        event.registerFunction("createStack(name)", "return scripting.createItemStack(name);");
        event.registerFunction("createStacks(name)", "return scripting.createItemStacks(name);");
        event.registerFunction("random(min, max)", "return Math.floor(Math.random() * (max - min + 1) + min);");
        event.registerFunction("getState(name)", "return scripting.getState(name);");
        event.registerFunction("fakePlayer(world, pos)", "return scripting.getFakePlayer(name);");
        event.registerFunction("includes(string, substring)", "return string.indexOf(substring) !== -1;");
        event.registerFunction("include(string)", "if (includes(string, ':')) {\n" +
                "\tvar split = string.split(':');\n" +
                "\tvar suffix = (includes(split[1], '.js')) ? (split[1]) : (split[1] + \".js\");\n" +
                "    load('classpath:assets/' + split[0] + '/' + suffix);\n" +
                "}");
        event.registerFunction("hasFunction(x)", "return eval(typeof(x) == typeof(Function));");
    }

    public static NonNullList<ItemStackJS> createItemStacks(String[] stacks) {
        NonNullList<ItemStackJS> list = NonNullList.withSize(stacks.length, ItemStackJS.EMPTY);
        for (int i = 0; i < stacks.length; i++) {
            list.set(i, WrapperRegistry.wrap(StackHelper.getStackFromString(stacks[i])));
        }

        return list;
    }

    public static Map createMap() {
        return Maps.newHashMap();
    }

    public static ItemStackJS createUnbreakableItemStack(String name) {
        ItemStack stack = StackHelper.getStackFromString(name);
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setBoolean("Unbreakable", true);
        return WrapperRegistry.wrap(stack);
    }

    public static ItemStackJS createItemStack(String name) {
        return WrapperRegistry.wrap(StackHelper.getStackFromString(name));
    }

    public static StateJS getState(@Nonnull String name) {
        return WrapperRegistry.wrap(Objects.requireNonNull(StateAdapter.fromString(name)));
    }

    public static boolean isTrue(String script, String function, Object... vars) {
        return Scripting.getResult(new ResourceLocation(script), function, false, vars);
    }

    public static PlayerJS getFakePlayer(WorldServerJS world, PositionJS pos) {
        return WrapperRegistry.wrap(FakePlayerHelper.getFakePlayerWithPosition(world.penguinScriptingObject, pos.penguinScriptingObject));
    }

    public static void call(String script, String function, Object... vars) {
        Scripting.callFunction(new ResourceLocation(function), function, vars);
    }
}
