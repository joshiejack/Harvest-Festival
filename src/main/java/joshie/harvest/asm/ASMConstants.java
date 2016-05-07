package joshie.harvest.asm;

public class ASMConstants {
    public static final class Overrides {
        public static final String SNOW = "joshie.harvest.asm.overrides.BlockSnowSheet";
    }

    public static final class Transformers {
        public static final String PUMPKIN = "joshie.harvest.asm.transformers.PumpkinTransformer";
    }

    //Java
    public static final String HASH_SET = "java.util.HashSet";
    public static final String LIST = "java.util.List";
    public static final String RANDOM = "java.util.Random";
    public static final String STRING = "java.lang.String";

    //Minecraft
    public static final String ACTION_RESULT = "net.minecraft.util.ActionResult";
    public static final String BLOCK = "net.minecraft.block.Block";
    public static final String CREATIVE_TABS = "net.minecraft.creativetab.CreativeTabs";
    public static final String ENTITY_RENDERER = "net.minecraft.client.renderer.EntityRenderer";
    public static final String ENUM_ACTION_RESULT = "net.minecraft.util.EnumActionResult";
    public static final String FACING = "net.minecraft.util.EnumFacing";
    public static final String HAND = "net.minecraft.util.EnumHand";
    public static final String ITEM = "net.minecraft.item.Item";
    public static final String MINECRAFT = "net.minecraft.client.Minecraft";
    public static final String PLAYER = "net.minecraft.entity.player.EntityPlayer";
    public static final String POS = "net.minecraft.util.math.BlockPos";
    public static final String STACK = "net.minecraft.item.ItemStack";
    public static final String STATE = "net.minecraft.block.state.IBlockState";
    public static final String WORLD = "net.minecraft.world.World";

    //Harvest Festival
    public static final String WEATHER_RENDERER = "joshie.harvest.calendar.WeatherRenderer";
}