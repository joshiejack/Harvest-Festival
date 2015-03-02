package joshie.harvestmoon.items;

import joshie.harvestmoon.core.handlers.ChunkTester;
import joshie.harvestmoon.core.handlers.CodeGeneratorBuildings;
import joshie.harvestmoon.core.handlers.CodeGeneratorRendering;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCheat extends ItemHMMeta {
    private static final int COORD_SETTER = 0;
    private static final int CODE_GENERATOR = 1;
    private static final int RENDER_GENERATOR = 2;
    private static final int META_CHECKER = 3;
    private static final int CHUNK_TESTER = 4;
    private static int x1, x2, y1, y2, z1, z2;

    @Override
    public int getMetaCount() {
        return 5;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        int damage = stack.getItemDamage();
        if (damage == COORD_SETTER) {
            if (player.isSneaking()) {
                x2 = xCoord;
                y2 = yCoord;
                z2 = zCoord;
                if (world.isRemote) {
                    MCClientHelper.addToChat("Setting Second Coordinates to " + x2 + " " + y2 + " " + z2);
                }
            } else {
                x1 = xCoord;
                y1 = yCoord;
                z1 = zCoord;
                if (world.isRemote) {
                    MCClientHelper.addToChat("Setting First Coordinates to " + x1 + " " + y1 + " " + z1);
                }
            }
        } else if (damage == CODE_GENERATOR) {
            new CodeGeneratorBuildings(world, x1, y1, z1, x2, y2, z2).getCode(player.isSneaking());
        } else if (damage == RENDER_GENERATOR) {
            new CodeGeneratorRendering().getCode();
        } else if (damage == META_CHECKER) {
            if(world.isRemote) {
                MCClientHelper.addToChat("Block: " + world.getBlock(xCoord, yCoord, zCoord));
                MCClientHelper.addToChat("Metadata: " + world.getBlockMetadata(xCoord, yCoord, zCoord));
                MCClientHelper.addToChat("" + world.getBlock(xCoord, yCoord, zCoord).getClass());
            }
        } else if (damage == CHUNK_TESTER) {
            ChunkTester.activate(world, xCoord, yCoord, zCoord);
        }

        return false;
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case COORD_SETTER:
                return "coord_setter";
            case CODE_GENERATOR:
                return "code_generator";
            case RENDER_GENERATOR:
                return "render_generator";
            case META_CHECKER:
                return "meta_checker";
            case CHUNK_TESTER:
                return "chunk_tester";
            default:
                return "invalid";
        }
    }
}
